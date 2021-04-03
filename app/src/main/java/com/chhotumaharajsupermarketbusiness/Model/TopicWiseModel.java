package com.chhotumaharajsupermarketbusiness.Model;

public class TopicWiseModel {

    private int number;
    private int id;
    private String name;
    private String ppt;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private String video;

    public TopicWiseModel(int number, int id, String name, String ppt, String video) {
        this.number = number;
        this.id = id;
        this.name = name;
        this.ppt = ppt;
        this.video = video;
    }

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
}
