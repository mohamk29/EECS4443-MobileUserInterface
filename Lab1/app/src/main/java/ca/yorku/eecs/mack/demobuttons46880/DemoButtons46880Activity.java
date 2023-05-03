package ca.yorku.eecs.mack.demobuttons46880;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import ca.yorku.eecs.mack.demobuttons.R;

/**
 * Demo_Buttons - with modifications by...
 *
 * Login ID - mohamk29
 * Student ID - 216046880
 * Last name - Khan
 * First name(s) - Mohammad
 */


@SuppressWarnings("unused")
public class DemoButtons46880Activity extends Activity {
    private final static String MYDEBUG = "MYDEBUG"; // for Log.i messages


    //Created the hello button and helloButtonText
    Button b, helloButton;
    CheckBox cb;
    RadioButton rb1, rb2, rb3;
    ToggleButton tb;
    ImageButton backspaceButton;
    TextView buttonClickStatus, checkBoxClickStatus, radioButtonClickStatus, toggleButtonClickStatus,
            backspaceButtonClickStatus, helloButtonText;

    //Created variable to hold "hello" string
    String buttonClickString, backspaceString, helloButtonClickString;
    boolean checkStatus;
    boolean rb1Status, rb2Status, rb3Status;
    boolean tbStatus;

    // called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        b = (Button) findViewById(R.id.button);;
        cb = (CheckBox) findViewById(R.id.checkbox);
        rb1 = (RadioButton) findViewById(R.id.radiobutton1);
        rb2 = (RadioButton) findViewById(R.id.radiobutton2);
        rb3 = (RadioButton) findViewById(R.id.radiobutton3);
        rb1.toggle();
        tb = (ToggleButton) findViewById(R.id.togglebutton);
        backspaceButton = (ImageButton) findViewById(R.id.backspacebutton);

        //Created the hello button
        helloButton = (Button) findViewById(R.id.helloButton);


        buttonClickStatus = (TextView) findViewById(R.id.buttonclickstatus);
        checkBoxClickStatus = (TextView) findViewById(R.id.checkboxclickstatus);
        radioButtonClickStatus = (TextView) findViewById(R.id.radiobuttonclickstatus);
        toggleButtonClickStatus = (TextView) findViewById(R.id.togglebuttonclickstatus);
        backspaceButtonClickStatus = (TextView) findViewById(R.id.backspacebuttonclickstatus);
        helloButtonText = (TextView) findViewById(R.id.helloButtonText);

        buttonClickString = "";
        backspaceString = "";
        //initialized the variable to hold the "hello" text
        helloButtonClickString = "";

        buttonClickStatus.setText(buttonClickString);
        checkBoxClickStatus.setText(R.string.unchecked);
        radioButtonClickStatus.setText(R.string.red);
        radioButtonClickStatus.setTextColor(Color.RED);
        toggleButtonClickStatus.setText(R.string.off);

        //setting it to empty string to handle functionality in the method bellow
        helloButtonText.setText(helloButtonClickString);
    }

    // handle button clicks
    public void buttonClick(View v) {
        // plain button
        if (v == b) {
            buttonClickString += ".";
            buttonClickStatus.setText(buttonClickString);
        }

        // checkbox
        else if (v == cb) {
            if (cb.isChecked()) {
                cb.setChecked(true);
                checkBoxClickStatus.setText(R.string.checked);
            } else {
                cb.setChecked(false);
                checkBoxClickStatus.setText(R.string.unchecked);
            }
        }

        // radio button #1 (RED)
        else if (v == rb1) {
            rb1.setChecked(true);
            radioButtonClickStatus.setText(R.string.red);
            radioButtonClickStatus.setTextColor(Color.RED);
        }

        // radio button #2 (GREEN)
        else if (v == rb2) {
            rb2.setChecked(true);
            radioButtonClickStatus.setText(R.string.green);
            radioButtonClickStatus.setTextColor(Color.GREEN);
        }

        // radio button #3 (BLUE)
        else if (v == rb3) {
            rb3.setChecked(true);
            radioButtonClickStatus.setText(R.string.blue);
            radioButtonClickStatus.setTextColor(Color.BLUE);
        }

        // toggle button
        else if (v == tb) {
            tb.setActivated(tb.isChecked());
            if (tb.isChecked())
                toggleButtonClickStatus.setText(R.string.on);
            else
                toggleButtonClickStatus.setText(R.string.off);
        }

        // backspace button
        else if (v == backspaceButton) {
            backspaceString += "BK ";
            backspaceButtonClickStatus.setText(backspaceString);
        }

        // upon clicking the hello button add the hello string and set it to the TextField
        else if(v == helloButton){
            helloButtonClickString += "Hello! ";
            helloButtonText.setText(helloButtonClickString);
        }

    }



}