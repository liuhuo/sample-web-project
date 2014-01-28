package org.liuhuo.spring.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.io.Serializable;


public class ISPDailyRecord implements Serializable {
    String isp;
    Timestamp date;
    int clicks;

    public ISPDailyRecord(String isp, Timestamp date, int clicks) {
        this.isp = isp;
        this.date = date;
        this.clicks = clicks;
    }

    public String getIsp() {
        return this.isp;
    }

    public Timestamp getDate() {
        return this.date;
    }

    public int  getClicks() {
        return this.clicks;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public void setDate(String date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.date = new Timestamp(formatter.parse(date).getTime());
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "<" + isp + " " + date + " " + clicks + ">";
    }
}
