package com.example.anotherthreadeddownload;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ThreadedDownloadService extends Service {
    public final static int KEY_ID = 127001; //totally random
    public final static String ACTION_COMPLETE = "done"; //totally random
    public enum download_method {THREADED_MESSENGER, PENDING_INTENT, ASYNC_RECEIVER}

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    String downloadFile (String url) {
        String file_name = "downloaded_image.jpg";
        try {
            //store the content of the image to an InputStream
            InputStream in_stream = (InputStream) new URL(url).getContent();
            int nRead;
            byte[] data = new byte[1024]; //totally random
            //write from the InputStream to a file
            FileOutputStream out_stream = getApplicationContext().openFileOutput(file_name, Context.MODE_WORLD_READABLE);
            while ((nRead = in_stream.read(data, 0, data.length)) != -1) {
                out_stream.write(data, 0, nRead);
            }
            out_stream.close();
            Log.d(getClass().getSimpleName(), "Download succeeded");
            return file_name;
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Download failed", e);
            return null;
        }
    }

    private void sendFileName (String file_name, Messenger messenger) {
        Message msg = Message.obtain();
        Bundle b = new Bundle();
        b.putString("FILENAME", file_name);
        msg.setData(b);
        try {
            messenger.send(msg);
            Log.d(getClass().getSimpleName(), "Message sent");
        } catch (RemoteException e) {
            Log.d(getClass().getSimpleName(), "Failed to send message", e);
        }
    }

    private void threadMessageDownload (final Intent intent) {
        (new Thread() {
            @Override
            public void run() {
                String fileName = downloadFile(intent.getStringExtra("URL"));
                sendFileName(fileName, (Messenger)intent.getParcelableExtra("MESSENGER"));
            }
        }).start();
    }

    private void threadPendingIntentDownload (final Intent intent) {
        (new Thread() {
            @Override
            public void run() {
                PendingIntent pending_intent = (PendingIntent) intent.getParcelableExtra("PENDING_INTENT");
                Intent reply_intent = new Intent();
                Log.d(getClass().getSimpleName(), "Downloading via pending intent");
                reply_intent.putExtra("FILENAME", downloadFile(intent.getStringExtra("URL")));
                try {
                    pending_intent.send(ThreadedDownloadService.this, KEY_ID, reply_intent);
                    Log.d(getClass().getSimpleName(), "Pending intent sent");
                } catch (Exception e) {
                    Log.d(getClass().getSimpleName(), "Failed to send pending intent", e);
                }
            }
        }).start();
    }

    private void asyncTaskDownload (final Intent intent) {
        (new Thread() {
            @Override
            public void run() {
                Intent reply_intent = new Intent(ACTION_COMPLETE);
                Log.d(getClass().getSimpleName(), "Downloading via Async Receiver");
                reply_intent.putExtra("FILENAME", downloadFile(intent.getStringExtra("URL")));
                try {
                    sendBroadcast(reply_intent);
                    Log.d(getClass().getSimpleName(), "Broadcast sent");
                } catch (Exception e) {
                    Log.d(getClass().getSimpleName(), "Failed to send broadcast", e);
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(), "Service started");
        //decide what download method to use
        download_method[] dm = download_method.values();
        switch (dm[intent.getIntExtra("DOWNLOAD_METHOD", 0)]) {
            case THREADED_MESSENGER: {
                Log.d(getClass().getSimpleName(), "Attempting to call threadedMessengerDownload");
                threadMessageDownload(intent);
                break;
            }
            case PENDING_INTENT: {
                Log.d(getClass().getSimpleName(), "Attempting to call threadPendingIntentDownload");
                threadPendingIntentDownload(intent);
                break;
            }
            case ASYNC_RECEIVER: {
                Log.d(getClass().getSimpleName(), "Attempting to call asyncTaskDownload");
                asyncTaskDownload(intent);
                break;
            }
            default: {
                Log.d(getClass().getSimpleName(), "Error");
                break;
            }
        }
        return Service.START_STICKY;
    }
}
