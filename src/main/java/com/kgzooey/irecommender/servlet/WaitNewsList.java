package com.kgzooey.irecommender.servlet;

import com.kgzooey.irecommender.models.WaitNews;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/WaitNewsList")
public class WaitNewsList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            String newsId = request.getParameter("newsId");
            String sql = "INSERT INTO read_record VALUES("+userId+","+newsId+","+"0,"+"2"+")";
            DBUtil.executeUpdata(sql);
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<WaitNews> list = new ArrayList<WaitNews>();
            String userId = request.getParameter("userId");
            String sql = "SELECT read.newsId FROM read"
                    +" WHERE read.userId="+userId
                    +" AND readStatus=2";
            ResultSet resultSet1 = DBUtil.executeQuery(sql);
            while (resultSet1.next()){
                WaitNews waitNews = new WaitNews();
                waitNews.setNewsId(resultSet1.getInt("newsId"));
                list.add(waitNews);
            }

            for (int i = 0; i < list.size(); i++){
                sql = "SELECT newsTitle FROM news"
                        +" WHERE news.newsId="+list.get(i).getNewsId();
                ResultSet resultSet2 = DBUtil.executeQuery(sql);
                int j = 0;
                while (resultSet2.next()){
                    list.get(j).setNewsTitle(resultSet2.getString("newsTitle"));
                    j++;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
