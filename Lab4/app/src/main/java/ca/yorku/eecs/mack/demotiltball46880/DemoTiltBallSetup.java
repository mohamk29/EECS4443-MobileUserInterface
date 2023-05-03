package ca.yorku.eecs.mack.demotiltball46880;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.util.Log;

import ca.yorku.eecs.mack.demotiltball.R;

public class DemoTiltBallSetup extends Activity
{
	final static String[] ORDER_OF_CONTROL = { "Velocity", "Position" }; // NOTE: do not change strings
	final static String[] GAIN = { "Very low", "Low", "Medium", "High", "Very high" };
	final static String[] PATH_TYPE = { "Square", "Circle", "Free" };
	final static String[] PATH_WIDTH = { "Narrow", "Medium", "Wide" };

	/**
	 * Create array to hold values for displaying as options for the lap spinner
	 */
	final static String[] LAP_NUMBER = { "1", "2", "3", "4", "5" };

	final static String MYDEBUG = "MYDEBUG";

	// somewhat arbitrary mappings for gain by order of control
	final static int[] GAIN_ARG_POSITION_CONTROL = { 5, 10, 20, 40, 80 };
	final static int[] GAIN_ARG_VELOCITY_CONTROL = { 25, 50, 100, 200, 400 };

	/**
	 * Declare the spinNumberOfLaps variable
	 */
	Spinner spinOrderOfControl, spinGain, spinPathMode, spinPathWidth, spinNumberOfLaps;

	// called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.i(MYDEBUG, "Got here! (DemoTiltBallSetup - onCreate)");

		setContentView(R.layout.setup);

		spinOrderOfControl = (Spinner) findViewById(R.id.paramOrderOfControl);
		ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle, ORDER_OF_CONTROL);
		spinOrderOfControl.setAdapter(adapter2);

		spinGain = (Spinner) findViewById(R.id.paramGain);
		ArrayAdapter<CharSequence> adapter3 = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle, GAIN);
		spinGain.setAdapter(adapter3);
		spinGain.setSelection(2); // "medium" default

		spinPathMode = (Spinner) findViewById(R.id.paramPathType);
		ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle, PATH_TYPE);
		spinPathMode.setAdapter(adapter1);
		spinPathMode.setSelection(0); // free

		spinPathWidth = (Spinner) findViewById(R.id.paramPathWidth);
		ArrayAdapter<CharSequence> adapter4 = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle, PATH_WIDTH);
		spinPathWidth.setAdapter(adapter4);
		spinPathWidth.setSelection(1); // medium

		/**
		 * Connect the spinner from the UI and add sections
		 */
		spinNumberOfLaps = (Spinner) findViewById(R.id.paramNumberOfLaps);
		ArrayAdapter<CharSequence> adapter5 = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle, LAP_NUMBER);
		spinNumberOfLaps.setAdapter(adapter5);
		spinNumberOfLaps.setSelection(0); // 1
	}

	// called when the "OK" button is tapped
	public void clickOK(View view)
	{
		// get user's choices...
		String orderOfControl = (String) spinOrderOfControl.getSelectedItem();

		// actual gain value depends on order of control
		int gain;
		if (orderOfControl.equals("Velocity"))
			gain = GAIN_ARG_VELOCITY_CONTROL[spinGain.getSelectedItemPosition()];
		else
			gain = GAIN_ARG_POSITION_CONTROL[spinGain.getSelectedItemPosition()];

		String pathType = PATH_TYPE[spinPathMode.getSelectedItemPosition()];
		String pathWidth = PATH_WIDTH[spinPathWidth.getSelectedItemPosition()];

		/**
		 * Got the number of laps the user inputs and convert it to string to be used later for calculation
		 */
		int numberOfLaps = Integer.parseInt(spinNumberOfLaps.getSelectedItem().toString());

		// bundle up parameters to pass on to activity
		Bundle b = new Bundle();
		b.putString("orderOfControl", orderOfControl);
		b.putInt("gain", gain);
		b.putString("pathType", pathType);
		b.putString("pathWidth", pathWidth);
		b.putInt("numberOfLaps", numberOfLaps);

		// start experiment activity
		Intent i = new Intent(getApplicationContext(), DemoTiltBall46880Activity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtras(b);
		startActivity(i);

		// comment out (return to setup after clicking BACK in main activity
		finish();
	}

	/** Called when the "Exit" button is pressed. */
	public void clickExit(View view)
	{
		super.onDestroy(); // cleanup
		this.finish(); // terminate
	}
}
