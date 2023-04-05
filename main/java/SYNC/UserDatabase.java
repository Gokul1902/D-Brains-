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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/userdb")
public class UserDatabase extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
       String ip="";
       String name="";
       String password="";
       String database="";
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String getuser= request.getParameter("getUser");
		System.out.println(getuser);
		String [] userStrings=getuser.split("-");
		String clientName=userStrings[0];
		String clientIp=userStrings[1];
		System.out.println(clientIp);
		System.out.println(clientName);
		System.out.println("master"+MasterIp);
		System.out.println(MasterUser);
		System.out.println(MasterPass);
		System.out.println(getuser);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
         	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
         	Statement st = con.createStatement();
         	ResultSet rs = st.executeQuery("select * from userdetail;");
         	 while (rs.next()) {
         		 if(rs.getObject(2).equals(clientName.trim()) && rs.getObject(1).equals(clientIp.trim())) {
         			 ip=(String)rs.getObject(1);
         			 name=(String)rs.getObject(2);
         			 password=(String)rs.getObject(3);
         		 }
         	 }
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error2");
		} 
		sess.setAttribute("userip", ip);
		sess.setAttribute("UserName", name);
		sess.setAttribute("userpass", password);
		System.out.println("cl"+ip);
		System.out.println(name);
		System.out.println(password);
		 
	       try {
	    	   Class.forName("com.mysql.cj.jdbc.Driver");
	    	   Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" + database, name, password);
	    	   Statement st = con.createStatement();
	          ResultSet rs = st.executeQuery("show databases;");
	          ResultSetMetaData rsmd = rs.getMetaData();
	          int columnsNumber = rsmd.getColumnCount();
	          int count=0;
	          JSONObject userdatabaselist = new JSONObject();
	          while (rs.next()) {
	        	  count+=1;
	                  for (int i = 1; i <= columnsNumber; i++) {

	                          String columnValue = rs.getString(i);
	                          userdatabaselist.put("Data"+count,columnValue);
	                          
	                     }
	          
	   
	       }
	          PrintWriter printWriter = response.getWriter();
	          printWriter.println(userdatabaselist);
          
       }
         
          
		
	 catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
		System.out.println("error2");
	}
	}



}
