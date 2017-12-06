package cse2017.in.ac.nitrkl.dontlaugh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

import static java.security.AccessController.getContext;

/**
 * Created by LENOVO on 30-10-2017.
 */

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    private final int[] backColor;
    private final int[] textColor;
    private final int[] gifId;

    public CustomGrid(Context c,String[] web,int[] backColor,int[] textColor ,int[] gifId ) {
        mContext = c;
        this.backColor = backColor;
        this.textColor = textColor;
        this.gifId = gifId;
        this.web = web;
    }
    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.gridsingle, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
//            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            RelativeLayout relativeLayout= (RelativeLayout) grid.findViewById(R.id.grid_single);
            GifImageView gifImageView = (GifImageView) grid.findViewById(R.id.grid_gif);

            textView.setText(web[position]);
//            linearLayout.setBackgroundResource(Imageid[position]);
            relativeLayout.setBackgroundColor(backColor[position]);
            textView.setTextColor(Color.WHITE);
            gifImageView.setImageResource(gifId[position]);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

//            relativeLayout.setMinimumHeight((int)(height*.9/3));
//            gifImageView.setMaxHeight((int)(height*.9/3));
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
