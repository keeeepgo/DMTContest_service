package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.WaitNews;

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
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<WaitNews> list = new ArrayList<WaitNews>();
            String userId = request.getParameter("userId");
            String sql = " SELECT newsId FROM read_record"
                    +" WHERE userId=1"
                    +" AND readStatus=2";
            ResultSet resultSet1 = DBUtil.executeQuery(sql);
            while (resultSet1.next()){
                WaitNews waitNews = new WaitNews();
                waitNews.setNewsId(resultSet1.getInt("newsId"));
                System.out.println(waitNews.getNewsId());
                list.add(waitNews);
            }

            for (int i = 0; i < list.size(); i++){
                sql = " SELECT newsTitle FROM news"
                        +" WHERE news.newsId="+list.get(i).getNewsId();
                ResultSet resultSet2 = DBUtil.executeQuery(sql);
                int j = 0;
                while (resultSet2.next()){
                    list.get(j).setNewsTitle(resultSet2.getString("newsTitle"));
                    System.out.println(resultSet2.getString("newsTitle"));
                    j++;
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
