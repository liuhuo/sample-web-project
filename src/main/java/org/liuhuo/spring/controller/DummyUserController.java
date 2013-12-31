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

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
    public @ResponseBody List<DummyUser>  alluser() {
	List<DummyUser> users = dummyUserDao.selectAll();
	System.out.println(users);
	return users;
    }

    @RequestMapping(value = "/poll")
    public @ResponseBody String poll() throws Exception {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	try {
	    HttpGet  httpget = new HttpGet("https://www.okcoin.com/api/ticker.do?symbol=ltc_cny");
	    ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
	    };
	    String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
	    return responseBody;
	}
	finally {
	    httpclient.close();
	}
    }
}
