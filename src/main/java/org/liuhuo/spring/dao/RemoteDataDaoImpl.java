package org.liuhuo.spring.dao;

import java.util.List;
import java.util.Set;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.liuhuo.spring.model.IspInfo;
import org.liuhuo.spring.model.RemoteData;
import org.liuhuo.spring.model.ISPDailyRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



@Repository
public class RemoteDataDaoImpl implements RemoteDataDao{

    @Autowired
    private DataSource dataSource;

    public IspInfo findIsp(String ip) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        SqlRowSet rowset = temp.queryForRowSet("select * from isp_info where isp_ip = ?", new Object[] {ip});
        if (!rowset.isBeforeFirst()) {
            return null;
        }
        else {
            rowset.next();
            IspInfo result = new IspInfo(rowset.getInt("isp_id"),
                                         rowset.getString("isp_ip"),
                                         rowset.getString("isp_name"));
            return result;
        }
    }

    public int insertIspInfo(String ip) {
        NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(dataSource);
        String query = "insert into isp_info (isp_ip) values (:ip);";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ip",ip);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        temp.update(query, parameters, keyHolder, new String[]{"isp_id"});

        Number id = keyHolder.getKey();

        return id.intValue();
    }

    public void insertRemoteData(int sourceIpId, Date date, String eventType, String serviceName, int count, boolean test) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "insert into remote_data (event_source,service_name,event_type,event_time, event_count, record_time, test) values (?,?,?,?,?,?,?)";
        temp.update(query,new Object[]{sourceIpId,serviceName,eventType,date,count,new Date(), test});
    }

    public void insertRemoteData(String errorMsg) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "insert into remote_data (error_msg,record_time) values (?,?)";
        temp.update(query,new Object[]{errorMsg, new Date()});
    }

    public List<RemoteData> findAllRecord() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select id, isp_ip as ip, isp_name,service_name, service_category, event_type,event_time,event_count,record_time,test, error_msg from remote_data rd left join  isp_info ii on rd.event_source = ii.isp_id order by record_time";
        return temp.query(query, new RemoteDataRowMapper());
    }

    public List<RemoteData> findRecordInOneSec() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.SECOND, -1);
        Date oneSecAgo = cal.getTime();
        String query = "select id, isp_ip as ip,isp_name, service_name, service_category, event_type,event_time,event_count,record_time,test, error_msg from remote_data rd left join  isp_info ii on rd.event_source = ii.isp_id where record_time >= ? and record_time <= ? order by record_time";
        return temp.query(query, new Object[] {oneSecAgo, now} ,new RemoteDataRowMapper());
    }

    public List<RemoteData> findLast7days() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -7);
        Date prev = cal.getTime();
        return findGivenRange(prev,now);
    }

    public List<RemoteData> findLast14days() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -14);
        Date prev = cal.getTime();
        return findGivenRange(prev,now);
    }

    public List<RemoteData> findLast30days() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -30);
        Date prev = cal.getTime();
        return findGivenRange(prev,now);
    }

    public List<RemoteData> findToday() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, 1);
        Date next = cal.getTime();
        return findGivenRange(now, next);
    }

    public List<RemoteData> findYesterday() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1);
        Date prev = cal.getTime();
        return findGivenRange(prev,now);
    }

    public int findOverallCountToday(String type) {
        Date now = now();
        Date nextDay = anotherDay(now, 1);
        return  findOverallCountGivenRange(now,nextDay,type);
    }

    public int findOverallCountYesterday(String type) {
        Date now = now();
        Date prev = anotherDay(now, -1);
        return  findOverallCountGivenRange(prev,now,type);
    }

    public int findOverallCount7days(String type) {
        Date now = now();
        Date prev = anotherDay(now, -7);
        return  findOverallCountGivenRange(prev,now,type);
    }

    public int findOverallCount14days(String type) {
        Date now = now();
        Date prev = anotherDay(now, -14);
        return  findOverallCountGivenRange(prev,now,type);
    }

    public int findOverallCount30days(String type) {
        Date now = now();
        Date prev = anotherDay(now, -30);
        return  findOverallCountGivenRange(prev,now,type);
    }

    public List<ISPDailyRecord> findIspData7days(String type) {
        Date now = now();
        Date prev = anotherDay(now, -7);
        return findIspDataGivenRange(prev,now,type);
    }

    public List<ISPDailyRecord> findIspData30days(String type) {
        Date now = now();
        Date prev = anotherDay(now, -30);
        return findIspDataGivenRange(prev,now,type);
    }

    public List<ISPDailyRecord> findServiceDetails7days(String[] services, String type, String isp) {
        Date now = now();
        Date prev = anotherDay(now, -7);
        return findServiceDetailsGivenParam(services,prev,now,type,isp);
    }

    public List<ISPDailyRecord> findServiceDetails30days(String[] services, String type, String isp){
        Date now = now();
        Date prev = anotherDay(now, -30);
        return findServiceDetailsGivenParam(services,prev,now,type,isp);
    }


    public List<String> findIspNames() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select distinct isp_name from isp_info where isp_name is not null";
        return temp.queryForList(query,String.class);
    }

    public int findSpecificIspCountToday(String type, String isp) {
        Date now = now();
        Date nextDay = anotherDay(now, 1);
        return  findSpecificIspCountGivenRange(now,nextDay,type,isp);
    }
    public int findSpecificIspCountYesterday(String type, String isp) {
        Date now = now();
        Date prev = anotherDay(now, -1);
        return  findSpecificIspCountGivenRange(prev,now,type,isp);
    }
    public int findSpecificIspCount7days(String type, String isp) {
        Date now = now();
        Date prev = anotherDay(now, -7);
        return  findSpecificIspCountGivenRange(prev,now,type,isp);
    }
    public int findSpecificIspCount14days(String type, String isp) {
        Date now = now();
        Date prev = anotherDay(now, -14);
        return  findSpecificIspCountGivenRange(prev,now,type,isp);
    }
    public int findSpecificIspCount30days(String type, String isp) {
        Date now = now();
        Date prev = anotherDay(now, -30);
        return  findSpecificIspCountGivenRange(prev,now,type,isp);
    }

    public List<String> findServiceNamesGivenIsp(String isp){
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select distinct service_name from isp_info, remote_data where event_source = isp_id and isp_name = ? ";
        return temp.queryForList(query,new Object[] {isp}, String.class);

    }

    public List<ISPDailyRecord> findServiceDetailsInAMonth(String isp) {
        Calendar cal = Calendar.getInstance();
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Date start = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,lastDay);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        Date end = cal.getTime();
        return findServiceDetailsGivenIsp(start,end,isp);
    }

    private List<ISPDailyRecord> findIspDataGivenRange(Date start, Date end, String type) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select isp_name as isp, event_time as record_date, sum(event_count) as push from remote_data, isp_info where isp_id = event_source and event_type = ? and event_time >= ? and event_time <= ? group by isp_name, event_time order by isp_name, event_time";
        return temp.query(query,new Object[] {type,start, end}, new ISPDailyRecordRowMapper());
    }

    private List<RemoteData> findGivenRange(Date start, Date end) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select id, isp_ip as ip,isp_name, service_name, service_category, event_type, event_time, event_count, record_time, test, error_msg from remote_data, isp_info where event_source = isp_id and service_name is not null and event_time >= ? and event_time <= ? and test = false order by event_time";
        return temp.query(query,new Object[] {start, end}, new RemoteDataRowMapper());
    }

    private Date now() {
        return new Date();
    }

    private Date anotherDay(Date now, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE,offset);
        return cal.getTime();
    }

    private int findOverallCountGivenRange(Date start, Date end, String type) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select sum(event_count) from remote_data where test = false and service_name is not null and event_type = ? and event_time >= ? and event_time <= ?";
        List<Integer> result = temp.query(query,new RowMapper<Integer>() {
                public Integer mapRow(ResultSet rs, int row) throws SQLException {
                    return rs.getInt(1);
                }
            },type, start, end);
        return result.isEmpty() ? 0: result.get(0);
    }

    private int findSpecificIspCountGivenRange(Date start, Date end, String type, String isp) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        String query = "select sum(event_count) from remote_data, isp_info where event_source = isp_id and test = false and service_name is not null and isp_name = ?  and event_type = ? and event_time >= ? and event_time <= ?";
        List<Integer> result = temp.query(query,new RowMapper<Integer>() {
                public Integer mapRow(ResultSet rs, int row) throws SQLException {
                    return rs.getInt(1);
                }
            },isp,type, start, end);
        return result.isEmpty() ? 0: result.get(0);
    }

    private List<ISPDailyRecord> findServiceDetailsGivenParam(String[] services, Date start, Date end,
                                                              String type, String isp) {
        NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Set<String> set = new HashSet<>();
        for (String s: services) {
            set.add(s);
        }
        parameters.addValue("services",set);
        parameters.addValue("start",start);
        parameters.addValue("end",end);
        parameters.addValue("type",type);
        parameters.addValue("isp",isp);
        String query = "select service_name as isp, event_time as record_date, sum(event_count) as push from remote_data, isp_info where event_source = isp_id and service_name in (:services) and event_time >= :start and event_time <= :end and isp_name = :isp and event_type = :type group by service_name, event_time order by service_name, event_time";
        List<ISPDailyRecord> result = temp.query(query, parameters,new ISPDailyRecordRowMapper());
        return result;
    }

    private List<ISPDailyRecord> findServiceDetailsGivenIsp(Date start, Date end, String isp) {
        NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("start",start);
        parameters.addValue("end",end);
        parameters.addValue("isp",isp);
        String query = "select service_name as isp, event_time as record_date, sum(event_count) as push from remote_data, isp_info where event_source = isp_id  and event_time >= :start and event_time <= :end and isp_name = :isp  group by  event_time,service_name order by event_time,service_name";
        List<ISPDailyRecord> result = temp.query(query, parameters,new ISPDailyRecordRowMapper());
        return result;
    }
}
