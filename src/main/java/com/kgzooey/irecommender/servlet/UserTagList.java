package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.UserTag;
import com.kgzooey.irecommender.servlet.DBUtil;

import javax.servlet.Servlet;
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

@WebServlet("/UserTagList")
public class UserTagList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            String tagContent = request.getParameter("tagContent");
            String sql =  "INSERT INTO tag(tagContent) VALUES(" + tagContent+");";
            ResultSet resultSet1 = DBUtil.executeQuery(sql);

            sql = "SELECT tagId FROM WHERE tagContent="+ tagContent+";";
            ResultSet resultSet2 = DBUtil.executeQuery(sql);
            resultSet2.next();
            int tagId = resultSet2.getInt("tagId");

            sql = "INSERT INTO user_tag(userId,tagId,weight) VALUES("+userId+","+tagId+","+"0.1";




            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();
            System.out.println("上传成功");
            Writer_response.write("上传成功");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UserTag> list = new ArrayList<UserTag>();
            String userId = request.getParameter("userId");
            String sql = "SELECT tag.tagId,tagContent FROM user_tag,tag "
                    + "WHERE user_tag.tagId=tag.tagId "
                    + "AND userId="+userId + " ORDER BY user_tag.weight DESC";

            ResultSet resultSet = DBUtil.executeQuery(sql);

            while (resultSet.next()){
                UserTag temp = new UserTag();
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
