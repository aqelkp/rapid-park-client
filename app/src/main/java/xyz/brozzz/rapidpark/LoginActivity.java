package xyz.brozzz.rapidpark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String[] descriptions, headers;
    ImageView[] radios,Images;
    Boolean ButtonVisiblity=false;
    private View hiddenPanel,container;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);

        descriptions = getResources().getStringArray(R.array.descriptions);
        List<String> desList = Arrays.<String>asList(descriptions);
        headers = getResources().getStringArray(R.array.headers);
        List<String> headList = Arrays.<String>asList(headers);
        mCustomPagerAdapter = new CustomPagerAdapter(this,new ArrayList<String>(desList),new ArrayList<String>(headList));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);


        LinearLayout fbtn =(LinearLayout) findViewById(R.id.bfacebook);
        LinearLayout gbtn =(LinearLayout) findViewById(R.id.bgoogle);
        fbtn.setOnClickListener(this);
        gbtn.setOnClickListener(this);




        radios =  new ImageView[] { (ImageView) findViewById(R.id.radio1), (ImageView) findViewById(R.id.radio3), (ImageView) findViewById(R.id.radio3) };
        container=(LinearLayout) findViewById(R.id.hpcontainer);
        final boolean[] heighwidthtset = {true,true};
        final float[] width = new float[1];
        final float[] height = new float[1];
        final float[] positionRadio = new float[3];
        Images =  new ImageView[] {
                (ImageView) findViewById(R.id.radiodot),
                (ImageView) findViewById(R.id.radio2),
                (ImageView) findViewById(R.id.radio3)};


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(heighwidthtset[0]){

                    height[0] =mViewPager.getHeight();
                    width[0] =mViewPager.getWidth();
                    positionRadio[0]=Images[0].getWidth()/2+Images[0].getLeft();
                    positionRadio[1]=Images[1].getWidth()/2+Images[1].getLeft();
                    positionRadio[2]=Images[2].getWidth()/2+Images[2].getLeft();
                    Log.d("positionRadio",positionRadio.toString());
                    heighwidthtset[0] =false;
                }
                positionRadio[0]=Images[0].getWidth()/2+Images[0].getLeft();
                positionRadio[1]=Images[1].getWidth()/2+Images[1].getLeft();
                positionRadio[2]=Images[2].getWidth()/2+Images[2].getLeft();
                Log.d("positionRadio",positionRadio.toString());
                if(position==0){
                    Images[0].setTranslationX(positionOffset * (positionRadio[1] - positionRadio[0]));
                }else if(position==1){
                    Images[0].setTranslationX((positionOffset+1)*(positionRadio[1]-positionRadio[0]));
                    if(heighwidthtset[1]){
                        container.setTranslationY((float) -(pxFromDp(getBaseContext(),132)*positionOffset));
                    }
                } else if(position==2){
                    heighwidthtset[1]=false;
                }

            }

            @Override
            public void onPageSelected(int position) {
                changePagination(position);
                if (position == 2) {
                    // slideUpDown();
                }
            }

            private void changePagination(int position) {
                /*for (int i = 0; i < radios.length; i++) {
                    radios[i].setImageResource(R.drawable.radio_off);
                }
                radios[position].setImageResource(R.drawable.radio_on);*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    public void slideUpDown() {
        container=(LinearLayout) findViewById(R.id.hpcontainer);
        hiddenPanel=(LinearLayout) findViewById(R.id.hidden_pannel);
        if (!ButtonVisiblity) {
            // Show the panel
            ButtonVisiblity=true;
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            container.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }

    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bgoogle) {

        }
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        switch (v.getId()){

        }
    }






    class CustomPagerAdapter extends PagerAdapter {

        ArrayList<String> someList = new ArrayList<String>();
        ArrayList<String> headList = new ArrayList<String>();

        Context context;

        public CustomPagerAdapter(Context context, ArrayList<String> newsomeList,ArrayList<String> newHeadList) {

            super();
            this.context = context;
            this.someList = newsomeList;
            this.headList = newHeadList;

        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);

        }

        @Override
        public int getCount() {

            return someList.size();

        }

        @Override
        public boolean isViewFromObject(View collection, Object object) {

            return collection == ((View) object);
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            // Inflating layout
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Setting view you want to display as a row element
            View view = inflater.inflate(R.layout.fr_login_text, null);

            TextView itemText = (TextView) view.findViewById(R.id.tvDescription);
            TextView headText = (TextView) view.findViewById(R.id.tvHead);

            // Getting reference for text view and inflate the view for Answers

            try {

                itemText.setText(someList.get(position));
                headText.setText(headList.get(position));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ((ViewPager) collection).addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);

        }
    }


}
