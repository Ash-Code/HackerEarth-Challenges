package com.apps.renegade.hackerearthchallenges.helpers;

/**
 * Created by Administrator on 4/2/2015.
 */
public class Data extends ListItem {
    private String status = "";
    private String type = "";
    private String desc = "";
    private String title = "";
    private String url = "";
    private String start_time = "";
    private String end_time = "";
    private String thumbnail="";
    private String date_duration = "";
    private String time_duration = "";

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    protected Data() {
        super(ListItem.TYPE_DATA);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void calculateDurations() {
        if (!(start_time.equals("") && end_time.equals(""))) {
            String[] start = start_time.split(", ");
            String[] end = end_time.split(", ");
            date_duration = start[0] + " -> " + end[0];
            time_duration = start[1] + " -> " + end[1];
        }
    }


    public String getDate_duration() {
        return date_duration;
    }

    public String getTime_duration() {
        return time_duration;
    }
}
