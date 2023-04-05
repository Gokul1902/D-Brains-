package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/PsqlCreate")
public class PsqlCreate extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String query = request.getParameter("query");
		String databaseName = request.getParameter("databaseName");
		if(query!="") {
		HttpSession sess = request.getSession();
		
		request.setAttribute("userquery", query);
		}else {
			query=null;
		}
		
		
		
		if(query!=null){
			out.println(query+"<br>");
	        Connection con;  
	        Statement st=null;
	        Scanner scan = new Scanner(System.in);
	        boolean bool=true;
	        HttpSession sess = request.getSession();
	    	String database = request.getParameter("databaseName");
	        String ipaddress="localhost";
	        String name=(String)sess.getAttribute("username");
	        String password=(String)sess.getAttribute("password");
	                try {
	                	  Class.forName("org.postgresql.Driver");
		                    if(database==null) database="";
		                    con = DriverManager.getConnection("jdbc:postgresql://"+ipaddress+":5432/" + database, name, password);
	                    st = con.createStatement();
	                        if (query.equals("quit;")) {
	                            out.println("Bye" );
	                            bool = false;
	                        } else {
	                            if (query.substring(0, 4).equalsIgnoreCase("show") || query.substring(0, 6).equalsIgnoreCase("select") || query.substring(0, 4).equalsIgnoreCase("desc")) {
	                                long stime = System.currentTimeMillis();
	                                ResultSet rs = st.executeQuery(query);
	                                long etime = System.currentTimeMillis();
	                                float timing = (etime - stime) / (float) 100;
	                                out.println("Query Ok, rows affected(" + timing + "sec)"+"<br>");
	                                

	                                int row = 0;
	                                ResultSet rs3 = st.executeQuery(query);
	                                ResultSetMetaData rsmd3 = rs3.getMetaData();
	                                int columnsNumber3 = rsmd3.getColumnCount();
	                                while (rs3.next()) {
	                                        for (int i = 1; i <= columnsNumber3; i++) {
	                                                
	                                           }
	                                        row+=1;
	                             }
	                                ResultSet rs4 = st.executeQuery(query);
	                                ResultSetMetaData rsmd4 = rs4.getMetaData();
	                                int columnsNumber4 = rsmd4.getColumnCount();
	                                String [][] arr=new String[row][columnsNumber4];
	                                int innerrow=0;
	                                while (rs4.next()) {
	                                	
	                                    for (int i = 0; i < columnsNumber4; i++) {
	                                    	String columnValue = rs4.getString(i+1);
	                                            arr[innerrow][i]=columnValue;
	                                       }
	                                    innerrow+=1;
	                         }
	                                if(arr != null){
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
	                            

	                            }
	                        

	                            else {
	                                        long stime = System.currentTimeMillis();
	                                        st.executeUpdate(query);
	                                        long etime = System.currentTimeMillis();
	                                        float timing = (etime - stime) / (float) 100;
	                                        out.println("Query Ok, rows affected(" + timing + "sec)");

	                            }
	                        }
	                   }
	                catch (SQLException e){
	                	out.println(e.getMessage());
	                } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        }
			 
			 else{
				 out.println("Enter Something!!!");
			 }
	}

}
