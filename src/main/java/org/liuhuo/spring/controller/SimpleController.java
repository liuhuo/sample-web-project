package org.liuhuo.spring.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.liuhuo.spring.model.DummyUser;

@Controller
public class SimpleController {
    private static List<DummyUser> dummyUserList = new ArrayList<>();

    static {
	dummyUserList.add(new DummyUser(1,"dummy user 1","dummy role 1"));
	dummyUserList.add(new DummyUser(2,"dummy user 2","dummy role 2"));
	dummyUserList.add(new DummyUser(3,"dummy user 3","dummy role 3"));
	dummyUserList.add(new DummyUser(4,"dummy user 4","dummy role 4"));
    }

    @ModelAttribute("users")
    public List<DummyUser> setUsers() {
	return dummyUserList;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String all(ModelMap model) {
	return "all";
    }
}
