package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.UserBean;

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

@WebServlet("/AllUsers")
public class AllUsers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-type", "application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication");

        String userId = request.getParameter("userId");
        if(userId == null) {
            try {
                String sql = "SELECT userId,userName,registerDate,loginDate FROM user";
                ResultSet resultSet = DBUtil.executeQuery(sql);
                List<UserBean> list = new ArrayList<UserBean>();
                while (resultSet.next()) {
                    UserBean temp = new UserBean();
                    temp.setUserId(resultSet.getInt("userId"));
                    temp.setUserName(resultSet.getString("userName"));
                    temp.setRegisterDate(resultSet.getDate("registerDate"));
                    temp.setLoginDate(resultSet.getDate("loginDate"));
                    list.add(temp);
                }
                ObjectMapper mapper = new ObjectMapper();

                //Java集合转JSON
                String jsonlist = "{\"code\": 0,\"msg\":\"\",\"count\": "+list.size()+",\"data\":" + mapper.writeValueAsString(list) + "}";
                response.getWriter().write(jsonlist);
                DBUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
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
                String json =  mapper.writeValueAsString(userBean);

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
            String userId = req.getParameter("userId");
            String sql = "DELETE FROM user WHERE userId=" + userId;
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
