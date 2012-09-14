package edu.vanderbilt.binaryclock;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
//import android.widget.EditText;

public class BinaryClockActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       	Timer timer = new Timer();
       	TimerTask updateTask;
       	final Handler handler = new Handler();
       	updateTask = new TimerTask() {
       		public void run() {
       			handler.post(new Runnable() {
       				public void run() {
       					updateTime();
       				}
       			});
       		}
       	};
		timer.schedule(updateTask, 0, 1000);
    }
    
    public int toBinary(int dec) {
    	int bin = 0, mult = 1;
    	while (dec > 0) {
    		if (dec%2 == 1) {
    			bin += mult;
    		}
    		dec /= 2;
    		mult *= 10;
    	}
    	return bin;
    }

	public void updateTime() {
		/*EditText hourText = (EditText) findViewById(R.id.hourText);
		EditText minuteText = (EditText) findViewById(R.id.minuteText);
		EditText secondText = (EditText) findViewById(R.id.secondText);
		hourText.setText(String.valueOf(binaryHours));
		minuteText.setText(String.valueOf(binaryMinutes));
		secondText.setText(String.valueOf(binarySeconds));
		*/

		Date time = new Date();
		int binaryHours = toBinary(time.getHours());
		int binaryMinutes = toBinary(time.getMinutes());
		int binarySeconds = toBinary(time.getSeconds());
		
		Resources res = getResources();
       	Drawable green_bright = res.getDrawable(R.drawable.green_bright);
       	Drawable blue_bright = res.getDrawable(R.drawable.blue_bright);
       	Drawable orange_bright = res.getDrawable(R.drawable.orange_bright);
       	Drawable green_dark = res.getDrawable(R.drawable.green_dark);
       	Drawable blue_dark = res.getDrawable(R.drawable.blue_dark);
       	Drawable orange_dark = res.getDrawable(R.drawable.orange_dark);
       	ImageView hour_0 = (ImageView) findViewById(R.id.hour_0);
       	ImageView hour_1 = (ImageView) findViewById(R.id.hour_1);
       	ImageView hour_2 = (ImageView) findViewById(R.id.hour_2);
       	ImageView hour_3 = (ImageView) findViewById(R.id.hour_3);
       	ImageView hour_4 = (ImageView) findViewById(R.id.hour_4);
       	ImageView minute_0 = (ImageView) findViewById(R.id.minute_0);
       	ImageView minute_1 = (ImageView) findViewById(R.id.minute_1);
       	ImageView minute_2 = (ImageView) findViewById(R.id.minute_2);
       	ImageView minute_3 = (ImageView) findViewById(R.id.minute_3);
       	ImageView minute_4 = (ImageView) findViewById(R.id.minute_4);
       	ImageView minute_5 = (ImageView) findViewById(R.id.minute_5);
       	ImageView second_0 = (ImageView) findViewById(R.id.second_0);
       	ImageView second_1 = (ImageView) findViewById(R.id.second_1);
       	ImageView second_2 = (ImageView) findViewById(R.id.second_2);
       	ImageView second_3 = (ImageView) findViewById(R.id.second_3);
       	ImageView second_4 = (ImageView) findViewById(R.id.second_4);
       	ImageView second_5 = (ImageView) findViewById(R.id.second_5);
       	
    	light(binaryHours, hour_0, green_bright, green_dark);
    	binaryHours /= 10;
       	light(binaryHours, hour_1, green_bright, green_dark);
       	binaryHours /= 10;
       	light(binaryHours, hour_2, green_bright, green_dark);
       	binaryHours /= 10;
       	light(binaryHours, hour_3, green_bright, green_dark);
       	binaryHours /= 10;
       	light(binaryHours, hour_4, green_bright, green_dark);
       	binaryHours /= 10;

       	light(binaryMinutes, minute_0, blue_bright, blue_dark);
       	binaryMinutes /= 10;
       	light(binaryMinutes, minute_1, blue_bright, blue_dark);
       	binaryMinutes /= 10;
       	light(binaryMinutes, minute_2, blue_bright, blue_dark);
       	binaryMinutes /= 10;
       	light(binaryMinutes, minute_3, blue_bright, blue_dark);
       	binaryMinutes /= 10;
       	light(binaryMinutes, minute_4, blue_bright, blue_dark);
       	binaryMinutes /= 10;
       	light(binaryMinutes, minute_5, blue_bright, blue_dark);
       	binaryMinutes /= 10;

       	light(binarySeconds, second_0, orange_bright, orange_dark);
       	binarySeconds /= 10;
       	light(binarySeconds, second_1, orange_bright, orange_dark);
       	binarySeconds /= 10;
       	light(binarySeconds, second_2, orange_bright, orange_dark);
       	binarySeconds /= 10;
       	light(binarySeconds, second_3, orange_bright, orange_dark);
       	binarySeconds /= 10;
       	light(binarySeconds, second_4, orange_bright, orange_dark);
       	binarySeconds /= 10;
       	light(binarySeconds, second_5, orange_bright, orange_dark);
       	binarySeconds /= 10;
	}
	
	private void light(int binaryTime, ImageView bulb, Drawable color_bright, Drawable color_dark) {
		if (binaryTime%10 == 1) {
			bulb.setImageDrawable(color_bright);
		} else {
			bulb.setImageDrawable(color_dark);
		}
	}
}
