package org.liuhuo.spring.dao;

import java.util.List;
import java.util.Set;

import org.liuhuo.spring.model.ISPDailyRecord;

public interface PVRecordDao {
    public List<java.sql.Date> selectDate();
    public List<java.sql.Date> rangeDate(String start, String end);
    public List<ISPDailyRecord> ispDailyPush();
    public List<ISPDailyRecord> ispRangePush(String start, String end);
    public List<ISPDailyRecord> selectedServicePush(String[] params);
    public List<String> serviceNames();
    public List<String> ispNames();
    public List<String> serviceNames(String isp);
}
