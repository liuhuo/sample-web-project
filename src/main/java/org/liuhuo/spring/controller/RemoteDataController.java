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
import org.liuhuo.spring.model.ISPDailyRecord;

import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
public class RemoteDataController {

    @Autowired
    private RemoteDataDao remoteDataDao;

    private static final Logger logger = Logger.getLogger(RemoteDataController.class);

    @RequestMapping(value = "/remote/track")
    public String tracerPage(ModelMap model) {
        return "track";
    }



    @ResponseBody
    @RequestMapping(value="/remote/track/all", produces="application/json;charset=UTF-8")
    public List<RemoteData> allData(ModelMap model) {
        return remoteDataDao.findAllRecord();
    }

    @RequestMapping(value = "/remote/data/show",produces="application/json;charset=UTF-8")
    public String dataPresentation(ModelMap model) {
        return "presentation";
    }



    @RequestMapping(value = "/remote/7days",produces="application/json;charset=UTF-8")
    @ResponseBody
    public List<RemoteData> find7daysData(ModelMap model) {
        return remoteDataDao.findLast7days();
    }

    @RequestMapping(value = "/remote/isp/service/detail",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<ISPDailyRecord> findServiceDetails(@RequestParam("services") String services,
                                               @RequestParam("days") String days,
                                               @RequestParam("type") String type,
                                               @RequestParam("isp") String isp) {
        // System.out.println(services);
        // System.out.println(days);
        // System.out.println(type);
        // System.out.println(isp);
        type = type.equals("pv") ? "1" : "2";
        List<ISPDailyRecord> result;
        if (days.equals("7")) {
            result = remoteDataDao.findServiceDetails7days(services.split("#"),type,isp);
        }
        else {
            result = remoteDataDao.findServiceDetails30days(services.split("#"),type,isp);
        }
        return result;
    }

    @RequestMapping(value = "/remote/isp/service/month",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<ISPDailyRecord> findServiceDetailsInAMonth(@RequestParam("isp") String isp) {
        List<ISPDailyRecord> result = remoteDataDao.findServiceDetailsInAMonth(isp);
        return result;
    }


    /* get isp names */
    @RequestMapping(value = "/remote/isp/names",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<String> findIspNames(ModelMap model) {
        //System.out.println(remoteDataDao.findIspNames());
        return remoteDataDao.findIspNames();
    }

    /* get service name for a given isp */
    @RequestMapping(value = "/remote/isp/services",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<String> findServiceNames(@RequestParam("isp") String isp) {
        // System.out.println(isp);
        return remoteDataDao.findServiceNamesGivenIsp(isp);
    }


    /* overall pv & uv given range*/
    @RequestMapping(value = "/remote/isp/pv/7days",produces="application/json;charset=UTF-8")
    @ResponseBody
    public List<ISPDailyRecord> findIspPVData7days(ModelMap model) {
        return remoteDataDao.findIspData7days("1");
    }

    @RequestMapping(value = "/remote/isp/uv/7days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<ISPDailyRecord> findIspUVData7days(ModelMap model) {
        return remoteDataDao.findIspData7days("2");
    }

    @RequestMapping(value = "/remote/isp/pv/30days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<ISPDailyRecord> findIspPVData30days(ModelMap model) {
        return remoteDataDao.findIspData30days("1");
    }

    @RequestMapping(value = "/remote/isp/uv/30days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<ISPDailyRecord> findIspUVData30days(ModelMap model) {
        return remoteDataDao.findIspData30days("2");
    }

    /* specific isp stats */
    @ResponseBody
    @RequestMapping(value = "/remote/isp/summary",produces="application/json;charset=UTF-8")
    public String findSpecificIspStats(@RequestParam("isp") String isp) {
        // System.out.println("reach here "  + isp);
        JSONArray result = new JSONArray();

        JSONObject jo1 = new JSONObject();
        JSONObject jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCountToday("1",isp));
        jo2.put("uv",remoteDataDao.findSpecificIspCountToday("2",isp));
        jo1.put("name","今日");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCountYesterday("1",isp));
        jo2.put("uv",remoteDataDao.findSpecificIspCountYesterday("2",isp));
        jo1.put("name","昨日");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount7days("1",isp)/7);
        jo2.put("uv",remoteDataDao.findSpecificIspCount7days("2",isp)/7);
        jo1.put("name","最近7日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount14days("1",isp)/14);
        jo2.put("uv",remoteDataDao.findSpecificIspCount14days("2",isp)/14);
        jo1.put("name","最近14日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount30days("1",isp)/30);
        jo2.put("uv",remoteDataDao.findSpecificIspCount30days("2",isp)/30);
        jo1.put("name","最近30日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount7days("1",isp));
        jo2.put("uv",remoteDataDao.findSpecificIspCount7days("2",isp));
        jo1.put("name","最近7日总量");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount14days("1",isp));
        jo2.put("uv",remoteDataDao.findSpecificIspCount14days("2",isp));
        jo1.put("name","最近14日总量");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findSpecificIspCount30days("1",isp));
        jo2.put("uv",remoteDataDao.findSpecificIspCount30days("2",isp));
        jo1.put("name","最近30日总量");
        jo1.put("data",jo2);
        result.add(jo1);


        return result.toString();
    }

    /* overall stats */
    @ResponseBody
    @RequestMapping(value = "/remote/count/overall",produces="application/json;charset=UTF-8")
    public String findOverallCount(ModelMap model) {
        JSONArray result = new JSONArray();

        JSONObject jo1 = new JSONObject();
        JSONObject jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCountToday("1"));
        jo2.put("uv",remoteDataDao.findOverallCountToday("2"));
        jo1.put("name","今日");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCountYesterday("1"));
        jo2.put("uv",remoteDataDao.findOverallCountYesterday("2"));
        jo1.put("name","昨日");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount7days("1")/7);
        jo2.put("uv",remoteDataDao.findOverallCount7days("2")/7);
        jo1.put("name","最近7日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount14days("1")/14);
        jo2.put("uv",remoteDataDao.findOverallCount14days("2")/14);
        jo1.put("name","最近14日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount30days("1")/30);
        jo2.put("uv",remoteDataDao.findOverallCount30days("2")/30);
        jo1.put("name","最近30日平均");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount7days("1"));
        jo2.put("uv",remoteDataDao.findOverallCount7days("2"));
        jo1.put("name","最近7日总量");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount14days("1"));
        jo2.put("uv",remoteDataDao.findOverallCount14days("2"));
        jo1.put("name","最近14日总量");
        jo1.put("data",jo2);
        result.add(jo1);

