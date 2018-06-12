package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.RecommendNews;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RecommendNewsList")
public class RecommendNewsList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<RecommendNews> list = new ArrayList<RecommendNews>();
            String userId = request.getParameter("userId");
            String sql = "SELECT news.newsId,newsTitle,newsContent FROM user_news_rec,news"
                    + " WHERE user_news_rec.newsId=news.newsId"
                    + " AND userId="+userId
                    + " ORDER BY user_news_rec.score DESC";
            ResultSet resultSet = DBUtil.executeQuery(sql);
            while (resultSet.next()){
                RecommendNews temp = new RecommendNews();
                temp.setNewsId(resultSet.getInt("newsId"));
                temp.setNewsTitle(resultSet.getString("newsTitle"));
                list.add(temp);
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
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
