package com.apps.renegade.hackerearthchallenges.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.renegade.hackerearthchallenges.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 4/2/2015.
 */
public class CustomListAdapter extends ArrayAdapter<ListItem> {
    public ArrayList<ListItem> mList;
    public static final String TYPE_COLLEGE = "college";
    public static final String TYPE_HIRING = "hiring";
    public static final String TYPE_CONTEST = "contest";
    public static int red;
    public static int green;
    public static int blue;
    private ImageCache mCache;

    Context context;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (getItemViewType(position) == ListItem.TYPE_DATA)
                convertView = inflater.inflate(R.layout.data_layout, null, false);

        }

        Data data = (Data) mList.get(position);
        // Log.d("CustomAdapter", "Data at:"+position+" " + data.getTitle());
        if (convertView != null) {
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView status = (TextView) convertView.findViewById(R.id.status);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView url = (TextView) convertView.findViewById(R.id.url);
            TextView desc = (TextView) convertView.findViewById(R.id.description);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            ImageView thumb = (ImageView) convertView.findViewById(R.id.thumbnail);
            title.setText(data.getTitle());
            status.setText(data.getStatus());
            date.setText(data.getDate_duration());
            time.setText(data.getTime_duration());
            url.setText(data.getUrl());
            desc.setText(data.getDesc());
            type.setText(data.getType());
            if (mCache.isReady()) {
                if (!data.getThumbnail().equals("")) {
                    Bitmap bmp = mCache.getBitmap(data.getThumbnail());
                    if (bmp != null) {
                        thumb.setImageBitmap(bmp);
                    }
                }else{
                    thumb.setImageResource(R.drawable.hacker);
                }
            }
            if (data.getType().equals(TYPE_COLLEGE)) {
                type.setBackgroundColor(blue);
            }
            if (data.getType().equals(TYPE_CONTEST)) {
                type.setBackgroundColor(green);
            }
            if (data.getType().equals(TYPE_HIRING)) {
                type.setBackgroundColor(red);
            }
        }
        return convertView;
    }

    public CustomListAdapter(Context context, int resource, ArrayList<ListItem> objects, ImageCache cache) {
        super(context, resource, objects);
        this.context = context;
        this.mCache = cache;
        blue = context.getResources().getColor(R.color.paleblue);
        green = context.getResources().getColor(R.color.green);
        red = context.getResources().getColor(R.color.red);
        mList = objects;
    }


}

