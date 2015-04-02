package com.apps.renegade.hackerearthchallenges.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 4/2/2015.
 */
public class ListManager {
    private ArrayList<ListItem> mList;
    private ChallengeFetcher mFetcher;
    private Context mContext;
    private CustomListAdapter mAdapter = null;
    private SwipeRefreshLayout mSwiper;
    private ImageCache mImageCache;


    public static final String KEY_DESC = "description";
    public static final String KEY_STATUS = "status";
    public static final String KEY_START = "start_timestamp";
    public static final String KEY_END = "end_timestamp";
    public static final String KEY_TYPE = "challenge_type";
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_THUMBNAIL = "thumbnail";

    public ListManager(Context context) {
        mList = new ArrayList<>();
        mFetcher = new ChallengeFetcher(context, this);
        mContext = context;
    }

    public void setAdapter(CustomListAdapter adapter) {
        mAdapter = adapter;
    }

    public void setSwiper(SwipeRefreshLayout swiper) {
        mSwiper = swiper;
    }

    public void setImageCache(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    public void refresh() {
        if (!isConnected()) {
            Toast.makeText(mContext, "Unable to connect. Please check your internet connection.", Toast.LENGTH_LONG).show();
            mSwiper.setRefreshing(false);
        } else {
            Toast.makeText(mContext, "Download started", Toast.LENGTH_SHORT).show();
            mFetcher.startDownload();
        }

    }

    public void onFinishedRefresh(JSONArray array) {
        Toast.makeText(mContext, "Download finished", Toast.LENGTH_SHORT).show();
        mSwiper.setRefreshing(false);
        if (array == null) {
            Toast.makeText(mContext, "Unable to fetch challenges, please try again later.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("onFinishedRefresh", array.length() + "");

        mList.clear();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject x = array.getJSONObject(i);
                if (x != null) {
                    Data curr = new Data();
                    curr.setDesc(x.getString(KEY_DESC));
                    curr.setEnd_time(x.getString(KEY_END));
                    curr.setStart_time(x.getString(KEY_START));
                    curr.setTitle(x.getString(KEY_TITLE));
                    curr.setStatus(x.getString(KEY_STATUS));
                    curr.setUrl(x.getString(KEY_URL));
                    curr.setType(x.getString(KEY_TYPE));
                    if (x.has(KEY_THUMBNAIL))
                        curr.setThumbnail(x.getString(KEY_THUMBNAIL));
                    curr.calculateDurations();
                    mList.add(curr);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("JSON to Data", "unable to extract JSON from array");
            }
        }

        Log.d("onFinishedRefresh", "Final arraySize: " + mList.size());
         mImageCache.refreshCache(mList);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<ListItem> getmList() {
        return this.mList;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


}
