package org.liuhuo.data;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Properties;

public class RandomGenerator {

    public static double nextTime(double rate) {
        return -Math.log(1.0 - Math.random()) / rate;
    }

    public static void simulate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2013,Calendar.NOVEMBER,1,0,0,0);
        //System.out.println(cal.getTimeInMillis());
        long start = cal.getTimeInMillis();
        System.out.println("start at " + cal.getTime());
        cal.set(2013,Calendar.NOVEMBER,2,0,0,59);
        long end = cal.getTimeInMillis();
        long next = start;
        
        Properties connectionProps = new Properties();
        connectionProps.put("user", "liuhuo");
        connectionProps.put("password", "cipher");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/playground",connectionProps);
        System.out.println("Connected to database");

        String insertSql = "insert into remote_push_data (source,isp,service_name, event_type, amount,event_time, test, record_time) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(insertSql);

        Date d = new Date();
        while (next < end) {
            int event = 0;
            double elapse = 0.0;
            while (elapse < 1.0) {
                elapse += nextTime(100.0);
                event += 1;
            }
            // System.out.print(event + " number of events happens in " + elapse + " seconds ");
            next = next + (int) (elapse * 1000);
            // d.setTime(next);
            stmt.setString(1,"localhost");
            stmt.setString(2, "dummy isp");
            stmt.setString(3, "dummy service");
            stmt.setString(4, "pv");
            stmt.setInt(5,event);
            stmt.setTimestamp(6, new java.sql.Timestamp(next));
            stmt.setInt(7,0);
            stmt.setTimestamp(8,new java.sql.Timestamp(new Date().getTime()));
            stmt.executeUpdate();

            // System.out.println("at " + d );

        }
    }


    public static void main(String[] args) throws Exception {
        simulate();
        System.out.println();

        double sum = 0.0;
        for (int i = 0; i < 10000; i++) {
            sum += nextTime(100.0);
        }
        System.out.println(sum/10000);
    }
}
