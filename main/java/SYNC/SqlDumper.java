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
 * Servlet implementation class SqlDumper
 */
@WebServlet("/SqlDumper")
public class SqlDumper extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		String uservalue=request.getParameter("uservalue");
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String ip="";
	       String name="";
	       String password="";
	       
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
        	Statement st = con.createStatement();
        	ResultSet rs = st.executeQuery("select * from sqlRemote;");
        	 while (rs.next()) {
        		 if(rs.getObject(2).equals(uservalue)) {
        			ip=rs.getString(1);
        			name=rs.getString(2);
        			password=rs.getString(3);
        		 }
        	 }
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
        		 
		
	     try {
				Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection masterConnection=DriverManager.getConnection("jdbc:mysql://"+ip+":3306/", name, password);
	            Statement statement= masterConnection.createStatement();
	            ResultSet rs = statement.executeQuery("show databases;");
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnsNumber = rsmd.getColumnCount();
	            int count=0;
	            
	            JSONObject database = new JSONObject();
	            while (rs.next()) {
	          	  count+=1;
	                  			
	                            database.put("Data"+count,rs.getObject(1));
	                            
	                       }
	          
	            
	         
	            PrintWriter printWriter = response.getWriter();
	            printWriter.println(database);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
	}

}
