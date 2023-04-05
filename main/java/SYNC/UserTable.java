package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/usertableget")
public class UserTable extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
	     String database =request.getParameter("dbname");
	     sess.setAttribute("setuserdatabase", database);
	     String ip = (String)sess.getAttribute("userip");
	     String name = (String)sess.getAttribute("UserName");
	     String password = (String)sess.getAttribute("userpass");
	     System.out.println(database);
			 
	     try {
	    	   
	    	   Class.forName("com.mysql.cj.jdbc.Driver");
	         	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" + database, name, password);
	         	Statement st = con.createStatement();
	          ResultSet rs = st.executeQuery("show tables;");
	          ResultSetMetaData rsmd = rs.getMetaData();
	          int columnsNumber = rsmd.getColumnCount();
	          
	          JSONObject usertablelist = new JSONObject();
	          int count=0;
	          while (rs.next()) {
	          	count+=1;
	                  for (int i = 1; i <= columnsNumber; i++) {

	                          String columnValue = rs.getString(i);
	                          usertablelist.put("tables"+count,columnValue);
	                          
	                     }
	          
	   
	       }


	  PrintWriter printWriter = response.getWriter();
	  printWriter.println(usertablelist);
	          

	          
	       }
	        
			
		 catch (Exception e) {
			// TODO: handle exception
			System.out.println("error2");
		}
	}

}
