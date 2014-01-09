package org.liuhuo.spring.dao;

import java.util.List;
import java.util.Set;
import java.util.Date;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.liuhuo.spring.model.IspInfo;
import org.liuhuo.spring.model.RemoteData;

public interface RemoteDataDao {
    public IspInfo findIsp(String ip);
    public int insertIspInfo(String ip);
    public void insertRemoteData(int sourceIpId, Date date, String eventType, String serviceName, int count, boolean test);
    public void insertRemoteData(String errorMsg);
    public List<RemoteData> findAllRecord();
    public List<RemoteData> findRecordInOneSec();
}


