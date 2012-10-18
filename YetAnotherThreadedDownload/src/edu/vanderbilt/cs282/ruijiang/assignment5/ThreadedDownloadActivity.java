//Rui Jiang

package edu.vanderbilt.cs282.ruijiang.assignment5;

import java.io.InputStream;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ThreadedDownloadActivity extends Activity {
    private EditText url_box;
    private ImageView image_view;
    private Drawable default_drawable;
    private ProgressDialog prog_diag;
    private Context context;
    private IDownloadBoundServiceSync sync_service;
    private IDownloadBoundServiceAsync async_service;
    private boolean sync_bound;
    private boolean async_bound;
    private IDownloadBoundServiceAsyncCallback async_callback = new IDownloadBoundServiceAsyncCallback() {
        @Override
        public IBinder asBinder() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void onDownloadFinished(String file_name) throws RemoteException {
            displayBitmap(file_name);
        }
        @Override
        public void onError(String error_message) throws RemoteException {
            CharSequence text = "There has been an error with the download.\nPlease ensure the URL entered is correct and you have Internet access.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            Log.d(getClass().getSimpleName(), "Failed to display image");
        }
    };
    private ServiceConnection sync_service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iservice) {
            sync_service = IDownloadBoundServiceSync.Stub.asInterface(iservice);
            sync_bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            sync_service = null;
            sync_bound = false;            
        }
    };
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
        Resources res = getResources();        
        context = this;
        setContentView(R.layout.activity_threaded_download);
        url_box = (EditText) findViewById(R.id.editText1);
        image_view = (ImageView) findViewById(R.id.imageView1);
        default_drawable = res.getDrawable(R.drawable.default_img);
        sync_bound = false;
        async_bound = false;
        bindService(new Intent(context, DownloadBoundServiceSync.class), sync_service_connection, context.BIND_AUTO_CREATE);
        bindService(new Intent(context, DownloadBoundServiceAsync.class), async_service_connection, context.BIND_AUTO_CREATE);
    }

    public void runSyncAIDL(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via sync AIDL");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Sync AIDL", true);
        if (sync_bound) {
            try {
                displayBitmap(sync_service.getFileName(url_box.getText().toString()));
            } catch (RemoteException e) {
                Log.d(getClass().getSimpleName(), "Error", e);
            }
        } else {
            CharSequence text = "The Service is not bound. Attempting to bind again.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            bindService(new Intent(context, DownloadBoundServiceSync.class), sync_service_connection, context.BIND_AUTO_CREATE);
        }
        prog_diag.dismiss();
    }
    
    public void runAsyncAIDL(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via async AIDL");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Async AIDL", true);
        if (async_bound) {
            try {
                async_service.setCallback(async_callback, url_box.getText().toString());
            } catch (RemoteException e) {
                Log.d(getClass().getSimpleName(), "Error", e);
            }
        } else {
            CharSequence text = "The Service is not bound. Attempting to bind again.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            bindService(new Intent(context, DownloadBoundServiceAsync.class), async_service_connection, context.BIND_AUTO_CREATE);
        }
        prog_diag.dismiss();
    }
    
    public void resetImage(View view) {
        image_view.setImageDrawable(default_drawable);
        Log.d(getClass().getSimpleName(), "Image reset");
    }
    
    public void displayBitmap(String file_name) {
        try {
            Log.d(getClass().getSimpleName(), "Attempting to display image");
            //read file using an InputStream and decode it using a BitmapFactory
            InputStream in_stream = (InputStream) context.openFileInput(file_name);
            Bitmap bitmap = BitmapFactory.decodeStream(in_stream);
            image_view.setImageBitmap(bitmap);
        } catch (Exception e) {
            CharSequence text = "There has been an error with the download.\nPlease ensure the URL entered is correct and you have Internet access.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            Log.d(getClass().getSimpleName(), "Failed to display image", e);
        }
    }
    
    @Override
    public void onStop() {
        if (sync_bound || async_bound) {
            unbindService(sync_service_connection);
            unbindService(async_service_connection);
        }
        super.onStop();
    }
}
