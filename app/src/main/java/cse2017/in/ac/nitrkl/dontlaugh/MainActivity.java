package cse2017.in.ac.nitrkl.dontlaugh;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.wenchao.cardstack.CardStack;

public class MainActivity extends AppCompatActivity implements CardStack.CardEventListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

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


        mCardStack.setAdapter(mCardAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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

        return (distance > 300) ? true : false;
    }

    @Override
    public boolean swipeStart(int direction, float distance) {

        return true;
    }

    @Override
    public boolean swipeContinue(int direction, float distanceX, float distanceY) {

        return true;
    }

    @Override
    public void discarded(int id, int direction) {
        //this callback invoked when dismiss animation is finished.
    }

    @Override
    public void topCardTapped() {
        //this callback invoked when a top card is tapped by user.
    }


}
