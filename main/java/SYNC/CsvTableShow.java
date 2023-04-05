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

/**
 * Servlet implementation class CsvTableShow
 */
@WebServlet("/CsvTableShow")
public class CsvTableShow extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String database=request.getParameter("csvdatabase");
		String table=request.getParameter("csvtable");
		String mysqluser=request.getParameter("uservalue");
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String ip="";
	       String name="";
	       String password="";
	       String mysqlpassword="";
	       String mysqlIp="";
	       System.out.println(mysqluser);
	       try {
				Class.forName("com.mysql.cj.jdbc.Driver");
	        	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
	        	Statement st = con.createStatement();
	        	ResultSet rs = st.executeQuery("select * from sqlRemote;");
	        	 while (rs.next()) {
	        		 if(rs.getObject(2).equals(mysqluser)) {
	        			 ip=(String)rs.getObject(1);
	        			 name=(String)rs.getObject(4);
	        			 password=(String)rs.getObject(5);
	        			 mysqlpassword=(String)rs.getObject(3);
	        			 mysqlIp=(String)rs.getObject(6);
	        	 }
	     		
				
			}
	       }catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("error2");
			} 
	       
	       try {
			Connection clieConnection = DriverManager.getConnection("jdbc:mysql://"+mysqlIp+":3306/"+database, mysqluser, mysqlpassword);
			Statement st = clieConnection.createStatement();
	        int row = 0;
	        ResultSet rs3 = st.executeQuery("select * from "+table+";");
	        ResultSetMetaData rsmd3 = rs3.getMetaData();
	        int columnsNumber3 = rsmd3.getColumnCount();
	        while (rs3.next()) {
	                for (int i = 1; i <= columnsNumber3; i++) {
	                        
	                   }
	                row+=1;
	     }
	        int innerrow=0;
	        ResultSet rs4 = st.executeQuery("select * from "+table+";");
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

	    
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}
	}

}
