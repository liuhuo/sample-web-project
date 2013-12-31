package org.liuhuo.spring.model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class PVRecord {
    public String isp;
    public String website;
    public Date date;
    public int clicks;

    public PVRecord() {}

    public PVRecord(String isp, String website, Date date, int clicks) {
        this.isp = isp;
        this.date = date;
        this.clicks = clicks;
        this.website = website;
    }

    public String getIsp() {
        return this.isp;
    }

    public Date getDate() {
        return this.date;
    }

    public int  getClicks() {
        return this.clicks;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public void setDate(String date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.date = formatter.parse(date);
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setClicks(String clicks) {
        this.clicks = Integer.parseInt(clicks);
    }
    
    @Override
    public String toString() {
        return "<" + isp + " " + website + " " + date + " " + clicks + ">";
    }
}
