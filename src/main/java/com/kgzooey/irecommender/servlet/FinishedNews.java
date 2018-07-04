package com.kgzooey.irecommender.servlet;

import com.sun.rowset.CachedRowSetImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@WebServlet("/FinishedNews")
public class FinishedNews extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String newsId = request.getParameter("newsId");
        String grade = request.getParameter("newsGrade");
        try {
            //对文章阅读数的处理
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            String time;
            if(month<10) {
                time = year + "-0" + month + "-" + day;
            }else if (day<10){
                time = year + "-" + month + "-0" + day;
            }else{
                time = year + "-" + month + "-0" + day;
            }
            System.out.println("当前时间:" + time);
            String sql = "SELECT * FROM vitality WHERE date =" +'"'+ time +'"';
            ResultSet resultSet = DBUtil.executeQuery(sql);
            if (resultSet.next()) {
                System.out.println("有今天的时间记录" );
                //如果有今天的记录,增加阅读数
                int newsAmount = resultSet.getInt("newsAmount") + 1;
                System.out.println("newsAmount" + newsAmount);
                sql = "UPDATE vitality SET newsAmount="+newsAmount;
                int test = DBUtil.executeUpdata(sql);
                if (test>0){
                    System.out.print("修改成功"); }
            } else {
                System.out.println("没有今天的时间记录" );
                //没有今天的记录，增加记录然后增加阅读数
                sql = "INSERT INTO vitality" + " VALUES(" + userId + "," +'"'+ time +'"'+ "," + "1)";
                System.out.print(sql);
                int test = DBUtil.executeUpdata(sql);
                if (test >0) {
                    System.out.print("插入数据成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更改阅读状态和评分
        try{
            String sql = "UPDATE read_record SET readStatus=2, grade="+grade
                    +" WHERE userId="+userId
                    +" AND newsId="+newsId;
            int test= DBUtil.executeUpdata(sql);
            if (test>0){
                response.setHeader("Content-type", "text/html; charset=utf-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "Authentication");
                PrintWriter Writer_response = response.getWriter();
                Writer_response.write("成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
