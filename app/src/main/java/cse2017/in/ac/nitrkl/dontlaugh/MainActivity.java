package cse2017.in.ac.nitrkl.dontlaugh;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.wenchao.cardstack.CardStack;

public class MainActivity extends AppCompatActivity{
    GridView grid;
    String[] web = {
            "My Feed",
            "Unread",
            "Trending",
            "Starred",
            "Shared",
            "All"
    } ;
    int[] imageId = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCardStack = (CardStack) findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_layout);
        mCardStack.setStackMargin(20);

        mCardAdapter = new CardsDataAdapter(getApplicationContext(), 0);
        mCardAdapter.add(R.mipmap.ic_launcher);
        mCardAdapter.add(R.mipmap.ic_launcher);
        mCardAdapter.add(R.mipmap.ic_launcher);
        mCardAdapter.add(R.mipmap.ic_launcher);
        mCardAdapter.add(R.mipmap.ic_launcher);



        toast = Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT);
        toast.show();

        mCardStack.setListener(new CardStack.CardEventListener() {


            @Override
            public boolean swipeEnd(int direction, float distance) {
                //if "return true" the dismiss animation will be triggered
                //if false, the card will move back to stack
                //distance is finger swipe distance in dp

                //the direction indicate swipe direction
                //there are four directions
                //  0  |  1
                // ----------
                //  2  |  3
                toast.cancel();
                toast = Toast.makeText(getApplicationContext(),"swipeEnd",Toast.LENGTH_SHORT);
                toast.show();

                return (distance > 300) ? true : false;
            }

            @Override
            public boolean swipeStart(int direction, float distance) {
                toast.cancel();
                toast = Toast.makeText(getApplicationContext(),"swipeStart",Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }

            @Override
            public boolean swipeContinue(int direction, float distanceX, float distanceY) {
                toast.cancel();
                toast = Toast.makeText(getApplicationContext(),"swipeCont",Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }

            @Override
            public void discarded(int id, int direction) {
                //this callback invoked when dismiss animation is finished.
                toast.cancel();
                toast = Toast.makeText(getApplicationContext(),"discarded"+id,Toast.LENGTH_SHORT);
                toast.show();
            }
            final int[] i = {0};
            @Override
            public void topCardTapped() {
                // TODO Auto-generated method stub
                i[0]++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        i[0] = 0;
                    }
                };
                if (i[0] == 1) {
                    //Single click
                    handler.postDelayed(r, 500);
                    toast.cancel();
                    toast = Toast.makeText(getApplicationContext(),"single click",Toast.LENGTH_SHORT);
                    toast.show();
                } else if (i[0] == 2) {
                    //Double click
                    i[0] = 0;
                    toast.cancel();
                    toast = Toast.makeText(getApplicationContext(),"double click",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        mCardStack.setAdapter(mCardAdapter);
        //toast = Toast.makeText(this,"Start",Toast.LENGTH_SHORT);
        //toast.show();

        CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePost(View v){



        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = Uri.parse("android.resource://cse2017.in.ac.nitrkl.dontlaugh/drawable/"+R.drawable.asdf);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Attached Image");



        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        //startActivity(sharingIntent);
    }

}
