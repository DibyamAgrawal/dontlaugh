package cse2017.in.ac.nitrkl.dontlaugh;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import cse2017.in.ac.nitrkl.dontlaugh.POJO.Post;

/**
 * Created by me on 11/1/2017.
 */

class CardsAdapter extends CardsCursorAdapter {


    private Context mContext;
    private Cursor mCursor;


    public CardsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_layout, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemViewType(int position) {

        return 0;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        final CustomViewHolder holder = (CustomViewHolder) viewHolder;
        final Post p = Post.fromCursor(cursor);
        Picasso.with(mContext)
                .load(p.getUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(240, 240)
                .centerCrop()
                .into(holder.content
                );


        Picasso.with(mContext).load(p.getUrl()).resize(240, 240).networkPolicy(NetworkPolicy.OFFLINE).centerCrop().into(holder.content, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(mContext)
                        .load(p.getUrl())
                        .resize(240, 240)
                        .centerCrop()
                        .into(holder.content);

            }
        });

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    private class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView content;


        public CustomViewHolder(View v) {
            super(v);

            content = (ImageView) v.findViewById(R.id.content);
        }
    }

}
