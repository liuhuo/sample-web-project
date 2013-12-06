package org.liuhuo.spring.dao;

import java.util.List;

import org.liuhuo.spring.model.DummyUser;

public interface DummyUserDao {
    public void addDummyUser(DummyUser user);
    public List<DummyUser> selectAll();
    public void deleteDummyUser(DummyUser user);
}
