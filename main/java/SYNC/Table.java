package SYNC;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;
@WebServlet ("/tableget")
public class Table extends HttpServlet {
	  public void service(HttpServletRequest req,HttpServletResponse res) {
			
		HttpSession sess = req.getSession();
     String database =req.getParameter("dbname");
     System.out.println(database);
     sess.setAttribute("setdatabase", database);
     String ip = (String)sess.getAttribute("ip");
     String name = (String)sess.getAttribute("username");
     String password = (String)sess.getAttribute("password");
		 
     try {
  	   
  	   Class.forName("com.mysql.cj.jdbc.Driver");
       	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" + database, name, password);
       	Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("show tables;");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        
        JSONObject tablelist = new JSONObject();
        int count=0;
        while (rs.next()) {
        	count+=1;
                for (int i = 1; i <= columnsNumber; i++) {

                        String columnValue = rs.getString(i);
                        tablelist.put("tables"+count,columnValue);
                        
                   }
        
 
     }


PrintWriter printWriter = res.getWriter();
printWriter.println(tablelist);
        

        
     }
       
        
		
	 catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
		System.out.println("error2");
	}
	  }

}
