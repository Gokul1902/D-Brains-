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

@WebServlet("/usertabledata")
public class UserTableData extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
     String database =(String)sess.getAttribute("setuserdatabase");
     String tablename =request.getParameter("tableName");
     String ip = (String)sess.getAttribute("userip");
     String name = (String)sess.getAttribute("UserName");
     String password = (String)sess.getAttribute("userpass");
		 
     try {
    	   
    	   Class.forName("com.mysql.cj.jdbc.Driver");
         	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" + database, name, password);
         	Statement st = con.createStatement();
          int row = 0;
          ResultSet rs3 = st.executeQuery("select * from "+tablename+";");
          ResultSetMetaData rsmd3 = rs3.getMetaData();
          int columnsNumber3 = rsmd3.getColumnCount();
          while (rs3.next()) {
                  for (int i = 1; i <= columnsNumber3; i++) {
                          
                     }
                  row+=1;
       }
          int innerrow=0;
          ResultSet rs4 = st.executeQuery("select * from "+tablename+";");
          ResultSetMetaData rsmd4 = rs4.getMetaData();
          int columnsNumber4 = rsmd4.getColumnCount();
          String [][] arr=new String[row][columnsNumber4];
          
          while (rs4.next()) {
          	
              for (int i = 0; i < columnsNumber4; i++) {
              	String columnValue = rs4.getString(i+1);
                      arr[innerrow][i]=columnValue;
                 }
              innerrow+=1;
   }

          PrintWriter out = response.getWriter();
          
          out.println("<table>");
  		for(int i = 0;i < arr.length;i++){
  			out.println("<tr>");
  			for(int j = 0;j < arr[i].length;j++){
  				if(i == 0){
  					out.println("<td>" + arr[i][j] + "</td>");
  				}
  				else{
  					out.println("<td>" + arr[i][j] + "</td>");
  				}
  			}
  			out.println("</tr>");
  		}
  		out.println("</table>");

      
       }
        
		
	 catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
	}
	}
}
