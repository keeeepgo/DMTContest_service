package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet("/UserTagList")
public class UserTagList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            String userId = request.getParameter("userId");
            String tagContent = request.getParameter("tagContent");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(Calendar.getInstance().getTime());

            String sql_result = "上传失败";
            String sql =  "INSERT INTO tag(tagContent,tagDate) VALUES( " +"'"+ tagContent+"'"+",'"+today+"');";
            if(DBUtil.executeUpdata(sql)==1){
                sql = "SELECT tagId FROM tag WHERE tagContent='"+ tagContent+"';";
                ResultSet resultSet2 = DBUtil.executeQuery(sql);
                resultSet2.next();
                int tagId = resultSet2.getInt("tagId");
                sql = "INSERT INTO user_tag(userId,tagId,tagWeight) VALUES("+userId+","+tagId+","+"0.1)";
                if(DBUtil.executeUpdata(sql)==1){
                    sql_result = "上传成功";
                }
            }

            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            Writer_response.write(sql_result);
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<TagBean> list = new ArrayList<TagBean>();
            String userId = request.getParameter("userId");
            String sql = "SELECT tag.tagId,tagContent FROM user_tag,tag "
                    + "WHERE user_tag.tagId=tag.tagId "
                    + "AND userId="+userId + " ORDER BY user_tag.tagWeight DESC";

            ResultSet resultSet = DBUtil.executeQuery(sql);

            while (resultSet.next()){
                TagBean temp = new TagBean();
                temp.setTagId(resultSet.getInt("tagId"));
                temp.setTagContent(resultSet.getString("tagContent"));
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
            System.out.println(jsonlist);
            Writer_response.write(jsonlist);
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doDelete(req, resp);
        try {
            String userId = req.getParameter("userId");
            String tagId = req.getParameter("tagId");
            String sql = "DELETE FROM user_tag WHERE userId=" + userId + " AND tagId=" + tagId;
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
