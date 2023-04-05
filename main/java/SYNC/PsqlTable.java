package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;


@WebServlet("/PsqlTable")
public class PsqlTable extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
	     String database =request.getParameter("psqldbname");
	     System.out.println(database);
	     sess.setAttribute("setpsqldatabase", database);
	     String ip = "localhost";
	     String name = (String)sess.getAttribute("username");
	     String password = (String)sess.getAttribute("password");
			 
	     try {
	  	   
	    	 Class.forName("org.postgresql.Driver");
	       	Connection con = DriverManager.getConnection("jdbc:postgresql://"+ip+":5432/" + database, name, password);
	       	Statement st = con.createStatement();
	        ResultSet rs = st.executeQuery("SELECT tablename FROM pg_tables WHERE schemaname = 'public';");
	        ResultSetMetaData rsmd = rs.getMetaData();
	        int columnsNumber = rsmd.getColumnCount();

	        JSONObject tablelist = new JSONObject();
	        int count=0;
	        while (rs.next()) {
	        	count+=1;
	                for (int i = 1; i <= columnsNumber; i++) {

	                        String columnValue = rs.getString(i);
	                        System.out.println(columnValue);
	                        tablelist.put("tables"+count,columnValue);
	                        
	                   }
	        
	 
	     }


	PrintWriter printWriter = response.getWriter();
	printWriter.println(tablelist);
	        

	        
	     }
	       
	        
			
		 catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			System.out.println("error2");
		}
	}

}
