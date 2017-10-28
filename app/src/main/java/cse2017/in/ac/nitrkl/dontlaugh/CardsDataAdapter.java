package cse2017.in.ac.nitrkl.dontlaugh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dibya on 22-10-2017.
 */

public class CardsDataAdapter extends ArrayAdapter<Integer> {

    public CardsDataAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        ImageView v = (ImageView)contentView.findViewById(R.id.content);
        //v.setBackgroundResource(getItem(position));

        Drawable drawable = contentView.getResources().getDrawable(getItem(position));
        v.setImageDrawable(drawable);

/*
        v.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };
                if (i == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (i == 2) {
                    //Double click
                    i = 0;
                    Toast toast;
                    toast = Toast.makeText(getContext(),"Start",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
*/
        return contentView;
    }

}