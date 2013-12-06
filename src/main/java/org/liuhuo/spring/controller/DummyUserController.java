package org.liuhuo.spring.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import org.liuhuo.spring.model.DummyUser;
import org.liuhuo.spring.dao.DummyUserDao;

@Controller
public class DummyUserController {

    @Autowired
    private DummyUserDao dummyUserDao;



    @ModelAttribute("users")
    public List<DummyUser> setUsers() {
	return dummyUserDao.selectAll();
    }

    @RequestMapping(value = "/otherall", method = RequestMethod.GET)
    public String all(ModelMap model) {
	return "all";
    }

    @RequestMapping(value = "/alluser", method = RequestMethod.GET)
    public @ResponseBody DummyUser alluser() {
	DummyUser user = dummyUserDao.selectAll().get(0);
	System.out.println(user);
	return user;
    }
}
