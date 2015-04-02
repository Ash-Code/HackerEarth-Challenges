package com.apps.renegade.hackerearthchallenges.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Administrator on 4/3/2015.
 */
public class ImageCache {
    private LruCache<String, Bitmap> mCache;
    private Context mContext;
    private boolean ready = false;


    public ImageCache(Context context, LruCache<String, Bitmap> cache) {
        this.mContext = context;
        this.mCache = cache;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    public void refreshCache(ArrayList<ListItem> arr) {
        ready = false;
        if (!isConnected())
            return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            Data curr = (Data) arr.get(i);
            if (!curr.getThumbnail().equals("")) {
                Bitmap bmp = mCache.get(curr.getThumbnail());
                if (bmp == null)
                    builder.append(curr.getThumbnail()).append(" ");
                else {
                    Log.d("ImageCache ", "found image");
                }
            }
        }
        new DownloadImageTask().execute(builder.toString());

    }

    public boolean isReady() {
        return ready;
    }

    public Bitmap getBitmap(String key) {
        if (isReady()) {
            return mCache.get(key);
        } else
            return null;
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }


    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());

        }
        return bitmap;
    }

    public LruCache<String, Bitmap> getmCache() {
        return mCache;
    }


    private class BitmapKey {
        Bitmap bitmap;
        String Key;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getKey() {
            return Key;
        }

        public BitmapKey(Bitmap b, String k) {
            bitmap = b;
            Key = k;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, BitmapKey, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String[] urls = params[0].split(" ");
            for (int i = 0; i < urls.length; i++) {
                Bitmap bmp = DownloadImage(urls[i]);
                publishProgress(new BitmapKey(bmp, urls[i]));
            }
            return urls.length;
        }

        @Override
        protected void onProgressUpdate(BitmapKey... values) {
            mCache.put(values[0].getKey(), values[0].getBitmap());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(mContext, integer + " downloaded images", Toast.LENGTH_SHORT).show();
            ready = true;

        }
    }
}
