package SYNC;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

//import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
@WebServlet ("/DB")
public class Database extends HttpServlet{
	  public void service(HttpServletRequest req,HttpServletResponse res) {
		
		HttpSession sess = req.getSession();
       String ip="";
       String name="";
       String password="";
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String getuser= req.getParameter("getUser");
		System.out.println("jj"+getuser);
       
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
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error2");
		} 
		

		 
       try {
    	   
    	   Class.forName("com.mysql.cj.jdbc.Driver");
         	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/", name, password);
         	Statement st = con.createStatement();
          ResultSet rs = st.executeQuery("show databases;");
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
          PrintWriter printWriter = res.getWriter();
          printWriter.println(databaselist);
                  

          
//          RequestDispatcher rd = 
//                  req.getRequestDispatcher("/show.jsp");
//          rd.forward(req,res);
          
       }
         
          
		
	 catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
		System.out.println("error2");
	}
	  }
       }
	  
       


