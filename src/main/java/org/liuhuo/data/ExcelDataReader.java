package org.liuhuo.data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;

import java.io.FileInputStream;
import java.io.File;

import java.util.Iterator;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Properties;

public class ExcelDataReader {
    
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");

        Properties connectionProps = new Properties();
        connectionProps.put("user", "liuhuo");
        connectionProps.put("password", "cipher");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/playground",connectionProps);
        System.out.println("Connected to database");

        FileInputStream file = new FileInputStream(new File("/home/liuhuo/Downloads/data-november.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(41);
        //32-39 40 41
        String isp;
        String website;
        Date date;
        int pv_push = -1;

        // System.out.println(sheet.getPhysicalNumberOfRows());
        // System.out.println(sheet.getLastRowNum());
        int days = 30, rows = sheet.getLastRowNum();
        Iterator<Row> rowIter = sheet.iterator();
        Row row = rowIter.next();
        Iterator<Cell> cellIter = row.cellIterator();
        Cell cell = cellIter.next();
        isp = cell.getStringCellValue();
        // System.out.println("isp " + isp);
        rowIter.next();
        rowIter.next();
        for (int r = 0; r < rows - 6 + 1; r++) {
            row = rowIter.next();
            cellIter = row.iterator();
            cell = cellIter.next();
            website = cell.getStringCellValue();
            // System.out.print(website + ": ");
            // System.out.println();
            for (int i = 0; i < days; i++) {
                Calendar c = new GregorianCalendar();
                c.set(2013,10,i+1,0,0,0);
                //System.out.println(c.getTime() + " " + (i+1));
                cell = cellIter.next();
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    pv_push = (int)cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    pv_push = 0;
                    break;
                }
                String insertSql = "insert into pv_record (isp, website,record_date,pv_push) values (?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(insertSql);
                stmt.setString(1,isp);
                stmt.setString(2,website);
                stmt.setDate(3,new java.sql.Date(c.getTimeInMillis()));
                stmt.setInt(4,pv_push);
                stmt.executeUpdate();
                System.out.println(isp + " " + website + " " + c.getTime() + " " + pv_push);
                
                // System.out.print(pv_push);
                // System.out.print(" ");
                // //System.out.println(i+1);

                // //System.out.println(c.getTime());
            }
            // System.out.println();
            // System.out.println();
        }
        conn.close();
    }
}
