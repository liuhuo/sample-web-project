package org.liuhuo.spring.dao;

import java.util.List;
import java.util.Set;
import java.util.Date;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.liuhuo.spring.model.IspInfo;
import org.liuhuo.spring.model.RemoteData;

import org.liuhuo.spring.model.ISPDailyRecord;

public interface RemoteDataDao {
    public IspInfo findIsp(String ip);
    public int insertIspInfo(String ip);
    public void insertRemoteData(int sourceIpId, Date date, String eventType, String serviceName, int count, boolean test);
    public void insertRemoteData(String errorMsg);
    public List<RemoteData> findAllRecord();
    public List<RemoteData> findRecordInOneSec();
    public List<RemoteData> findToday();
    public List<RemoteData> findYesterday();
    public List<RemoteData> findLast7days();
    public List<RemoteData> findLast14days();
    public List<RemoteData> findLast30days();
    public int findOverallCountToday(String type);
    public int findOverallCountYesterday(String type);
    public int findOverallCount7days(String type);
    public int findOverallCount14days(String type);
    public int findOverallCount30days(String type);

    public List<ISPDailyRecord> findIspData7days(String type);
    public List<ISPDailyRecord> findIspData30days(String type);

    public List<String> findIspNames();

    public int findSpecificIspCountToday(String type, String isp);
    public int findSpecificIspCountYesterday(String type, String isp);
    public int findSpecificIspCount7days(String type, String isp);
    public int findSpecificIspCount14days(String type, String isp);
    public int findSpecificIspCount30days(String type, String isp);

    public List<String> findServiceNamesGivenIsp(String isp);

    public List<ISPDailyRecord> findServiceDetails7days(String[] services, String type, String isp);
    public List<ISPDailyRecord> findServiceDetails30days(String[] services, String type, String isp);


    public List<ISPDailyRecord> findServiceDetailsInAMonth(String isp);
}
