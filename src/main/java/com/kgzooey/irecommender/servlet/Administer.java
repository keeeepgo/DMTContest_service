package com.kgzooey.irecommender.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;

@WebServlet("/Administer")
public class Administer extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adminId = request.getParameter("adminId");
        String adminPassword = request.getParameter("adminPassword");
        String sql = "SELECT adminId,adminPassword FROM administer WHERE adminId="+adminId+" AND adminPassword='"+adminPassword+"'";
        try{
            ResultSet resultSet = DBUtil.executeQuery(sql);
            if(resultSet.next()){
                HttpSession session = request.getSession(true);
                session.setAttribute("adminId",adminId);
                response.getWriter().write("登录成功");
                response.sendRedirect("http://localhost:8080/web_admin/admin_news.html");
            }else{
                response.getWriter().write("登录失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);
        String adminId = (String)session.getAttribute("adminId");

        response.setHeader("Content-type", "application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");
        response.getWriter().write(adminId);
    }
}
