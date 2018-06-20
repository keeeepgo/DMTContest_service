package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.WaitNews;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/WaitNewsList")
public class WaitNewsList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            String newsId = request.getParameter("newsId");
            String sql_result = "添加失败";
            String sql = " UPDATE read_record SET readStatus=" +userId
                        +" WHERE userId= "+userId+" AND newsId= "+newsId;
            if(DBUtil.executeUpdata(sql)==1) {
                sql_result = "添加成功";
            }

            DBUtil.close();
            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            Writer_response.write(sql_result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<WaitNews> list = new ArrayList<WaitNews>();
            String userId = request.getParameter("userId");
            String sql = " SELECT newsTitle,news.newsId FROM read_record,news"
                    +" WHERE read_record.newsId=news.newsId"
                    +" AND readStatus=1"
                    +" AND userId="+userId;
            ResultSet resultSet1 = DBUtil.executeQuery(sql);
            while (resultSet1.next()){
                WaitNews waitNews = new WaitNews();
                waitNews.setNewsId(resultSet1.getInt("newsId"));
                waitNews.setNewsTitle(resultSet1.getString("newsTitle"));
                list.add(waitNews);
            }

            ObjectMapper mapper = new ObjectMapper();

            //Java集合转JSON
            String jsonlist = mapper.writeValueAsString(list);
            System.out.print(jsonlist);
            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            Writer_response.write(jsonlist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
