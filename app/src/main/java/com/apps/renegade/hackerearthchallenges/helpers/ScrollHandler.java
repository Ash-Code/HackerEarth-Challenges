package com.apps.renegade.hackerearthchallenges.helpers;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 4/2/2015.
 */
public class ScrollHandler implements ListView.OnScrollListener {
    private ImageView mHeaderImage;
    private ListView mListView;

    private int headerHeight;
    private Activity mainActivity;
    private ActionBar actionBar;

    private ViewGroup mHeaderLayout;

    public ScrollHandler(Activity act, ViewGroup headerLayout, ListView list, ImageView image) {
        mainActivity = act;
        mHeaderImage = image;
        mHeaderLayout = headerLayout;
        mListView = list;
        headerHeight = mHeaderLayout.getBottom();
        actionBar = mainActivity.getActionBar();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //********GLITCH THE IMAGEVIEW STUCK ON FLING
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int scroll = getScrollY();
        headerHeight = mHeaderLayout.getBottom();


        Matrix matrix = setMatrix(mHeaderImage);

        matrix.postTranslate(0, getTranslation(scroll));

        // mHeaderImage.setColorFilter(Color.argb(getTint(scroll), 0, 0, 0));

        mHeaderImage.setImageMatrix(matrix);
        mHeaderLayout.setTranslationY(-1 * scroll);
        mHeaderLayout.requestLayout();

        //mTextView.setText("Scroll : " + scroll + " headerHeight: " + headerHeight + " Header Bottom: " + mHeaderImage.getBottom() + "\n Action " + actionBar + " Tint: " + getTint(scroll) + " Translation: " + getTranslation(scroll));

    }

    public Matrix setMatrix(ImageView view) {
        Drawable d = view.getDrawable();
        float h = d.getIntrinsicHeight();
        float w = d.getIntrinsicWidth();
        float vH = view.getBottom();
        float vW = view.getWidth();
        Matrix matrix = new Matrix();
        matrix.reset();
        float scale = Math.max(vH / h, vW / w);
        scale += 0.1;
        matrix.postScale(scale, scale);
        return matrix;

    }


    public float getTranslation(int scroll) {
        return ((float) scroll) / 3f;
    }


    public int getScrollY() {
        int firstVisiblePos = mListView.getFirstVisiblePosition();
        View c = mListView.getChildAt(0);
        int listH = 0;
        int displ = 0;
        if (c != null) {
            displ = -1 * c.getTop();
            listH = c.getHeight();
        }
        int headerH = 0;
        if (firstVisiblePos >= 1)
            headerH = mHeaderImage.getHeight();
        return displ + firstVisiblePos * listH + headerH;

    }

}
