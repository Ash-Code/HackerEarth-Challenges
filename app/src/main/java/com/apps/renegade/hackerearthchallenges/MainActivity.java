package com.apps.renegade.hackerearthchallenges;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import com.apps.renegade.hackerearthchallenges.helpers.CustomListAdapter;
import com.apps.renegade.hackerearthchallenges.helpers.ImageCache;
import com.apps.renegade.hackerearthchallenges.helpers.ListManager;
import com.apps.renegade.hackerearthchallenges.helpers.ScrollHandler;


public class MainActivity extends ActionBarActivity {
    public ListView mListView;
    private ListManager mManager;
    private ImageView mHeaderImage;
    private ViewGroup mHeaderLayout;
    private View mPlaceHolder;
    private ImageCache mImageCache;
    private LruCache<String, Bitmap> mLruCache;
    private CustomListAdapter mAdapter;
    private SwipeRefreshLayout mSwiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.main_list);
        mHeaderImage = (ImageView) findViewById(R.id.backImage);
        mHeaderLayout = (ViewGroup) findViewById(R.id.headerLayout);
        mSwiper = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
        init();

    }


    public void init() {
        final int maxMem = (int) Runtime.getRuntime().maxMemory() / 1024;
        int capacity = maxMem / 4;
        Log.d("LRU Cache created", capacity + "");
        mLruCache = new LruCache<String, Bitmap>(capacity) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        mImageCache = new ImageCache(getBaseContext(), mLruCache);
        mManager = new ListManager(getBaseContext());
        mAdapter = new CustomListAdapter(getBaseContext(), R.layout.data_layout, mManager.getmList(),mImageCache);
        mPlaceHolder = getLayoutInflater().inflate(R.layout.fake_header, mListView, false);
        mListView.addHeaderView(mPlaceHolder);
        mSwiper.setColorSchemeResources(R.color.red,R.color.green,R.color.base_blue);
        mManager.setAdapter(mAdapter);
        mManager.setSwiper(mSwiper);
        mManager.setImageCache(mImageCache);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new ScrollHandler(this, mHeaderLayout, mListView, mHeaderImage));
        mSwiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mManager.refresh();
            }
        });
        mManager.refresh();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
