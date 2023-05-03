package ca.yorku.eecs.mack.democamera46880;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import ca.yorku.eecs.mack.democamera.R;

public class ImageViewerActivity extends Activity implements OnTouchListener{
    /**
     * stores the JPG file
     */
    ImageView imageView;
    TextView textView;
    /**
     * the parent view used to hold the image view
     */
    RelativeLayout container;
    /**
     * class used to get the images
     */
    ImageDownloader imageDownloader;
    /**
     * directory which contains the images
     */
    String directory;
    /**
     * array of all JPG files in the directory
     */
    String[] filenames;


    float positionX;
    float positionY;
    float xRatio, yRatio;
    float scaleFactor, lastScaleFactor;
    /**
     * used to keep track of the current image
     */
    int index;
    int widthDisplay, heightDisplay;


    /**
     * used to move the image while keeping track (Active pointer)
     */
    private static final int POINTER_ID_INVALID = -1;
    private int activePointerId = POINTER_ID_INVALID;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private boolean isZoomMode;
    private float lastTouchX;
    private float lastTouchY;
    /**
     * option menu items (to send the image or to delete the iamge)
     */
    final static int SEND = 0;
    final static int DELETE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);
        container = (RelativeLayout)findViewById(R.id.imagecontainer);
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        /**
         * set a click listener to make image respond to touch events upon user touch
         */
        container.setOnTouchListener(this);

        /**
         * pass the data from startActivity
         */
        Bundle bundle = getIntent().getExtras();
        filenames = bundle.getStringArray("imageFilenames");
        directory = bundle.getString("directory");
        index = bundle.getInt("position");

        /**
         * handles the scaling and transformation to intantiate the app
         */
        setDefaults();

        /**
         * Create the gesture detectors for scaling, flinging, double tapping
         */
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        /**
         * determines the scalable width and height for the image
         */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthDisplay = dm.widthPixels;
        heightDisplay = widthDisplay;

        imageView.setMaxWidth(widthDisplay);
        imageView.setMaxHeight(heightDisplay);

        /**
         * instantiate the image downloader
         */
        imageDownloader = new ImageDownloader();
    }

    /**
     * display the image starting with the first image
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus)

    {
        displayImage(index);
    }

    /**
     * go back to the previous image
     */
    private void goToPreviousImage()
    {
        --index;
        if (index < 0)
            index = filenames.length - 1;
        displayImage(index);
    }

    /**
     * go to the next image
     */
    private void goToNextImage()
    {
        ++index;
        if (index >= filenames.length)
            index = 0;
        displayImage(index);
    }

    /**
     *displays specific image at the index position specified in the filenames array
     */
    private void displayImage(int idx)
    {
        /**
         * creates the path within the android device storage
         */
        String path = directory + File.separator + filenames[idx];

        /**
         * image is downloaded
         */
        imageDownloader.download(path, imageView, widthDisplay);

        /**
         * retrieve file size in KB
         */
        File f = new File(path);
        long kiloBytes = f.length() / 1024;

        /**
         * retrieve image dimensions
         */
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try
        {
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e)
        {
            Log.i("MYDEBUG", "FileNotFoundException e=" + e.toString());
        }

        /**
         * output the image file position, count, size and dimensions
         */
        String s = String.format(Locale.CANADA, "%s (%d of %d, %d KB, %d x %d)", filenames[idx], (idx + 1),
                filenames.length, kiloBytes, o.outWidth, o.outHeight);
        textView.setText(s);

        setDefaults();
        /**
         * fade in the image
         */
        imageView.animate().alpha(1f);
        imageView.invalidate();
    }

    /**
     * initiate the default values to start the application
     */
    private void setDefaults()
    {
        imageView.setScaleX(scaleFactor);
        imageView.setScaleY(scaleFactor);
        imageView.setTranslationX(0);
        imageView.setTranslationY(0);
        imageView.setAlpha(0f);
        isZoomMode = false;
        scaleFactor = 1f;
        lastScaleFactor = 1f;
        positionX = 0;
        positionY = 0;
        xRatio = 0.5f;
        yRatio = 0.5f;
    }


    /**
     * maintains image on device as device orientaiton changes
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("position", index);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * maintains image on device as device orientaiton changes
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt("position");
        displayImage(index);
    }

    @Override
    public boolean onTouch(View v, MotionEvent me)
    {
        /**
         * check for any touch or other gestures by the user
         */
        scaleGestureDetector.onTouchEvent(me);

        /**
         * check if it is a double tap
         */
        gestureDetector.onTouchEvent(me);

        final int action = me.getAction();
        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                /**
                 * retrieve the users touch coordinates on the screen
                 */
                final float x = me.getX();
                final float y = me.getY();

                /**
                 * id of the pointer is retrieved and saved
                 */
                activePointerId = me.getPointerId(0);

                lastTouchX = x;
                lastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                if (activePointerId != POINTER_ID_INVALID)
                {
                    /**
                     * search for the active pointer index and retrieve position
                     */
                    final int pointerIndex = me.findPointerIndex(activePointerId);
                    final float x = me.getX(pointerIndex);
                    final float y = me.getY(pointerIndex);

                    /**
                     * if scaleGestureDetector isn't processing a gesture then move
                     */
                    if (!scaleGestureDetector.isInProgress())
                    {
                        /**
                         * calculate the position of the image
                         */
                        final float dx = x - lastTouchX;
                        final float dy = y - lastTouchY;

                        /**
                         * apply the position horizontal delta
                         */
                        positionX += dx;
                        if (isZoomMode)
                            positionY += dy;

                        /**
                         * give the position deltas to the image instance
                         */
                        imageView.setTranslationX(positionX);
                        imageView.setTranslationY(positionY);

                        imageView.invalidate();
                    }

                    /**
                     * store the coordinates of the last touch done by the user
                     */
                    lastTouchX = x;
                    lastTouchY = y;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                /**
                 * remove the pointer id
                 */
                activePointerId = POINTER_ID_INVALID;
                if (!isZoomMode)
                {
                    positionX = 0f;
                    positionY = 0f;
                    scaleFactor = 1f;
                    lastScaleFactor = 1f;
                    imageView.animate().scaleX(scaleFactor).scaleY(scaleFactor).translationX(positionX).translationY(
                            positionY);
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            {
                activePointerId = POINTER_ID_INVALID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                /**
                 * retain the index of the pointer that left the touch sensor
                 */
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = me.getPointerId(pointerIndex);
                if (pointerId == activePointerId)
                {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = me.getX(newPointerIndex);
                    lastTouchY = me.getY(newPointerIndex);
                    activePointerId = me.getPointerId(newPointerIndex);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:
            {
                break;
            }
        }
        return true;
    }


    /**
     *Returns rectangle bounds of the view as it is rendered on the screen.
     */
    public Rect getViewRectangle(ImageView view)
    {
        Rect r = new Rect();
        final float pivotX = view.getPivotX();
        final float pivotY = view.getPivotY();
        final float scaleX = view.getScaleX();
        final float scaleY = view.getScaleY();
        final float left = view.getLeft();
        final float top = view.getTop();
        final float right = view.getRight();
        final float bottom = view.getBottom();
        final float translationX = view.getTranslationX();
        final float translationY = view.getTranslationY();
        r.left = (int)(pivotX - (pivotX - left) * scaleX + translationX + 0.5f);
        r.top = (int)(pivotY - (pivotY - top) * scaleY + translationY + 0.5f);
        r.right = (int)(pivotX + (right - pivotX) * scaleX + translationX + 0.5f);
        r.bottom = (int)(pivotY + (bottom - pivotY) * scaleY + translationY + 0.5f);
        return r;
    }

    /**
     * detector for two-finger scale gestures
     */
    private class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        float lastFocusX, lastFocusY;

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            isZoomMode = true;
            scaleFactor *= detector.getScaleFactor();

            /**
             * scale object appropriately
             */
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 40.0f));

            /**
             * start to scale image
             */
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);

            /**
             * store the change in scale since last execution of onScale
             */
            float scaleFactorChange = scaleFactor - lastScaleFactor;
            lastScaleFactor = scaleFactor;

            /**
             * calculate the pixel change in size of view based on scaling
             */
            float pixelChangeX = scaleFactorChange * imageView.getWidth();
            float pixelChangeY = scaleFactorChange * imageView.getHeight();


            /**
             * upon focus point migration move the image equivelent
             */
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            float dx = focusX - lastFocusX;
            float dy = focusY - lastFocusY;
            lastFocusX = focusX;
            lastFocusY = focusY;
            positionX += dx;
            positionY += dy;


            /**
             * ensure scaling appears centered on the focus point
             */
            float focusAdjustX = pixelChangeX * (0.5f - xRatio);
            float focusAdjustY = pixelChangeY * (0.5f - yRatio);
            positionX += focusAdjustX;
            positionY += focusAdjustY;

            /**
             * move the image
             */
            imageView.setTranslationX(positionX);
            imageView.setTranslationY(positionY);

            imageView.invalidate();
            return true;
        }


        /**
         *Compute xRatio and yRatio. xRatio is the x offset of the focus point (relative to the
         *left edge of the view) divided by the width of the view. yRatio is the y offset of the
         *focus point (relative to the top of the view) divided by the height of the view.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            lastFocusX = focusX;
            lastFocusY = focusY;

            /**
             * get rectangle that surrounds the view
             */
            Rect r = getViewRectangle(imageView);

            /**
             * compute x and y ratios used to scale
             */
            xRatio = (focusX - r.left) / (r.right - r.left);
            yRatio = (focusY - r.top) / (r.bottom - r.top);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector)
        {
        }
    }


    /**
     * handles the user interaction for fling or double tap gestures
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
        {
            Log.i("MYDEBUG", "Got here: onFling");
            /**
             * move to the right
             */
            if (velocityX > 3000)
            {
                goToPreviousImage();
            }
            /**
             * move to the left
             */
            else if (velocityX < -3000)
            {
                goToNextImage();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent me)
        {
            /**
             * move between normal and big
             */
            if (isZoomMode)
            {
                scaleFactor = 1f;
                lastScaleFactor = 1f;
            } else
            {
                scaleFactor = 3f;
                lastScaleFactor = 3f;
            }

            /**
             * return image to normal positon
             */
            positionX = 0f;
            positionY = 0f;

            imageView.animate().scaleX(scaleFactor).scaleY(scaleFactor).translationX(positionX).translationY(positionY);

            /**
             * toggle the zoom
             */
            isZoomMode = !isZoomMode;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent me)
        {
            Log.i("MYDEBUG", "Got here: onLongPress");
        }
    }

    /**
     *set the option menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.add(SEND, SEND, SEND, R.string.menu_send);
        menu.add(DELETE, DELETE, DELETE, R.string.menu_delete);
        return true;
    }

    /**
     * sends the image to another user via email
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri screenshotUri = Uri.parse("file://" + directory + File.separator + filenames[index]);
        switch (item.getItemId()) {
            case DELETE:
                File fdelete = new File(screenshotUri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Log.i("DEBUG-DELETE", "file Deleted :" + screenshotUri.getPath());
                    } else {
                        Log.i("DEBUG-DELETE", "file not Deleted :" + screenshotUri.getPath());
                    }
                }
                finish();
                return true;
            case SEND:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Photo");
                emailIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                emailIntent.setType("image/png");
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
