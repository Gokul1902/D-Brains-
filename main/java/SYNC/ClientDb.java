package SYNC;

import java.io.IOException;
import java.net.InetAddress;
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

@WebServlet("/clientdb")
public class ClientDb extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");

		 System.out.println("present");

         JSONObject userlist = new JSONObject();
       try {
    	   
    	   Class.forName("com.mysql.cj.jdbc.Driver");
    	   System.out.println("ok");
         	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
         	 System.out.println(" not ok");
         	Statement st = con.createStatement();
          ResultSet rs = st.executeQuery("select * from userdetail;");
          ResultSetMetaData rsmd = rs.getMetaData();
          int columnsNumber = rsmd.getColumnCount();
          
          int count=0;
          while (rs.next()) {
        	  count+=1;
        	  try {
                  
                  InetAddress localhost = InetAddress.getByName((String) rs.getObject(1));
                  System.out.println("Local host IP address: " + localhost.getHostAddress());
                 
                  boolean isReachable = localhost.isReachable(1000); 

                  if (isReachable) {
                      System.out.println("Host is reachable");
                      System.out.println("table");
            		  Connection connection=DriverManager.getConnection("jdbc:mysql://"+rs.getString(1)+":3306/", rs.getString(2), rs.getString(3));
            		  String columnValue= rs.getObject(2)+" - "+rs.getObject(1);
                	  userlist.put("user"+count,columnValue);
                  } else {
                      System.out.println("Host is not reachable");
                      System.out.println("delete");
      				st = con.createStatement();
      				 int rSet= st.executeUpdate("delete from userdetail where name="+"'"+rs.getObject(2)+"'"+" and password="+"'"+rs.getObject(3)+"'");
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
        	  
        	  
          }
          
          
       }
         
          
		
	 catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
       System.out.println(userlist);
       PrintWriter printWriter = response.getWriter();
       printWriter.println(userlist);
	}

}
