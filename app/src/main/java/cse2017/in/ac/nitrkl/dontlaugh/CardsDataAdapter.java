package cse2017.in.ac.nitrkl.dontlaugh;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        v.setBackgroundResource(getItem(position));
        return contentView;
    }

}