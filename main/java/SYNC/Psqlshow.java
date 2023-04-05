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

/**
 * Servlet implementation class PsqlConnect
 */
@WebServlet("/Psqlshow")
public class Psqlshow extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
	       String ip="";
	       String name="";
	       String password="";
			String MasterIp = (String)sess.getAttribute("MasterIp");
			String MasterUser =(String)sess.getAttribute("MasterUser");
			String MasterPass = (String)sess.getAttribute("MasterPass");
			String getuser= request.getParameter("getUser");
			System.out.println("jjk"+getuser);
	       
	       try {
				Class.forName("com.mysql.cj.jdbc.Driver");
	        	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
	        	Statement st = con.createStatement();
	        	ResultSet rs = st.executeQuery("select * from remote;");
	        	 while (rs.next()) {
	        		 if(rs.getObject(2).equals(getuser)) {
	        			 ip=(String)rs.getObject(1);
	        			 name=(String)rs.getObject(2);
	        			 password=(String)rs.getObject(3);
	        		 }
	        	 }
	     		sess.setAttribute("ip", ip);
	     		sess.setAttribute("username", name);
	     		sess.setAttribute("password", password);
	     		ip="localhost";
	       }catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("error2");
			} 
			

			 
	       try {
	    	   
	    	   Class.forName("org.postgresql.Driver");
	         	Connection con = DriverManager.getConnection("jdbc:postgresql://"+ip+":5432/postgres" , name, password);
	         	Statement st = con.createStatement();
	          ResultSet rs = st.executeQuery("SELECT datname FROM pg_database");
	          ResultSetMetaData rsmd = rs.getMetaData();
	          int columnsNumber = rsmd.getColumnCount();
	          int count=0;
	          JSONObject databaselist = new JSONObject();
	          while (rs.next()) {
	        	  count+=1;
	                  for (int i = 1; i <= columnsNumber; i++) {

	                          String columnValue = rs.getString(i);
	                          databaselist.put("Data"+count,columnValue);
	                          
	                     }
	          
	   
	       }
	          PrintWriter printWriter = response.getWriter();
	          printWriter.println(databaselist);
	                  
	          
	       }
	         
	          
			
		 catch (Exception e) {
			// TODO: handle exception
			 System.out.println(e.getMessage());
			System.out.println("pgsql");
		}
	}

}
