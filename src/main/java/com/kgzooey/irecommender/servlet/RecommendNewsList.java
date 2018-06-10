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
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String uri =  "jdbc:mysql://localhost:3306/irecommender?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";
            String user = "root";
            String password = "root";

            List<RecommendNews> List_RecommendNews = new ArrayList<RecommendNews>(10);

            String userId = request.getParameter("userId");

            Connection con = DriverManager.getConnection(uri, user, password);
            String sql = "SELECT news.newsId,newsTitle,newsContent FROM " +
                    "user_news_rec,news WHERE user_news_rec.newsId = news.newsId " +
                    "AND userId = " + userId;
            Statement sta = con.createStatement();
            ResultSet resultSet = sta.executeQuery(sql);
            while (resultSet.next()){
                RecommendNews temp = new RecommendNews();
                temp.setNewsId(resultSet.getInt("newsId"));
                temp.setNewsTitle(resultSet.getString("newsTitle"));
                List_RecommendNews.add(temp);
            }


            ObjectMapper mapper = new ObjectMapper();

            //Java集合转JSON
            String jsonlist = mapper.writeValueAsString(List_RecommendNews)+"sadasdada";
            response.setCharacterEncoding("UTF-8");
            PrintWriter Writer_response = response.getWriter();
            Writer_response.write(jsonlist);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
