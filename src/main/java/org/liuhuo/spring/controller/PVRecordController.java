package org.liuhuo.spring.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.util.Date;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;

import org.liuhuo.spring.dao.PVRecordDao;

import org.liuhuo.spring.model.PVRecord;
import org.liuhuo.spring.model.ISPDailyRecord;
import org.liuhuo.spring.model.Tmp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

@Controller
public class PVRecordController {
    @Autowired
    private PVRecordDao pvrecordDao;

    private static final Logger logger = Logger.getLogger(PVRecordController.class);


    @RequestMapping(value = "/dates", method = RequestMethod.GET)
    public @ResponseBody List<java.sql.Date> alldates() {
        List<java.sql.Date> dates = pvrecordDao.selectDate();
        // System.out.println(dates);
        return dates;
    }

    @RequestMapping(value = "/clicks", method = RequestMethod.GET)
    public @ResponseBody List<ISPDailyRecord> dailyClicks() {
        // System.out.println("----------------------");
        List<ISPDailyRecord> clicks = pvrecordDao.ispDailyPush();
        // System.out.println(clicks);
        return clicks;
    }

    @RequestMapping(value = "/rangedate", method = RequestMethod.GET)
    public @ResponseBody List<java.sql.Date> rangedate(@RequestParam String from, @RequestParam String to)  {
        List<java.sql.Date> dates = pvrecordDao.rangeDate(from,to);
        return dates;
    }
    
    @RequestMapping(value = "/rangeclick", method = RequestMethod.GET)
    public @ResponseBody List<ISPDailyRecord> rangeclick(@RequestParam String from, @RequestParam String to)  {
        List<ISPDailyRecord> records = pvrecordDao.ispRangePush(from,to);
        return records;
    }

    @RequestMapping(value = "/names",produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> names() {
        // List<TmpRecord> results = new ArrayList<>();
        // for (String str : pvrecordDao.serviceNames()) {
        //     TmpRecord tmp = new TmpRecord(str,new ArrayList());
        //     tmp.tokens.add(str);
        //     results.add(tmp);
        // }
        return pvrecordDao.serviceNames();
        // StringBuilder sb = new StringBuilder("[");
        // for (String str : pvrecordDao.serviceNames()) {
        //     sb.append("{");
        //     sb.append("\"value\": \"");
        //     sb.append(str+"\"");
        //     sb.append(",");
        //     sb.append("\"tokens\":");
        //     sb.append("[\"" + str + "\"]");
        //     sb.append("}");
        //     sb.append(",");
        // }
        // sb.setLength(sb.length() - 1);
        // sb.append("]");
        // System.out.println(sb.toString());
        // return sb.toString();

    }

    @RequestMapping(value = "/isps",
                    produces="application/json;charset=UTF-8")
    public @ResponseBody List<String> ispNames() {
        return pvrecordDao.ispNames();
    }

    @RequestMapping(value = "/ispservices",
                    produces="application/json;charset=UTF-8")
        public @ResponseBody List<String> serviceNames(@RequestParam String isp) throws Exception{
        byte tmpByteArr[];
        tmpByteArr = isp.getBytes("ISO-8859-1");
        isp = new String(tmpByteArr, "UTF-8");
        logger.info("got parameter isp:" +isp);
        System.out.println(pvrecordDao.serviceNames(isp));
        return pvrecordDao.serviceNames(isp);
    }

    @RequestMapping(value = "selected_service",
                    produces="application/json;charset=UTF-8")
        public @ResponseBody List<ISPDailyRecord> selectedServiceNames(@RequestParam("services") String services) throws Exception{
        byte tmpByteArr[];
        tmpByteArr = services.getBytes("ISO-8859-1");
        services = new String(tmpByteArr, "UTF-8");
        logger.info("got parameter services:" + services);
        String[] params = services.split("#");
        List<ISPDailyRecord> result = pvrecordDao.selectedServicePush(params);
        System.out.println(result);
        return result;
    }


    @RequestMapping(value = "/services",produces="application/json")
    public @ResponseBody String services() {
        // return "[{\"title\":\"web development\",\"price\":200}]";
        return "[{\"title\":\"web development\",\"price\":200},{\"title\":\"web design\",\"price\":250}]";
    }

    @RequestMapping(value = "/service/{id}",produces="application/json")
    public @ResponseBody String services(@PathVariable String id) {
        System.out.println(id);
        return "{\"title\":\"custom service 1\",\"price\":110}";
    }

    @RequestMapping(value = "/dummysave") 
    public String dummySave(@ModelAttribute Tmp tmp) {
        System.out.println(tmp);
        return "hello";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveRecord(@ModelAttribute("pv") PVRecord pvrecord,ModelMap model) {
        System.out.println(pvrecord);
        System.out.println("from model " + model.get("pv"));
        return "hello";
    }
    
    @RequestMapping(value = "/remote", method = RequestMethod.POST)
    public String remoteData(HttpServletRequest request) {
        System.out.println("behold, a log is comming");
        logger.info("I am a debug msg");
        StringBuffer url = request.getRequestURL();
        logger.info(url.toString());
        Enumeration<String> names = request.getParameterNames();
        StringBuffer params = new StringBuffer();
        while (names.hasMoreElements()) {
            String paramName = names.nextElement();
            String paramValue = request.getParameter(paramName);
            params.append(paramName);
            params.append("=");
            params.append(paramValue);
            params.append(";");

        }
        params.setLength(params.length()-1);
        logger.info(params.toString());
        return "hello";
    }

    
}

class TmpRecord {
    String value;
    List<String> tokens;

    public TmpRecord(String value , List<String> tokens) {
        this.value = value;
        this.tokens = new ArrayList<>();
        for (String t : tokens) {
            tokens.add(t);
        }
    }

    public String getValue() {
        return this.value;
    }

    public List<String> getTokens() {
        return this.tokens;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = new ArrayList<>();
        for (String t : tokens) {
            this.tokens.add(t);
        }
    }
}
