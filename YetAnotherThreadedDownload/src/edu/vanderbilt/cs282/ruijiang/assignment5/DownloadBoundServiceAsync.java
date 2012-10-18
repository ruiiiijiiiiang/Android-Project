//Rui Jiang

package edu.vanderbilt.cs282.ruijiang.assignment5;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DownloadBoundServiceAsync extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    final String file_name = "downloaded_image.jpg";
    
    private final IDownloadBoundServiceAsync.Stub mBinder = new IDownloadBoundServiceAsync.Stub() {
        @Override
        public void setCallback(IDownloadBoundServiceAsyncCallback callback, final String url) throws RemoteException {
            Thread my_download = new Thread() {
                @Override
                public void run() {
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
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "Download failed", e);
                    }
                }
            };
            my_download.start();
            try {
                my_download.join();
                callback.onDownloadFinished(file_name);
            } catch (InterruptedException e) {
                Log.d(getClass().getSimpleName(), "Download failed", e);
                callback.onError(null);
            }
        }
    };
}
