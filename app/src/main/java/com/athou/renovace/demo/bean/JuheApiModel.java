package com.athou.renovace.demo.bean;

/**
 * Created by athou on 2016/10/28.
 */

public class JuheApiModel {

    long id;  /*事件ID*/
    String des;
    String lunar;
    String pic;
    String title;
    int year;
    int month;
    int day;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "JuheApiModel{" +
                "id=" + id +
                ", des='" + des + '\'' +
                ", lunar='" + lunar + '\'' +
                ", pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
