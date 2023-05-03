package ca.yorku.eecs.mack.democamera46880;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import ca.yorku.eecs.mack.democamera.R;

@SuppressWarnings("unused")
public class ImageListViewerActivity extends Activity implements AdapterView.OnItemClickListener
{
	final static String MYDEBUG = "MYDEBUG"; // for Log.i messages

	final static String IMAGE_INDEX_KEY = DemoCamera46880Activity.IMAGE_INDEX_KEY;
	final static String DIRECTORY_KEY = DemoCamera46880Activity.DIRECTORY_KEY;
	final static String IMAGE_FILENAMES_KEY = DemoCamera46880Activity.IMAGE_FILENAMES_KEY;

	/**
	 * declare the setup variable
	 */
	File directory;
	GridView gridView;
	TextView textView;
	ImageAdapter imageAdapter;
	File[] files;
	String[] filenames;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridviewlayout);
	}

	@Override
	protected void onStart() {
		super.onStart();
		init();
	}

	private void init()
	{
		// data passed from the calling activity in startActivityForResult (see DemoCameraIntentActivity)
		Bundle b = getIntent().getExtras();
		String directoryString = b.getString(DIRECTORY_KEY);

		/**
		 * images containing directory is retrieved
		 */
		directory = new File(directoryString);
		if (!directory.exists())
		{
			Log.i(MYDEBUG, "No directory: " + directory.toString());
			super.onDestroy(); // cleanup
			this.finish(); // terminate
		}

		/**
		 * all files in the directory grabbed
		 */
		files = directory.listFiles(new MyFilenameFilter(".jpg"));

		if (files.length == 0) {
			finish();
			return;
		}

		Arrays.sort(files, new Comparator<File>()
		{
			public int compare(File f1, File f2)
			{
				return f1.getName().compareTo(f2.getName());
			}
		});

		/**
		 * created a string array of the filenames
		 */
		filenames = new String[files.length];
		for (int i = 0; i < files.length; ++i) {
			filenames[i] = files[i].getName();
		}

		/**
		 * connect the backend with the UI
		 */
		gridView = (GridView)findViewById(R.id.gridview);
		textView = (TextView)findViewById(R.id.textview);

		/**
		 * output the name of the directory in the text view
		 */
		String[] s = directory.toString().split(File.separator);
		textView.setText(s[s.length - 1]);

		/**
		 * declare the ImageAdapter and pass it the array of filenames and the directory
		 */
		imageAdapter = new ImageAdapter(this);
		imageAdapter.setFilenames(filenames, directory);


		/**
		 * Determine the display width and height.
		 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		/**
		 * Make 3 pixel of space on the left and right and between each column (The "-12" completes that task)
		 */
		int columnWidth = dm.widthPixels < dm.heightPixels ? dm.widthPixels / 3 - 12
				: dm.heightPixels / 3 - 12;
		imageAdapter.setColumnWidth(columnWidth);
		gridView.setColumnWidth(columnWidth);


		/**
		 * pass imageAdapter to the GridView and load the images
		 */
		gridView.setAdapter(imageAdapter);

		/**
		 * set a click listener to the gridview to react to the taps by the user
		 */
		gridView.setOnItemClickListener(this);
	}


	/**
	 *Handles the user taps on the image in the GridView
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	{
		/**
		 * bundle the filenames array and directory to pass to the new activity
		 */
		final Bundle b = new Bundle();
		b.putStringArray("imageFilenames", filenames);
		b.putString("directory", directory.toString());
		b.putInt("position", position);

		/**
		 * intentiate the new image viewer activity
		 */
		Intent i = new Intent(getApplicationContext(), ImageViewerActivity.class);
		i.putExtras(b);
		startActivity(i);
	}

	/**
	 * filter class serves by returning only files with a specified extension .JPG
	 */
	class MyFilenameFilter implements FilenameFilter
	{
		String extension;

		MyFilenameFilter(String extensionArg)
		{
			this.extension = extensionArg;
		}

		@SuppressLint("DefaultLocale")
		public boolean accept(File f, String name)
		{
			/**
			 * handles accepting ".jpg" or ".JPG"
			 */
			return name.toLowerCase().endsWith(extension);
		}
	}

	@Override
	public void onBackPressed()
	{
		this.setResult(Activity.RESULT_OK);
		super.onBackPressed();
	}
}
