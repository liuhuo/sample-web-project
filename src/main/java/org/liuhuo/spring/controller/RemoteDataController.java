package org.liuhuo.spring.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.liuhuo.spring.dao.RemoteDataDao;
import org.liuhuo.spring.model.RemoteData;

import java.io.StringWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.liuhuo.spring.model.IspInfo;

import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;



@Controller
public class RemoteDataController {

    @Autowired
    private RemoteDataDao remoteDataDao;

    private static final Logger logger = Logger.getLogger(RemoteDataController.class);
    
    @RequestMapping(value = "/remote/track") 
    public String tracerPage(ModelMap model) {
        return "track";
    }


    @RequestMapping(value = "/remote/track/all",produces="application/json;charset=UTF-8")
    @ResponseBody
    public List<RemoteData> allData(ModelMap model) {
        return remoteDataDao.findAllRecord();
    }


    @RequestMapping(value = "/findisp", method = RequestMethod.GET)
    public @ResponseBody IspInfo findisp(@RequestParam("ip") String ip) {
        IspInfo result;
        result = remoteDataDao.findIsp(ip);
        return result;
    }

    @RequestMapping(value = "/saveremotedata")
    @ResponseBody
    public String savedata(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            String queryString = request.getQueryString();
            sb.append("original query string: ");
            sb.append(queryString);
            sb.append(System.getProperty("line.separator"));
            byte tmpByteArr[] = queryString.getBytes("ISO-8859-1");
            Base64 decoder = new Base64();
            byte[] decodedBytes = decoder.decodeBase64(tmpByteArr);
            queryString = new String(decodedBytes, "UTF-8");
            sb.append("after url decode: ");
            sb.append(queryString);
            sb.append(System.getProperty("line.separator"));
            URLDecoder urlDecoder = new URLDecoder();
            queryString = urlDecoder.decode(queryString, "UTF-8");
            sb.append("after base64 decode: ");
            sb.append(queryString);
            sb.append(System.getProperty("line.separator"));
            Map<String,String> queryMap = new HashMap<>();
            String[] queryParts = queryString.split("&");
            for (String query : queryParts) {
                System.out.println(query);
                String[] tmp = query.split("=");
                queryMap.put(tmp[0],tmp[1]);
            }

            String eventSource = queryMap.get("event_source_ip");
            String datetimeStr = queryMap.get("datetime");
            String serviceName = queryMap.get("service_name");
            String eventType = queryMap.get("event_type");
            String testFlag = queryMap.get("is_test");
            boolean isTest = testFlag == null ? true : Boolean.parseBoolean(testFlag);
            int eventCount = Integer.parseInt(queryMap.get("event_count"));
            if (eventSource == null || datetimeStr == null || serviceName == null || eventType == null) {
                throw new Exception("some parameter is null");
            }
            logger.info("get parameters: " + "event_source_ip:" + eventSource + "&date:" + datetimeStr + "&service_name:" + serviceName + "&event_type:" + eventType + "&event_count:" + eventCount);
            IspInfo result = remoteDataDao.findIsp(eventSource);
            int id = (result==null)?remoteDataDao.insertIspInfo(eventSource):result.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(datetimeStr);
            remoteDataDao.insertRemoteData(id,date,eventType,serviceName,eventCount,isTest);
            return "{\"status\": \"ok\"}";
        }
        catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            // sb.append(sw.toString());
            logger.info(sw.toString());

            remoteDataDao.insertRemoteData(sb.toString());
            return "{\"status\": \"fail\",\"reason\":\"there are most likely some data format errors\"}";
        }
    }
}
 
