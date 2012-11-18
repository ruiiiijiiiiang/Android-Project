//Rui Jiang

package edu.vanderbilt.cs282.ruijiang.assignment6;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DownloadBoundServiceAsync extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    private final IDownloadBoundServiceAsync.Stub mBinder = new IDownloadBoundServiceAsync.Stub() {
        @Override
        public void setCallback(final IDownloadBoundServiceAsyncCallback callback, final String url) throws RemoteException {
            Log.d("ASYNC", "Sending callback from Async Service");
            callback.onDownloadFinished(DownloadUtil.downloadImage(getApplicationContext(), url));
        }
    };
}
