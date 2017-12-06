package cse2017.in.ac.nitrkl.dontlaugh;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse2017.in.ac.nitrkl.dontlaugh.POJO.Post;
import cse2017.in.ac.nitrkl.dontlaugh.SQLite.DontLaughContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    GridView grid;
    String[] web = {
            "My Feed",
            "Unread",
            "Trending",
            "Starred",
            "Shared",
            "Broadcast"
    };
    int[] gifId = {
            R.drawable.myfeed,
            R.drawable.unread,
            R.drawable.trending,
            R.drawable.starred,
            R.drawable.shared1,
            R.drawable.broadcast1,
    };
    int[] backColor = {
            Color.parseColor("#C2AE7C"),
            Color.parseColor("#798E85"),
            Color.parseColor("#61AD54"),
            Color.parseColor("#EDAB21"),
            Color.parseColor("#EBDBCB"),
            Color.parseColor("#8CABA3")
    };
    int[] textColor = {
            Color.parseColor("#B25B42"),
            Color.parseColor("#C2B775"),
            Color.parseColor("#886236"),
            Color.parseColor("#223823"),
            Color.parseColor("#DB85AA"),
            Color.parseColor("#6B6E5E")
    };
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    //private CardStack mCardStack;
    private RecyclerView mCardStack;

    private CardsAdapter mCardAdapter;
    Toast toast;

    //firebase
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        /*mCardStack = (CardStack) findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_layout);
        mCardStack.setStackMargin(20);
        */

        mCardStack = (RecyclerView) findViewById(R.id.container);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mCardStack.setLayoutManager(manager);
        mCardStack.setHasFixedSize(true);


        //int top = mCardStack.getTop();
        getSupportLoaderManager().initLoader(100, new Bundle(), this);

        mCardAdapter = new CardsAdapter(this, null);
        mCardStack.setAdapter(mCardAdapter);


        CustomGrid adapter = new CustomGrid(MainActivity.this, web, backColor, textColor, gifId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("posts");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);

                insertPost(p, dataSnapshot.getKey());
                Toast.makeText(MainActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);

                insertPost(p, dataSnapshot.getKey());
                Toast.makeText(MainActivity.this, "Post Changed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                deletePost(dataSnapshot.getKey());
                Toast.makeText(MainActivity.this, "Post Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void deletePost(String key) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        contentResolver.delete(DontLaughContract.PostsEntry.CONTENT_URI, DontLaughContract.PostsEntry.PID + "=?", new String[]{key});
        contentResolver.notifyChange(DontLaughContract.PostsEntry.CONTENT_URI, null);
    }


    public void insertPost(Post m, String key) {
        ContentValues[] contentValues = new ContentValues[1];
        ContentValues cv = new ContentValues();
        cv.put(DontLaughContract.PostsEntry.PID, key);
        cv.put(DontLaughContract.PostsEntry.CID, m.getCid());
        cv.put(DontLaughContract.PostsEntry.INFO, m.getInfo());
        cv.put(DontLaughContract.PostsEntry.SEEN, m.getSeen());
        cv.put(DontLaughContract.PostsEntry.SHARE, m.getShare());
        cv.put(DontLaughContract.PostsEntry.SHARE_COUNT, m.getShareCount());
        cv.put(DontLaughContract.PostsEntry.STAR, m.getStar());
        cv.put(DontLaughContract.PostsEntry.TIME, m.getTime());
        cv.put(DontLaughContract.PostsEntry.URL, m.getUrl());
        cv.put(DontLaughContract.PostsEntry.URI, m.getUri());


        contentValues[0] = cv;
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        contentResolver.bulkInsert(DontLaughContract.PostsEntry.CONTENT_URI, contentValues);
        contentResolver.notifyChange(DontLaughContract.PostsEntry.CONTENT_URI, null);

        // mRecyclerView.scrollToPosition(0);

        //mChatsAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //share content chooser
    public void sharePost(View v) {

        String imagePath = "android.resource://cse2017.in.ac.nitrkl.dontlaugh/drawable/" + R.drawable.ic_launcher;
        Uri uri = Uri.parse(imagePath);
        String imageFileExtension = imagePath.substring(imagePath.lastIndexOf("."));

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/" + imageFileExtension);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Attached Image");
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //BottomSheet share = BottomSheet.createShareBottomSheet(this, sharingIntent, "Share Via");
// Make sure that it doesn't return null! If the system can not handle the intent, null will be returned.
        //if (share != null) share.show();


        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DontLaughContract.PostsEntry.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this,
                uri,
                null,
                null,
                null,
                DontLaughContract.PostsEntry.TIME + " desc"
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCardAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCardAdapter.swapCursor(null);
    }
}
