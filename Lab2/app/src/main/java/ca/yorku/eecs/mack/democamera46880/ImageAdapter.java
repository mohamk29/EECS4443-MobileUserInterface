package ca.yorku.eecs.mack.democamera46880;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

class ImageAdapter extends BaseAdapter
{
    String[] filenames;
    File directory;
    int columnWidth;
    private Context context;

    ImageDownloader imageDownloader = new ImageDownloader();

    public ImageAdapter(Context c)
    {
        context = c;
    }

    public int getCount()
    {
        return filenames.length;
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * Create a new imageView upon new refrence item request
     */
    public View getView(int position, View view, ViewGroup parent)
    {
        ImageView imageView;
        if (view == null)
        {
            /**
             * complete downloading of image and combine with the ImageView
             */
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(columnWidth, columnWidth));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else
        {
            imageView = (ImageView)view;
        }

        /**
         * complete downloading of image and combine with the ImageView
         */
        String path = directory + File.separator + filenames[position];
        imageDownloader.download(path, imageView, 250);
        return imageView;
    }

    /**
     *  provide the imageAdapter with filenames array and directory
     */
    public void setFilenames(String[] filenamesArg, File directoryArg)
    {
        filenames = filenamesArg;
        directory = directoryArg;
    }

    public void setColumnWidth(int widthArg)
    {
        columnWidth = widthArg;
    }
}
