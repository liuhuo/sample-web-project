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
        String query = "select id, isp_ip as ip, service_name, service_category, event_type,event_time,event_count,record_time,test, error_msg from remote_data rd left join  isp_info ii on rd.event_source = ii.isp_id order by record_time";
        return temp.query(query, new RemoteDataRowMapper());
    }

    public List<RemoteData> findRecordInOneSec() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.SECOND, -1);
        Date oneSecAgo = cal.getTime();
        String query = "select id, isp_ip as ip, service_name, service_category, event_type,event_time,event_count,record_time,test, error_msg from remote_data rd left join  isp_info ii on rd.event_source = ii.isp_id where record_time >= ? and record_time <= ? order by record_time";
        return temp.query(query, new Object[] {oneSecAgo, now} ,new RemoteDataRowMapper());
    }
}
