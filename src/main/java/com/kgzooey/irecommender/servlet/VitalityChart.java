package com.kgzooey.irecommender.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.Vitality;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/VitalityChart")
public class VitalityChart extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            String sql = "SELECT date,newsNumber FROM vitality WHERE userId=" + userId;
            ResultSet resultSet = DBUtil.executeQuery(sql);
            //获取今天的时间,设定开始/结束日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH) + 1;
            String time;
            if(month<10) {
                time = year + "-0" + month + "-" + day;
            }else {
                time = year + "-" + month + "-" + day;
            }
            Date d1 = sdf.parse(year+"-01-01");//定义起始日期
            Date d2 = sdf.parse(time);//定义结束日期
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间

            List<Vitality> list = new ArrayList<Vitality>();
            //数据库获取了数据则开始比较
            if(resultSet.next()){
                while(dd.getTime().before(d2)){//判断是否到结束日期
                    String timeEnd = sdf.format(dd.getTime());
                    Vitality vitality = new Vitality();
                    vitality.setDate(timeEnd);
                    int newsNumber;
                    //对比数据库获取的数据
                    if (resultSet.getDate("date").equals(timeEnd)){
                        newsNumber = resultSet.getInt("newsNumber");
                        vitality.setNewsNumber(newsNumber);
                        resultSet.next();
                    }else {
                        vitality.setNewsNumber(0);
                    }
                    list.add(vitality);
                    dd.add(Calendar.DATE, 1);//进行当前日期加1
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            //Java集合转JSON
            String jsonlist = mapper.writeValueAsString(list);
            System.out.print(jsonlist);
            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            System.out.println(jsonlist);
            Writer_response.write(jsonlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
