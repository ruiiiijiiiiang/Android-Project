package edu.vanderbilt.cs282.ruijiang.assignment6;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DownloadActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //the loader's unique id
    private static final int LOADER_ID = 0;
    private EditText url_box;
    private Context context;
    private ListView display_view;
    //service interface
    private IDownloadBoundServiceAsync async_service;
    private boolean async_bound = false;
    //implement the callback to receive information from the async service
    private IDownloadBoundServiceAsyncCallback.Stub async_callback = new IDownloadBoundServiceAsyncCallback.Stub() {
        @Override
        public void onDownloadFinished(final String uri) throws RemoteException {
            Log.d(getClass().getSimpleName(), "Callback received");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //display a toast showing the file uri if downloaded successfully, otherwise show an error
                    Toast toast;
                    if (uri != null) {
                        toast = Toast.makeText(context, uri, Toast.LENGTH_LONG);
                    } else {
                        String error = "Download failed. Please make sure the URL you entered is valid.";
                        toast = Toast.makeText(context, error, Toast.LENGTH_LONG);
                    }
                    toast.show();
                }
            });
        }
    };
    //this is for the activity to connect to the async service
    private ServiceConnection async_service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            async_service = IDownloadBoundServiceAsync.Stub.asInterface(service);
            async_bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            async_service = null;
            async_bound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_download);
        url_box = (EditText) findViewById(R.id.editText1);
        display_view = (ListView) findViewById(R.id.listView1);
        // bind both services when the application starts
        bindService(new Intent(context, DownloadBoundServiceAsync.class),
                async_service_connection, context.BIND_AUTO_CREATE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //the constuctor only needs to return a CursorLoader constructed by the proper query
        return new CursorLoader(context, DownloadContentProvider.CONTENT_URI,
                null, null, null, FileDatabase.COL_TIMESTAMP + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //the adapter is used for showing the images; it's set to the ListView in the activity interface
        DisplayCursorAdapter adapter = new DisplayCursorAdapter(context, cursor);
        display_view.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //nothing needs to be done here
    }

    //the following code is modified from Dr. Schmidt's notes
    // This class is used to implement both the Command pattern
    // (execute) and the Asynchronous Completion Token pattern (by
    // virtue of inheriting from AsyncQueryHandler).
    abstract class CompletionHandler extends AsyncQueryHandler {
        CompletionHandler() {
            super(getContentResolver());
        }

        abstract public void execute();
    }

    class QueryQueryHandler extends CompletionHandler {
        //similar structure as the CursorLoader
        public void execute() {
            startQuery(0, null, DownloadContentProvider.CONTENT_URI, (String[]) null,
                    (String) null, (String[]) null, (String) FileDatabase.COL_TIMESTAMP + " DESC");
        }

        public void onQueryComplete(int t, Object command, Cursor cursor) {
            DisplayCursorAdapter adapter = new DisplayCursorAdapter(context, cursor);
            display_view.setAdapter(adapter);
        }
    }

    //action for download button
    public void downloadFile(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via async AIDL");
        if (async_bound) {
            try {
                // attach the callback to the async service
                async_service.setCallback(async_callback, url_box.getText().toString());
            } catch (RemoteException e) {
                Log.d(getClass().getSimpleName(), "Error", e);
            }
        } else {
            // try to rebind the service if it's not bound
            CharSequence text = "The Service is not bound. Attempting to bind again.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            bindService(new Intent(context, DownloadBoundServiceAsync.class),
                    async_service_connection, context.BIND_AUTO_CREATE);
        }
    }

    //action for query button
    public void query(View view) {
        //needs to run on a non-UI thread to prevent blocking
        (new Thread() {
            @Override
            public void run() {
                final Cursor cursor = getContentResolver().query(
                        DownloadContentProvider.CONTENT_URI, null, null, null,
                        FileDatabase.COL_TIMESTAMP + " DESC");
                //needs to run on the UI-thread to modify the ImageView
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayCursorAdapter adapter = new DisplayCursorAdapter(
                                context, cursor);
                        display_view.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    //action for cursorloader button
    public void cursorLoader(View view) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    //action for asyncqueryhandler button
    public void asyncQueryHandler(View view) {
        new QueryQueryHandler().execute();
    }

    //action for reset button
    public void resetImage(View view) {
        display_view.setAdapter(null);
    }

    // handling screen rotation
    /**
     * This prevents the Activity from being destroyed while an operation is
     * outstanding. A more complicated Activity would require different
     * techniques, but this is sufficient for this Activity.
     */
    @Override
    public final void onConfigurationChanged(Configuration newConfig) {
        Log.d(getClass().getSimpleName(), "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        // the service needs to be unbound before the application exits
        if (async_bound) {
            unbindService(async_service_connection);
        }
        super.onDestroy();
    }
}
