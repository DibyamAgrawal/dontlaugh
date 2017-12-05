package cse2017.in.ac.nitrkl.dontlaugh;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by me on 11/1/2017.
 */

public class DontLaugh extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        try{

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);

        }
        catch (Exception e){

        }

    }
}
