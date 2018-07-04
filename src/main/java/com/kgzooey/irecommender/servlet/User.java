package com.kgzooey.irecommender.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/User")
public class User extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        try{
            String sql = "INSERT INTO user(userName,userPassword) VALUES ( "+userName+" , "+userPassword+" ) ";
            if(DBUtil.executeUpdata(sql) == 1){
                response.setHeader("Content-type", "text/html; charset=utf-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "Authentication");
                response.getWriter().write("success");
            }else{
                response.setHeader("Content-type", "text/html; charset=utf-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "Authentication");
                response.getWriter().write("failed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
