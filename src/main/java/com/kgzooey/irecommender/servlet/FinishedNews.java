package com.kgzooey.irecommender.servlet;

import com.sun.rowset.CachedRowSetImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;


@WebServlet("/FinishedNews")
public class FinishedNews extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //增加阅读记录
        String userId = request.getParameter("userId");
        String newsId = request.getParameter("newsId");
        try {

            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            String time;
            if(month<10) {
                time = year + "-0" + month + "-" + day;
            }else {
                time = year + "-" + month + "-" + day;
            }
            System.out.print("当前时间:" + time);
            String sql = "SELECT date FROM vitality WHERE date =" + time;
            ResultSet resultSet = DBUtil.executeQuery(sql);
            System.out.print("当前时间搜索的返回：" + resultSet);
            if (resultSet.next()) {
                //如果有今天的记录,增加阅读数
                sql = "SELECT newsNumber FROM vitality WHERE userId=" + userId;
                ResultSet resultSet1 = DBUtil.executeQuery(sql);
                if (resultSet1.next()) {
                    int newsNumber = resultSet1.getInt("newsNumber") + 1;
                    sql = "UPDATE vitality SET newsNumber="+newsNumber;
                    int test = DBUtil.executeUpdata(sql);
                    if (test == 1){
                        System.out.print("修改成功");
                    }
                }

            } else {
                //没有今天的记录，增加记录然后增加阅读数
                sql = "INSERT INTO vitality" + " VALUES(" + userId + "," + time + "," + "1)";
                int test = DBUtil.executeUpdata(sql);
                if (test == 1)
                    System.out.print("插入数据成功");
            }

            DBUtil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更改阅读状态
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
