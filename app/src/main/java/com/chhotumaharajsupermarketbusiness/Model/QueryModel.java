package com.chhotumaharajsupermarketbusiness.Model;

import java.util.List;

public class QueryModel {

    private int id;
    private String name;
    private String ppt;
    private String video;
    List<SubQueryModel> subQueryModels;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPpt() {
        return ppt;
    }

    public void setPpt(String ppt) {
        this.ppt = ppt;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<SubQueryModel> getSubQueryModels() {
        return subQueryModels;
    }

    public void setSubQueryModels(List<SubQueryModel> subQueryModels) {
        this.subQueryModels = subQueryModels;
    }
}
