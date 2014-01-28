package org.liuhuo.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.liuhuo.spring.model.RemoteData;

public class RemoteDataRowMapper implements RowMapper<RemoteData> {
    public RemoteData mapRow(ResultSet rs, int row) throws SQLException {
        RemoteData data = new RemoteData();
        data.setId(rs.getInt("id"));
        data.setIp(rs.getString("ip"));
        data.setIspName(rs.getString("isp_name"));
        data.setServiceName(rs.getString("service_name"));
        data.setServiceCategory(rs.getString("service_category"));
        String type = rs.getString("event_type");
        data.setEventType(type == null ? null: (type.equals("1")) ? "PV":"UV");
        data.setEventTime(rs.getTimestamp("event_time"));
        data.setEventCount(rs.getInt("event_count"));
        data.setRecordTime(rs.getTimestamp("record_time"));
        data.setTestFlag(rs.getBoolean("test"));
        data.setErrorMsg(rs.getString("error_msg"));
        return data;
    }
}
