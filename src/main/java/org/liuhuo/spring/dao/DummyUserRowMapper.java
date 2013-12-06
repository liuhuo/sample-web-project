package org.liuhuo.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.liuhuo.spring.model.DummyUser;

public class DummyUserRowMapper implements RowMapper<DummyUser> {

    public DummyUser mapRow(ResultSet set, int arg1) throws SQLException {
	DummyUser user = new DummyUser(set.getInt(1), set.getString(2), set.getString(3));
	return user;
    }
}
