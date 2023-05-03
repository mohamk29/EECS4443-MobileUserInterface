package ca.yorku.eecs.mack.demoandroid46880;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

import ca.yorku.eecs.mack.demoandroid.R;

/**
 * Demo_Android - with modifications by...
 *
 * Login ID - mohamk29
 * Student ID - 216046880
 * Last name - Khan
 * First name(s) - Mohammad
 */

public class DemoAndroid46880Activity extends Activity implements OnClickListener {
    private final static String MYDEBUG = "MYDEBUG"; // for Log.i messages

    /**
     * Declared the resetButton
     */
    private Button incrementButton, decrementButton, exitButton, resetButton;
    private TextView textview;
    private int clickCount;

    // called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialize();
        Log.i(MYDEBUG, "Initialization done. Application running.");
    }

    private void initialize() {
        // get references to buttons and text view from the layout manager (rather than instantiate them)
        incrementButton = (Button) findViewById(R.id.incbutton);
        decrementButton = (Button) findViewById(R.id.decbutton);
        exitButton = (Button) findViewById(R.id.exitbutton);
        /**
         * Created the reset button
         */
        resetButton = (Button) findViewById(R.id.resetbutton);
        textview = (TextView) findViewById(R.id.textview);

        // some code is missing here

        /**
         * @author Mohammad Khan
         * Call the listener event on each button which listens for a click, the "this" keyword is used
         * to attach the buttons and will call the onClick method
         */

        incrementButton.setOnClickListener(this);
        decrementButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);


        // initialize the click count
        clickCount = 0;

        // initialize the text field with the click count
        textview.setText(String.format(Locale.CANADA, "Count: %d", clickCount));
    }

    // this code executes when a button is clicked (i.e., tapped with user's finger)
    @Override
    public void onClick(View v) {
        if (v == incrementButton) {
            Log.i(MYDEBUG, "Increment button clicked!");
            ++clickCount;

        } else if (v == decrementButton) {
            Log.i(MYDEBUG, "Decrement button clicked!");
            --clickCount;

        } else if (v == exitButton) {
            Log.i(MYDEBUG, "Good bye!");
            this.finish();

            /**
             * added a if statement to handle the functionality in the console
             * once resetButton is clicked as well as setting the count back to zero
             */
        }else if (v == resetButton){
            Log.i(MYDEBUG, "Reset button clicked!");
            clickCount = 0;

        } else
            Log.i(MYDEBUG, "Oops: Invalid Click Event!");

        // update click count
        textview.setText(String.format(Locale.CANADA, "Count: %d", clickCount));
    }
}