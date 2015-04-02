package com.apps.renegade.hackerearthchallenges.helpers;

/**
 * Created by Administrator on 4/2/2015.
 */
public abstract class ListItem {
    public final int item_type;
    public static final int TYPE_DATA = 0;
    public static final int TYPE_SEPERATOR = 1;

    public int getItemType() {
        return this.item_type;
    }

    protected ListItem(int item_type) {
        this.item_type = item_type;
    }
}
