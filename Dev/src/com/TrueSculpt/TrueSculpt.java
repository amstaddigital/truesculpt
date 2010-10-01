package com.TrueSculpt;

import com.TrueSculpt.ColorPickerDialog;
import com.TrueSculpt.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


@SuppressWarnings("deprecation")
public class TrueSculpt extends Activity implements OnColorChangedListener, SensorListener {

	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);        

		text = (TextView) findViewById(R.id.text);
		mSensorManager = (Sensor) getSystemService(SENSOR_SERVICE);

		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new ColorPickerDialog(TrueSculpt.this, TrueSculpt.this, 0).show();
			}
		});
		
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);//new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new CubeRenderer(false));
	}

	public void colorChanged(int color) {
		String msg="color is" + color;
		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(TrueSculpt.this, 
				SensorManager.SENSOR_ALL ,
				SensorManager.SENSOR_DELAY_FASTEST);
		 mGLSurfaceView.onResume();
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(TrueSculpt.this);
		super.onStop();	
	}
	
    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }
	
	private GLSurfaceView mGLSurfaceView;
	private Sensor mSensorManager;
	//Temp for test debug
	private String[] sensorvalues=new String[10];
	private DecimalFormat twoPlaces = new DecimalFormat("000.00");
	private TextView text;
	private String msg;
	private String fullmsg;

	public void onSensorChanged(int sensor, float[] values) {        
		synchronized (this) {        	 
			
			msg= 
				"sensor: " + sensor + 
				", x: " + twoPlaces.format(values[0]) +
				", y: " +  twoPlaces.format(values[1]) +
				", z: " +  twoPlaces.format(values[2]);        	 
			sensorvalues[sensor]=msg;

			fullmsg="";
			int n=sensorvalues.length;
			for (int i=0;i<n;i++)
			{
				fullmsg+=sensorvalues[i]+"\n";
			}
			
			text.setText(fullmsg);           
		}
	}

	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}