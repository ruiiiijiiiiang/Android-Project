package com.example.anotherthreadeddownload;

import java.io.InputStream;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ThreadedDownloadActivity extends Activity {
    EditText url_box;
    ImageView image_view;
    Drawable default_drawable;
    ProgressDialog prog_diag;
    Context context;
    String url;
    Messenger messenger;
    PendingIntent pending_intent;
    enum download_method {THREADED_MESSENGER, PENDING_INTENT, ASYNC_RECEIVER}
    
    public Handler handler;

    void displayBitmap (String pathname) {
        try {
            Log.d(getClass().getSimpleName(), "Attempting to display image");
            //read file using an InputStream and decode it using a BitmapFactory
            InputStream in_stream = (InputStream) context.openFileInput(pathname);
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
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        Resources res = getResources();        
        context = this;
        setContentView(R.layout.activity_threaded_download);
        url_box = (EditText) findViewById(R.id.editText1);
        image_view = (ImageView) findViewById(R.id.imageView1);
        default_drawable = res.getDrawable(R.drawable.default_img);
        pending_intent = createPendingResult(ThreadedDownloadService.KEY_ID, new Intent(), 0);
        //handler to handle the messenger
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d(getClass().getSimpleName(), "Downloaded via threaded mMessenger");
                displayBitmap(msg.getData().getString("FILENAME"));
                prog_diag.dismiss();
            }
        };
        messenger = new Messenger(handler);
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroy");
        if (!stopService(new Intent(context, ThreadedDownloadService.class))) {
            Log.d(getClass().getSimpleName(), "Failed to stop service");
        }
        super.onDestroy();
    }

    public void runThreadedMessenger(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via threaded messenger");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Threaded Messenger", true);
        Intent intent = new Intent(context, ThreadedDownloadService.class);
        intent.putExtra("DOWNLOAD_METHOD", download_method.THREADED_MESSENGER.ordinal());
        intent.putExtra("URL", url_box.getText().toString());
        intent.putExtra("MESSENGER", messenger);
        startService(intent);
    }

    public void runThreadedPendingIntent(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via pending intent");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Pending Intent", true);
        Intent intent = new Intent(context, ThreadedDownloadService.class);
        intent.putExtra("DOWNLOAD_METHOD", download_method.PENDING_INTENT.ordinal());
        intent.putExtra("URL", url_box.getText().toString());
        intent.putExtra("PENDING_INTENT", pending_intent);
        startService(intent);
    }

    public void runAsyncTaskReceiver(View view) {
        Log.d(getClass().getSimpleName(), "Attempting to download via async receiver");
        prog_diag = ProgressDialog.show(context, "Download", "Downloading via Async Receiver", true);
        Intent intent = new Intent(context, ThreadedDownloadService.class);
        intent.putExtra("DOWNLOAD_METHOD", download_method.ASYNC_RECEIVER.ordinal());
        intent.putExtra("URL", url_box.getText().toString());
        startService(intent);
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            displayBitmap(intent.getStringExtra("FILENAME"));
            Log.d(getClass().getSimpleName(), "Downloaded via Async Receiver");
            prog_diag.dismiss();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ThreadedDownloadService.ACTION_COMPLETE);
        registerReceiver(onEvent, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(onEvent);
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ThreadedDownloadService.KEY_ID) {
            displayBitmap(data.getStringExtra("FILENAME"));
            Log.d(getClass().getSimpleName(), "Downloaded via Pending Intent");
            prog_diag.dismiss();
        }
    }

    public void resetImage(View view) {
        image_view.setImageDrawable(default_drawable);
        Log.d(getClass().getSimpleName(), "Image reset");
    }
}
