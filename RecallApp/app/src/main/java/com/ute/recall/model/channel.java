package com.ute.recall.model;

import java.util.List;

public class channel {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<com.ute.recall.model.feeds> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<com.ute.recall.model.feeds> feeds) {
        this.feeds = feeds;
    }

    private  int id;
    private String name;
    private  String description;
    private String latitude;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String created_at;
    private List<feeds> feeds;

    public List<List<com.ute.recall.model.feeds>> getFeeds_sep() {
        return feeds_sep;
    }

    public void setFeeds_sep(List<List<com.ute.recall.model.feeds>> feeds_sep) {
        this.feeds_sep = feeds_sep;
    }

    List<List<feeds>> feeds_sep;
    public channel(int id, String name, String description, String latitude, String field1, String field2, String field3, String field4, String created_at, List<com.ute.recall.model.feeds> feeds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.created_at = created_at;
        this.feeds = feeds;
    }



}

