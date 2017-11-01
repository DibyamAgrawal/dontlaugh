package cse2017.in.ac.nitrkl.dontlaugh;

/**
 * Created by me on 11/1/2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wenchao.cardstack.CardAnimator;
import com.wenchao.cardstack.CardUtils;
import com.wenchao.cardstack.DefaultStackEventListener;
import com.wenchao.cardstack.DragGestureDetector;

import java.util.ArrayList;


public class CardStack extends RelativeLayout {
    private int mColor = -1;
    private int mIndex = 0;
    private int mNumVisible = 4;
    private CardsAdapter mAdapter;
    private OnTouchListener mOnTouchListener;
    private CardAnimator mCardAnimator;
    //private Queue<View> mIdleStack = new Queue<View>


    private com.wenchao.cardstack.CardStack.CardEventListener mEventListener = new DefaultStackEventListener(300);
    private int mContentResource = 0;
    public CardStack(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, com.wenchao.cardstack.R.styleable.CardStack);
            mColor = array.getColor(com.wenchao.cardstack.R.styleable.CardStack_backgroundColor, mColor);
            array.recycle();
        }

        //String sMyValue = attrs.getAttributeValue( "http://schemas.android.com/apk/res/android", "padding" );
        //get attrs assign minVisiableNum
        for(int i = 0; i<mNumVisible; i++){
            addContainerViews();
        }
        setupAnimation();
    }

    private void addContainerViews(){
        FrameLayout v =  new FrameLayout(getContext());
        viewCollection.add(v);
        addView(v);
    }

    public void setStackMargin(int margin){
        mCardAnimator.setStackMargin(margin);
        mCardAnimator.initLayout();
    }

    public void setContentResource(int res){
        mContentResource = res;
    }

    public void reset(boolean resetIndex){
        if(resetIndex) mIndex = 0;
        removeAllViews();
        viewCollection.clear();
        for(int i = 0; i<mNumVisible; i++){
            addContainerViews();
        }
        setupAnimation();
    }


    private void setupAnimation(){
        final View cardView = viewCollection.get(viewCollection.size()-1);
        mCardAnimator = new CardAnimator(viewCollection, mColor);
        mCardAnimator.initLayout();

        final DragGestureDetector dd = new DragGestureDetector(getContext(),new DragGestureDetector.DragListener(){

            @Override
            public  boolean onDragStart(MotionEvent e1, MotionEvent e2,
                                        float distanceX, float distanceY) {
                mCardAnimator.drag(e1,e2,distanceX,distanceY);
                return true;
            }

            @Override
            public boolean onDragContinue(MotionEvent e1, MotionEvent e2,
                                          float distanceX, float distanceY) {
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                //float distance = CardUtils.distance(x1,y1,x2,y2);
                final int direction = CardUtils.direction(x1,y1,x2,y2);
                mCardAnimator.drag(e1,e2,distanceX,distanceY);
                mEventListener.swipeContinue(direction, Math.abs(x2-x1),Math.abs(y2-y1));
                return true;
            }

            @Override
            public  boolean onDragEnd(MotionEvent e1, MotionEvent e2) {
                //reverse(e1,e2);
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                float distance = CardUtils.distance(x1,y1,x2,y2);
                final int direction = CardUtils.direction(x1,y1,x2,y2);

                boolean discard = mEventListener.swipeEnd(direction, distance);
                if(discard){
                    mCardAnimator.discard(direction, new AnimatorListenerAdapter(){

                        @Override
                        public void onAnimationEnd(Animator arg0) {
                            mCardAnimator.initLayout();
                            mIndex++;
                            mEventListener.discarded(mIndex,direction);

                            viewCollection.get(0).setOnTouchListener(null);
                            viewCollection.get(viewCollection.size()-1)
                                    .setOnTouchListener(mOnTouchListener);
                        }

                    });
                }else{
                    mCardAnimator.reverse(e1,e2);
                }
                return true;
            }

            @Override
            public boolean onTapUp() {
                mEventListener.topCardTapped();
                return true;
            }
        }
        );

        mOnTouchListener = new OnTouchListener() {
            private static final String DEBUG_TAG = "MotionEvents";
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                dd.onTouchEvent(event);
                return true;
            }
        };
        cardView.setOnTouchListener(mOnTouchListener);
    }


    ArrayList<View> viewCollection = new ArrayList<View>();
    public CardStack(Context context) {
        super(context);
    }

    public void setAdapter(final CardsAdapter adapter){
        if(mAdapter != null){
            mAdapter=adapter;
        }
    }
}




