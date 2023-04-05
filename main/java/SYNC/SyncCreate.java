package SYNC;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.naming.java.javaURLContextFactory;
import java.sql.*;
import java.util.*;


/**
 * Servlet implementation class Create
 */
@WebServlet("/SyncCreate")
public class SyncCreate extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String query = request.getParameter("query");
		if(query!="") {
		HttpSession sess = request.getSession();
		String database = (String)sess.getAttribute("database");
		sess.setAttribute("databases", database);
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
	        String database=(String)sess.getAttribute("database");
	        String ipaddress=request.getParameter("currentUserIp");
	        String name=request.getParameter("currentMasterName");
	        String password=request.getParameter("currentUserPass");
	        System.out.println(ipaddress+":"+name+":"+password);
	        String status=(String)sess.getAttribute("status");
	        if(status!=null) {
	        	database=request.getParameter("database");
	        	sess.setAttribute("status",null);
	        }
	                try {
	                	System.out.println(query);
	                    Class.forName("com.mysql.cj.jdbc.Driver");
	                    System.out.println("entry"+database);
	                    if(database==null) database="";
	                    System.out.println("exit"+database);
						con = DriverManager.getConnection("jdbc:mysql://"+ipaddress+":3306/" + database, name, password);
	                    st = con.createStatement();
	                        if (query.equals("quit;")) {
	                            out.println("Bye" );
	                            bool = false;
	                        } else if (query.substring(0, 3).equals("use") && query.length() > 4) {
	                        	
	                            database = query.replaceAll(";", "");
	                            database = (database.replaceAll(" ", "")).substring(3);
	                            System.out.println("data--->"+database);
	                            
	                            con = DriverManager.getConnection("jdbc:mysql://"+ipaddress+":3306/" + database, name, password);
	                            sess.setAttribute("database", database);
	                            st = con.createStatement();
	                            out.println("Database changed");
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
	                            		if((query.toLowerCase()).contains("insert")) {
	                            			
	                            			String originalString = query;
	                            			String wordToInsert = " ignore ";
	                            			int positionToInsert = 7;
	                            			query = originalString.substring(0, positionToInsert) + wordToInsert + originalString.substring(positionToInsert);	
	                            			System.out.println(query);
	                            		}
	                                        long stime = System.currentTimeMillis();
	                                        st.executeUpdate(query);
	                                        long etime = System.currentTimeMillis();
	                                        float timing = (etime - stime) / (float) 100;
	                                        out.println("Query Ok, rows affected(" + timing + "sec)");

	                            }
	                        }
	                   }
	                catch (SQLException e){
	                	e.printStackTrace();
	                	out.println(e.getMessage());
	                } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
	                	System.out.println(e.getMessage());
						e.printStackTrace();
					}
	        }
			 
			 else{
				 out.println("Enter Something!!!");
			 }
	}

}
