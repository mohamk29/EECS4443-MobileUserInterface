package ca.yorku.eecs.mack.demoscale46880;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ca.yorku.eecs.mack.demoscale.R;

/**
 * Demo_Buttons - with modifications by...
 *
 * Login ID - mohamk29
 * Student ID - 216046880
 * Last name - Khan
 * First name(s) - Mohammad
 */

public class DemoScale46880Activity extends Activity
{
    PaintPanel imagePanel; // the panel in which to paint the image
    StatusPanel statusPanel; // a status panel the display the image coordinates, size, and scale

    /**
     * Additional Feature: Added Button to UI to Display the Building Name
     * Created the necessary variable (button, TextView)
     **/
    Button b;
    TextView buttonClickStatus;
    /**
     * Variable to hold the Building Name
     **/
    String buttonClickString;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title bar
        setContentView(R.layout.main);
        // get references to UI components
        // cast removed (not needed anymore, avoids warning message)
        imagePanel = findViewById(R.id.paintpanel);
        statusPanel = findViewById(R.id.statuspanel);

        /**
         * Linked the button and the textView
         */
        b = (Button) findViewById(R.id.buildingName);
        buttonClickStatus = (TextView) findViewById(R.id.buttonclickstatus);

        /**
         * Stored the building name inside the variable
         */
        buttonClickString = "New Student Centre";

        // give the image panel a reference to the status panel
        imagePanel.setStatusPanel(statusPanel);
    }

    // Called when the "Reset" button is pressed.
    public void clickReset(View view)
    {
        imagePanel.xPosition = 10;
        imagePanel.yPosition = 10;
        imagePanel.scaleFactor = 1f;
        imagePanel.invalidate();
    }


    /**
     * Called when the "Building Name" button is pressed
     * @param v: The button we will use to activate the onClickListener
     */
    public void clickBuildingName(View v){
        if(v == b){
            buttonClickStatus.setText(buttonClickString);
        }
    }
}