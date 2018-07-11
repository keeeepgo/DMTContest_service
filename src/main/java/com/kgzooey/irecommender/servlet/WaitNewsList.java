package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.WaitNewsBean;

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
        String userId = request.getParameter("userId");
        String newsId = request.getParameter("newsId");
        String sql_result = "添加失败";
        String sql = "SELECT * FROM read_record WHERE userId= "+userId+" AND newsId= "+newsId;
        try {
            ResultSet resultSet = DBUtil.executeQuery(sql);
            if(resultSet.next()){
                sql = " UPDATE read_record SET readStatus=1"
                        +" WHERE userId= "+userId+" AND newsId= "+newsId;
                if(DBUtil.executeUpdata(sql)==1) {
                    sql_result = "添加成功";
                }
            }else{
                sql = "INSERT INTO read_record VALUES("+userId+","+newsId+",3,1"+")";
                if(DBUtil.executeUpdata(sql)==1) {
                    sql_result = "添加成功";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        DBUtil.close();
        response.setHeader("Content-type", "text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
        response.getWriter().write(sql_result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<WaitNewsBean> list = new ArrayList<WaitNewsBean>();
            String userId = request.getParameter("userId");
            String sql = " SELECT newsTitle,news.newsId FROM read_record,news"
                    +" WHERE read_record.newsId=news.newsId"
                    +" AND readStatus=1"
                    +" AND userId="+userId;
            ResultSet resultSet1 = DBUtil.executeQuery(sql);
            while (resultSet1.next()){
                WaitNewsBean waitNewsBean = new WaitNewsBean();
                waitNewsBean.setNewsId(resultSet1.getInt("newsId"));
                waitNewsBean.setNewsTitle(resultSet1.getString("newsTitle"));
                list.add(waitNewsBean);
            }

            ObjectMapper mapper = new ObjectMapper();

            //Java集合转JSON
            String jsonlist = mapper.writeValueAsString(list);
            //System.out.print(jsonlist);
            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            response.getWriter().write(jsonlist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
