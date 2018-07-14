package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.NewsBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet("/AllNews")
public class AllNews extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");

            String newsId = request.getParameter("newsId");
            String newsTitle = request.getParameter("newsTitle");
            String newsUrl = request.getParameter("newsUrl");
            String newsContent = request.getParameter("newsContent");
            String newsDate = request.getParameter("newsDate");

            String sql_result = "上传失败";
            String sql = "UPDATE news SET newsUrl='"+newsUrl+"' ,newsTitle=' "+newsTitle+"' ,newsContent='"+newsContent+"' ,newsDate='"+newsDate+"'  WHERE newsId = "+newsId;
            System.out.println(sql);
            if(DBUtil.executeUpdata(sql)==1){
                sql_result = "上传成功";
            }

            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            response.getWriter().write(sql_result);
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-type", "application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");

        String newsId = request.getParameter("newsId");
        if(newsId == null) {
            try {
                String sql = "SELECT newsId,newsUrl,newsTitle,newsContent,newsDate FROM news ";
                ResultSet resultSet = DBUtil.executeQuery(sql);
                List<NewsBean> list = new ArrayList<NewsBean>();
                while (resultSet.next()){
                    NewsBean newsBean = new NewsBean();
                    newsBean.setNewsId(resultSet.getInt("newsId"));
                    newsBean.setNewsUrl(resultSet.getString("newsUrl"));
                    newsBean.setNewsTitle(resultSet.getString("newsTitle"));
                    newsBean.setNewsContent(resultSet.getString("newsContent"));
                    newsBean.setNewsDate(resultSet.getDate("newsDate"));
                    list.add(newsBean);
                }
                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String jsonlist = "{\"code\": 0,\"msg\":\"\",\"count\": "+list.size()+",\"data\":" + mapper.writeValueAsString(list) + "}";
                response.setHeader("Content-type", "application/json; charset=utf-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "Authentication");
                response.getWriter().write(jsonlist);
                DBUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                String sql = "SELECT newsId,newsUrl,newsTitle,newsContent,newsDate FROM news WHERE newsId="+newsId;
                ResultSet resultSet = DBUtil.executeQuery(sql);
                NewsBean newsBean = new NewsBean();
                if(resultSet.next()){
                    newsBean.setNewsId(resultSet.getInt("newsId"));
                    newsBean.setNewsUrl(resultSet.getString("newsUrl"));
                    newsBean.setNewsTitle(resultSet.getString("newsTitle"));
                    newsBean.setNewsContent(resultSet.getString("newsContent"));
                    newsBean.setNewsDate(resultSet.getDate("newsDate"));
                }
                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String json = mapper.writeValueAsString(newsBean);
                response.getWriter().write(json);
                DBUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doDelete(req, resp);
        try {
            String newsId = req.getParameter("newsId");
            String sql = "DELETE FROM news WHERE newsId=" + newsId;
            int temp = DBUtil.executeUpdata(sql);
            resp.setHeader("Content-type", "text/html; charset=utf-8");
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Access-Control-Allow-Headers", "Authentication");
            if (temp < 1) {
                resp.getWriter().write("删除成功");
            } else {
                resp.getWriter().write("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
