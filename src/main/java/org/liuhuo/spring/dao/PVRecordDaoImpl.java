package org.liuhuo.spring.dao;

import java.util.List;
import java.util.Set;
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

import org.liuhuo.spring.model.ISPDailyRecord;

@Repository
public class PVRecordDaoImpl implements PVRecordDao {
    
    @Autowired
    DataSource dataSource;

    public List<java.sql.Date> selectDate() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        return temp.queryForList("select distinct record_date from pv_record order by record_date",java.sql.Date.class);
    }

    public List<java.sql.Date> rangeDate(String start, String end) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        return temp.queryForList("select distinct record_date from pv_record where record_date >= ? and record_date <= ? order by record_date",java.sql.Date.class, new Object[]{start,end});
    }

    public List<ISPDailyRecord> ispDailyPush() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        List<ISPDailyRecord> result = temp.query("select isp, record_date, sum(pv_push) as push from pv_record group by isp, record_date order by isp,record_date;", new ISPDailyRecordRowMapper());
        return result;
    }

    public List<ISPDailyRecord> ispRangePush(String start, String end) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        List<ISPDailyRecord> result = temp.query("select isp, record_date, sum(pv_push) as push from pv_record where record_date >= ? and record_date <= ? group by isp, record_date order by isp,record_date;", new Object[] {start, end},new ISPDailyRecordRowMapper());
        return result;
    }

    public List<ISPDailyRecord> selectedServicePush(String[] params) {
        NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Set<String> set = new HashSet<>();
        for (String s: params) {
            set.add(s);
        }
        parameters.addValue("p",set);
        List<ISPDailyRecord> result = temp.query("select website as isp, record_date, sum(pv_push) as push from pv_record where website in (:p) group by website, record_date order by isp,record_date", parameters,new ISPDailyRecordRowMapper());
        return result;
    }

    public List<String> serviceNames() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        List<String> names = temp.queryForList("select distinct website from pv_record",String.class);
        return names;
    }

    public List<String> ispNames() {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        List<String> names = temp.queryForList("select distinct isp from pv_record",String.class);
        return names;
    }

    public List<String> serviceNames(String isp) {
        JdbcTemplate temp = new JdbcTemplate(dataSource);
        List<String> names = temp.queryForList("select distinct website from pv_record where isp = ? ",String.class,new Object[]{isp});
        return names;
    }
}

//' select record_date,sum(pv_push) from pv_record  where isp = '福建铁通2' group by (unix_timestamp(record_date) -  unix_timestamp('2013-11-01')%(86400*7)) div (86400*7);''
