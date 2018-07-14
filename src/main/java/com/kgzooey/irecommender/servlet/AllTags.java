package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.NewsBean;
import com.kgzooey.irecommender.models.TagBean;

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

@WebServlet("/AllTags")
public class AllTags extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");

            String tagContent = request.getParameter("tagContent");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(Calendar.getInstance().getTime());

            String sql_result = "上传失败";
            String sql = "INSERT INTO user(tagContent,tagDate) VALUES ( "+tagContent+" , "+today+"') ";
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

        String userId = request.getParameter("userId");
        String newsId = request.getParameter("newsId");
        String tagId = request.getParameter("tagId");
        if (userId == null && newsId == null && tagId == null) {
            try {
                String sql = "SELECT tagId,tagContent,tagDate FROM tag";
                ResultSet resultSet = DBUtil.executeQuery(sql);
                List<TagBean> list = new ArrayList<TagBean>();
                while (resultSet.next()) {
                    TagBean tagBean = new TagBean();
                    tagBean.setTagId(resultSet.getInt("tagId"));
                    tagBean.setTagContent(resultSet.getString("tagContent"));
                    tagBean.setTagDate(resultSet.getDate("tagDate"));
                    list.add(tagBean);
                }
                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String jsonlist = "{\"code\": 0,\"msg\":\"\",\"count\": " + list.size() + ",\"data\":" + mapper.writeValueAsString(list) + "}";
                System.out.print(jsonlist);

                System.out.println(jsonlist);
                response.getWriter().write(jsonlist);
                DBUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(tagId != null){
            try {
                String sql = "SELECT tagId,tagContent,tagDate FROM tag WHERE tagId="+tagId;
                ResultSet resultSet = DBUtil.executeQuery(sql);
                TagBean tagBean = new TagBean();
                if (resultSet.next()) {
                    tagBean.setTagId(resultSet.getInt("tagId"));
                    tagBean.setTagContent(resultSet.getString("tagContent"));
                    tagBean.setTagDate(resultSet.getDate("tagDate"));
                }
                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String json =  mapper.writeValueAsString(tagBean);
                response.getWriter().write(json);
                DBUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (newsId != null){
            try{
                List<TagBean> list = new ArrayList<TagBean>();
                String sql = "SELECT tag.tagId,tagContent,tagDate FROM news_tag,tag "
                        + "WHERE news_tag.tagId=tag.tagId "
                        + "AND newsId="+newsId;
                ResultSet resultSet = DBUtil.executeQuery(sql);

                while (resultSet.next()){
                    TagBean temp = new TagBean();
                    temp.setTagId(resultSet.getInt("tagId"));
                    temp.setTagContent(resultSet.getString("tagContent"));
                    temp.setTagDate(resultSet.getDate("tagDate"));
                    list.add(temp);
                }

                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String jsonlist = "{\"code\": 0,\"msg\":\"\",\"count\": "+list.size()+",\"data\":" + mapper.writeValueAsString(list) + "}";
                response.getWriter().write(jsonlist);
                DBUtil.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                List<TagBean> list = new ArrayList<TagBean>();
                String sql = "SELECT tag.tagId,tagContent,tagDate FROM user_tag,tag "
                        + "WHERE user_tag.tagId=tag.tagId "
                        + "AND userId="+userId + " ORDER BY user_tag.tagWeight DESC";
                ResultSet resultSet = DBUtil.executeQuery(sql);

                while (resultSet.next()){
                    TagBean temp = new TagBean();
                    temp.setTagId(resultSet.getInt("tagId"));
                    temp.setTagContent(resultSet.getString("tagContent"));
                    temp.setTagDate(resultSet.getDate("tagDate"));
                    list.add(temp);
                }

                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String jsonlist = "{\"code\": 0,\"msg\":\"\",\"count\": "+list.size()+",\"data\":" + mapper.writeValueAsString(list) + "}";
                response.getWriter().write(jsonlist);
                DBUtil.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doDelete(req, resp);
        try {
            String tagId = req.getParameter("tagId");
            String sql = "DELETE FROM tag WHERE tagId=" + tagId;
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
