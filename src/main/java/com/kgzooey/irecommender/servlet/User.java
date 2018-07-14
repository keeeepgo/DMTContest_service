package com.kgzooey.irecommender.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet("/User")
public class User extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String actionType = request.getParameter("actionType");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(Calendar.getInstance().getTime());

        response.setHeader("Content-type", "text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");

        if (actionType.equals("login")) {
            try{
                String sql = "SELECT * FROM user WHERE "+
                        " userId = "+userId+" AND userPassword = '"+userPassword+"'";
                ResultSet resultSet = DBUtil.executeQuery(sql);
                if(resultSet.next()){
                    sql = "UPDATE user SET loginDate='"+today+"'WHERE userId="+userId;
                    DBUtil.executeUpdata(sql);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userId",userId);
                    response.getWriter().write("登录成功");
                    response.sendRedirect("http://localhost:8080/web_user/homepage.html");
                }else{
                    response.getWriter().write("登录失败");
                }
                DBUtil.close();
            }catch (Exception e){
                e.printStackTrace();
                response.getWriter().write("登录失败");
            }
        }else if(actionType.equals("signup")){
            try{
                String sql = "INSERT INTO user(userId,userName,userPassword,registerDate,loginDate) VALUES ( "+userId+", '"+userName+"' , '"+userPassword+" ', '"+today+"','"+today+"') ";
                System.out.println(sql);
                if(DBUtil.executeUpdata(sql) == 1){
                    sql = "UPDATE user SET loginDate='"+today+"'WHERE userId="+userId;
                    DBUtil.executeUpdata(sql);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userId",userId);
                    response.getWriter().write("注册成功");
                    response.sendRedirect("http://localhost:8080/web_user/homepage.html");
                }else{
                    response.getWriter().write("注册失败");
                }
                DBUtil.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);
        String userId = (String)session.getAttribute("userId");

        response.setHeader("Content-type", "application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
        response.getWriter().write(userId);
    }
}
