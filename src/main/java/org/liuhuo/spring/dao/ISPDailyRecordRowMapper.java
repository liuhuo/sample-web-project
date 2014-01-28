package org.liuhuo.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.liuhuo.spring.model.ISPDailyRecord;

public class ISPDailyRecordRowMapper implements RowMapper<ISPDailyRecord> {
    public ISPDailyRecord mapRow(ResultSet rs, int row) throws SQLException {
        ISPDailyRecord record = new ISPDailyRecord(rs.getString("isp"), rs.getTimestamp("record_date"),rs.getInt("push"));
        return record;
    }
}
