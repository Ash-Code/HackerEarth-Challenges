package com.apps.renegade.hackerearthchallenges.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Created by Administrator on 4/2/2015.
 */
public class ChallengeFetcher {

    Context mContext;

    ListManager mManager;


    public ChallengeFetcher(Context context, ListManager manager) {
        this.mContext = context;
        mManager = manager;
    }

    public String readJSONFeed(String URL) {
        Log.d("Checking availability ", isInternetAvailable() + "");
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.d("Connection status ", statusCode + "");
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                Log.d("Content toString ", content.toString());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    Log.d("Downloading ", line);
                }
                content.close();
                Log.d("Download ended", "true");
            } else {
                Log.e("JSON", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public void startDownload() {
        new GetJSONFeedTask(mManager).execute("https://www.hackerearth.com/api/events/upcoming/?format=json");
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("https://www.hackerearth.com/api/events/upcoming/?format=json");

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }


    public class GetJSONFeedTask extends AsyncTask<String, Void, String> {
        ListManager mManager;

        public GetJSONFeedTask(ListManager manager) {
            this.mManager = manager;
        }

        @Override
        protected String doInBackground(String... params) {
            return readJSONFeed(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray array = new JSONArray(result);
                Log.d("Successfully parsed", array.length() + " " + result.length());
                mManager.onFinishedRefresh(array);
            } catch (Exception e) {
                Log.d("JSON PARSING", "unable to parse from string to JSONArray " + result.length());
            }
        }
    }


}
