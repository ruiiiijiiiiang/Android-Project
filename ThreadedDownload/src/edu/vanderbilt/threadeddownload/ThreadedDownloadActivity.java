package edu.vanderbilt.threadeddownload;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ThreadedDownloadActivity extends Activity {

    public EditText url_box;
    public ImageView image_view;
    public Drawable default_drawable;
    public ProgressDialog prog_diag;
    public Context context;
    public String url;
    public Handler runnable_handler;
    public Handler message_handler;
    //Comment by Dr Schimdt:
    //Please use Java enums instead of ints if you can.
    //public final int START_PROG_DIAG = 0;
    //public final int STOP_PROG_DIAG = 1;
    //public final int SET_IMAGE_VIEW = 2;
    public enum DownloadStatus {
        START_PROG_DIAG,
        STOP_PROG_DIAG,
        SET_IMAGE_VIEW
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threaded_download);
        
        Resources res = getResources();        
        context = this;
        url_box = (EditText) findViewById(R.id.editText1);
        image_view = (ImageView) findViewById(R.id.imageView1);
        default_drawable = res.getDrawable(R.drawable.default_img);
        runnable_handler = new Handler();
        message_handler = new Handler() {
            DownloadStatus[] ds = DownloadStatus.values();
            public void handleMessage(Message msg) {
                switch (ds[msg.what]) {
                    case START_PROG_DIAG: {
                        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Message", true);
                        break; 
                    }
                    case SET_IMAGE_VIEW: {
                        image_view.setImageBitmap((Bitmap) msg.obj); 
                        break;
                    }
                    case STOP_PROG_DIAG: {
                        prog_diag.dismiss(); 
                        break; 
                    }
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_threaded_download, menu);
        return true;
    }
    
    Bitmap downloadBitmap (String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Log.d(getClass().getSimpleName(), "Download succeeded");
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Download failed");
            return null;
        }
    }

    public void runRunnable(View view) {
        Log.d(getClass().getSimpleName(), "Downloading via Runnable");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Runnable", true);
        new Thread(new DownloadRunnable()).start();
    }

    public void runMessages(View view) {
        Log.d(getClass().getSimpleName(), "Downloading via Message");
        new Thread(new DownloadMessage()).start();

    }

    public void runAsyncTask(View view) {
        Log.d(getClass().getSimpleName(), "Downloading via AsyncTask");
        new DownloadAsyncTask().execute(url_box.getText().toString());
    }

    public void resetImage(View view) {
        Log.d(getClass().getSimpleName(), "Reseting image");
        image_view.setImageDrawable(default_drawable);
    }
    
    //Runnable class for use with the Runnables and Handlers model
    private class DownloadRunnable implements Runnable {
        @Override
        public void run() {
            final Bitmap bm = downloadBitmap(url_box.getText().toString());
            runnable_handler.post(new Runnable() {
                @Override
                public void run() {
                    image_view.setImageBitmap(bm);
                    prog_diag.dismiss();
                }
            });
        }
    }
    
    //AsyncTask class for use with the AsyncTask model
    private class DownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        public void onPreExecute() {
            prog_diag = ProgressDialog.show(context, "Download", "Downloading via AsyncTask", true);
        }
        
        @Override
        public Bitmap doInBackground(String... url) {
            return downloadBitmap(url[0]);
        }

        @Override
        public void onProgressUpdate(Void... voids) {
        }

        @Override
        public void onPostExecute(Bitmap bm) {
            prog_diag.dismiss();
            image_view.setImageBitmap(bm);
        }
    }
    
    //Message class for use with the Messages and Handlers model
    private class DownloadMessage implements Runnable {
        @Override
        public void run() {
            Message msg = message_handler.obtainMessage(DownloadStatus.START_PROG_DIAG.ordinal());
            message_handler.sendMessage(msg);
            final Bitmap bm = downloadBitmap(url_box.getText().toString());
            msg = message_handler.obtainMessage(DownloadStatus.SET_IMAGE_VIEW.ordinal(), bm);
            message_handler.sendMessage(msg);
            msg = message_handler.obtainMessage(DownloadStatus.STOP_PROG_DIAG.ordinal());
            message_handler.sendMessage(msg);
        }
    }
}
