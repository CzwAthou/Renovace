package com.athou.renovace.demo.bean;

/**
 * Created by athou on 2016/10/28.
 */

public class GankIOModel {
    private String _id;
    private String content;
    private String publishedAt;
    private String title;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GankIOModel{" +
                "_id='" + _id + '\'' +
                ", content='" + content + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
