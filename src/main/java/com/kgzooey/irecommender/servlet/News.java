package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.NewsBean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class News extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            int newsId = Integer.parseInt(request.getParameter("newsId"));
            String sql = "SELECT newsTitle,newsContent FROM news" + "WHERE newsId="+newsId;
            ResultSet resultSet = DBUtil.executeQuery(sql);
            NewsBean newsBean = new NewsBean();
            while (resultSet.next()){
                newsBean.setNewsId(newsId);
                newsBean.setNewsTitle(resultSet.getString("newsTitle"));
                newsBean.setNewsContent(resultSet.getString("newsContent"));
            }

            ObjectMapper mapper = new ObjectMapper();

            //Java集合转JSON
            String json = mapper.writeValueAsString(newsBean);
            System.out.print(json);
            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            System.out.println(json);
            Writer_response.write(json);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
