//Rui Jiang

package edu.vanderbilt.cs282.ruijiang.assignment5;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class DownloadBoundServiceSync extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    private final IDownloadBoundServiceSync.Stub mBinder = new IDownloadBoundServiceSync.Stub() {
        public String downloadFileAndReturnFileName(final String url){
            //set the policy to allow downloading on the main thread
            //this will be safe since the service runs on a different process
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.d("SYNC", "Returning from Sync Service");
            return DownloadUtil.downloadImage(getApplicationContext(), url);
        }
    };
}
