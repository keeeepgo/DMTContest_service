package com.kgzooey.irecommender.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgzooey.irecommender.models.NoteBean;

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

@WebServlet("/NoteList")
public class NoteList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            String userId = request.getParameter("userId");
            String noteDate = request.getParameter("noteDate");
            String noteContent = request.getParameter("noteContent");
            System.out.println(userId);
            String sql =  "INSERT INTO note(userId,noteDate,noteContent) VALUES( " +userId+" , "+" '"+ noteDate+"'"+" ,'"+ noteContent+"'"+");";
            System.out.println(sql);

            response.setHeader("Content-type", "text/html; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Authentication");
            PrintWriter Writer_response = response.getWriter();

            if(DBUtil.executeUpdata(sql) == 1){
                System.out.println("上传成功");
                Writer_response.write("上传成功");
            }else{
                Writer_response.write("上传失败");
            }

            DBUtil.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<NoteBean> list = new ArrayList<NoteBean>();
            String userId = request.getParameter("userId");
            String sql = " SELECT noteId,noteDate,noteContent FROM note "
                    + " WHERE userId="+userId + " ORDER BY noteDate";

            ResultSet resultSet = DBUtil.executeQuery(sql);

            while (resultSet.next()){
                NoteBean temp = new NoteBean();
                temp.setNoteId(resultSet.getInt("noteId"));
                temp.setNoteDate(resultSet.getDate("noteDate"));
                temp.setNoteContent(resultSet.getString("noteContent"));
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

}
