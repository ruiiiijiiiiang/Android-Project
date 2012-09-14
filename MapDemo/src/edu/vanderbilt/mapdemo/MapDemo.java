package edu.vanderbilt.mapdemo;

import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//this class extends from LifecycleLoggingActivity so all life cycle information is logged
public class MapDemo extends LifecycleLoggingActivity {
    
    //declare EditText objects so they can be used in all methods of the class
    private static EditText lat_box;
    private static EditText lon_box;
    private static double lat;
    private static double lon;
    private static final int MAX_LAT = 90;
    private static final int MIN_LAT = -90;
    private static final int MAX_LON = 180;
    private static final int MIN_LON = -180;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_demo);
        
        //associate the EditTexts with GUI objects
        lat_box = (EditText) findViewById(R.id.lat_box);
        lon_box = (EditText) findViewById(R.id.lon_box);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_demo, menu);
        return true;
    }
    
    //this function is set as the onclick property for the button in the xml file
    public void showLocation(View view) {
        lat = Double.parseDouble(lat_box.getText().toString());
        lon = Double.parseDouble(lon_box.getText().toString());
        if (lat > MAX_LAT || lat < MIN_LAT || lon > MAX_LON || lon < MIN_LON) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter valid values for coordinates.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            try {
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:" + Double.toString(lat) + "," + Double.toString(lon))));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=" + lat + "," + lon)));
            }
        }
    }
}