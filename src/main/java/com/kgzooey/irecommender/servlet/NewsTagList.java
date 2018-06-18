package com.kgzooey.irecommender.servlet;

        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.kgzooey.irecommender.models.NewsTag;
        import com.kgzooey.irecommender.models.Note;
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

@WebServlet("/NewsTagList")
public class NewsTagList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<NewsTag> list = new ArrayList<NewsTag>();
            String newsId = request.getParameter("newsId");
            String sql = " SELECT tag.tagId,tag.tagContent FROM tag,news_tag "
                    + " WHERE tag.tagId=news_tag.tagId AND newsId="+newsId;

            ResultSet resultSet = DBUtil.executeQuery(sql);

            while (resultSet.next()){
                NewsTag temp = new NewsTag();
                temp.setTagId(resultSet.getInt("tagId"));
                temp.setTagContent(resultSet.getString("tagContent"));
                list.add(temp);
                System.out.println(temp.getTagContent());
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

