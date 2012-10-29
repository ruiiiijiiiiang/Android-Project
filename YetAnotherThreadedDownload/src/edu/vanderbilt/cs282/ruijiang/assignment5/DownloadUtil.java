package edu.vanderbilt.cs282.ruijiang.assignment5;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.util.Log;

public class DownloadUtil{
    private final static int BUFFERSIZE = 1024;
    
    //only one static function since no object of this class needs to be instantiated
    public static String downloadImage(Context context, String url) {
        try {
            //store the content of the image to an InputStream
            Log.d("DOWNLOAD", "Retrieving content from URL");
            InputStream in_stream = (InputStream) new URL(url).getContent();
            int nRead;
            byte[] data = new byte[BUFFERSIZE];
            String file_name = "downloaded_image.jpg";
            //write from the InputStream to a file
            Log.d("DOWNLOAD", "Writing data to file");
            FileOutputStream out_stream = context.openFileOutput(file_name , Context.MODE_WORLD_READABLE);
            while ((nRead = in_stream.read(data, 0, data.length)) != -1) {
                out_stream.write(data, 0, nRead);
            }
            out_stream.close();
            Log.d("DOWNLOAD", "Download succeeded");
            return file_name;
        } catch (Exception e) {
            Log.d("DOWNLOAD", "Download failed", e);
            return null;
        }
    }
}
