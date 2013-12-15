package org.liuhuo.spring.dao;

import org.liuhuo.spring.model.DummyUser;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class DummyUserDaoImpl implements DummyUserDao {

    @Autowired
    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addDummyUser(DummyUser user) {
	JdbcTemplate createUser = new JdbcTemplate(dataSource);
	createUser.update("insert into dummy_user (name, role) values (?, ?)",
			  new Object[] {user.getName(), user.getRole()});
    }

    public List<DummyUser> selectAll() {
	JdbcTemplate select = new JdbcTemplate(dataSource);
	return select.query("select * from dummy_user",
			    new DummyUserRowMapper());
    }

    public void deleteDummyUser(DummyUser user) {}

}