        jo1 = new JSONObject();
        jo2 = new JSONObject();
        jo2.put("pv",remoteDataDao.findOverallCount30days("1"));
        jo2.put("uv",remoteDataDao.findOverallCount30days("2"));
        jo1.put("name","最近30日总量");
        jo1.put("data",jo2);
        result.add(jo1);


        return result.toString();
    }

    /* pv & uv count methods */
    @RequestMapping(value = "/remote/count/pv/today",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int findTodayPVCount(ModelMap model) {
        return remoteDataDao.findOverallCountToday("1");
    }

    @RequestMapping(value = "/remote/count/uv/today",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int findTodayUVCount(ModelMap model) {
        return remoteDataDao.findOverallCountToday("2");
    }

    @RequestMapping(value = "/remote/count/pv/yesterday",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int findYesterdayPVCount(ModelMap model) {
        return remoteDataDao.findOverallCountYesterday("1");
    }

    @RequestMapping(value = "/remote/count/uv/yesterday",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int findYesterdayUVCount(ModelMap model) {
        return remoteDataDao.findOverallCountYesterday("2");
    }

    @RequestMapping(value = "/remote/count/pv/7days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find7daysPVCount(ModelMap model) {
        return remoteDataDao.findOverallCount7days("1");
    }

    @RequestMapping(value = "/remote/count/uv/7days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find7daysUVCount(ModelMap model) {
        return remoteDataDao.findOverallCount7days("2");
    }

    @RequestMapping(value = "/remote/count/pv/14days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find14daysPVCount(ModelMap model) {
        return remoteDataDao.findOverallCount14days("1");
    }

    @RequestMapping(value = "/remote/count/uv/14days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find14daysUVCount(ModelMap model) {
        return remoteDataDao.findOverallCount14days("2");
    }

    @RequestMapping(value = "/remote/count/pv/30days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find30daysPVCount(ModelMap model) {
        return remoteDataDao.findOverallCount30days("1");
    }

    @RequestMapping(value = "/remote/count/uv/30days",produces="application/json;charset=UTF-8")
        @ResponseBody
        public int find30daysUVCount(ModelMap model) {
        return remoteDataDao.findOverallCount30days("2");
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
