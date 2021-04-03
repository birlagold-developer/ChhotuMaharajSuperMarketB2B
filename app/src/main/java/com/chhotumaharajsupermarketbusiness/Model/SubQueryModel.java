package com.chhotumaharajsupermarketbusiness.Model;

public class SubQueryModel {

    private int id;
    private String video_title;
    private String video_query;
    private boolean isSelected = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_query() {
        return video_query;
    }

    public void setVideo_query(String video_query) {
        this.video_query = video_query;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
