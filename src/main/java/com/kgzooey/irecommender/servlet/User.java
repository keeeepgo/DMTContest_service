package com.kgzooey.irecommender.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet("/User")
public class User extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                String sql = "SELECT * FROM user WHERE userName = "+userName+" AND userPassword = "+userPassword;
                ResultSet resultSet = DBUtil.executeQuery(sql);
                if(resultSet.next()){
                    sql = "UPDATE user SET loginDate='"+today+"'";
                    DBUtil.executeUpdata(sql);
                    response.getWriter().write("登录成功");
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
                String sql = "INSERT INTO user(userName,userPassword,registerDate,loginDate) VALUES ( "+userName+" , "+userPassword+" , '"+today+"','"+today+"') ";
                System.out.println(sql);
                if(DBUtil.executeUpdata(sql) == 1){
                    response.getWriter().write("注册成功");
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
        String userId = request.getParameter("userId");
        try{
            String sql = "SELECT userId,userName,registerDate,loginDate FROM user WHERE userId="+userId;
            ResultSet resultSet = DBUtil.executeQuery(sql);
            UserBean userBean = new UserBean();
            if (resultSet.next()) {
                userBean.setUserId(resultSet.getInt("userId"));
                userBean.setUserName(resultSet.getString("userName"));
                userBean.setRegisterDate(resultSet.getDate("registerDate"));
                userBean.setLoginDate(resultSet.getDate("loginDate"));
            }

            ObjectMapper mapper = new ObjectMapper();

            //Java集合转JSON
            String json = mapper.writeValueAsString(userBean);
            response.setHeader("Content-type", "application/json; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            response.getWriter().write(json);
            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
