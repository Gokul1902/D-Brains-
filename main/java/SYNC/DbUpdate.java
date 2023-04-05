package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

/**
 * Servlet implementation class DbUpdate
 */
@WebServlet("/DbUpdate")
public class DbUpdate extends HttpServlet {
	public boolean universalChange(Connection localcon, Connection remotecon, boolean check,String queryinput) {
		boolean result;
		try {
			Statement localst = localcon.createStatement();
			Statement remotest = remotecon.createStatement();
			String sql = queryinput;
			if(check==true) {
				localst.executeUpdate(sql);
				check=false;
			}
			remotest.executeUpdate(sql);
			result = true;
			return result;
		}
		catch(Exception e){
			result = false;
			System.out.println(e.getMessage());
			System.out.println("i am main error");
			return result;
		}
		
	}
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		String currenMasterIp = (String)sess.getAttribute("currentMasterIp");
		String currentMasterUser =(String)sess.getAttribute("currentMasterUser");
		String currentMasterPass = (String)sess.getAttribute("currentMasterPass");
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		System.out.println("ok"+currenMasterIp);
		System.out.println("ok"+currentMasterUser);
		System.out.println("ok"+currentMasterPass);
		PrintWriter out = response.getWriter();
		String query = request.getParameter("clientquery");
		String queryDatabase=request.getParameter("queryDatabase");
		boolean status=true;
		if(query!="") {
		sess.setAttribute("queryDatabase", queryDatabase);
		request.setAttribute("userquery", query);
		}else {
			query=null;
		}
		
		if(query!=null) {
				try {
					Connection con=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery("select * from userdetail");
			
					Connection mastercon=DriverManager.getConnection("jdbc:mysql://"+currenMasterIp+":3306/", currentMasterUser, currentMasterPass);
				
					Statement masterStatement=mastercon.createStatement();
					
					int rset = masterStatement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+queryDatabase);
					 mastercon=DriverManager.getConnection("jdbc:mysql://"+currenMasterIp+":3306/"+queryDatabase, currentMasterUser, currentMasterPass);
					
					Connection remotecon=null;
					Compare objCompare = new Compare();
					boolean check = true;
					while(rs.next()) {
						if(rs.getObject(1).equals(currenMasterIp) && rs.getObject(2).equals(currentMasterUser) && rs.getObject(3).equals(currentMasterPass)) {
							continue;
						}
						else {
							Connection client=DriverManager.getConnection("jdbc:mysql://"+rs.getString(1)+":3306/", rs.getString(2), rs.getString(3));
							Statement clientStatement=client.createStatement();
							clientStatement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+queryDatabase);
							client=DriverManager.getConnection("jdbc:mysql://"+rs.getString(1)+":3306/"+queryDatabase, rs.getString(2), rs.getString(3));
						 status= objCompare.universalChange(mastercon, client, check, query);
						    check = false;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String process="";
				if(status) {
					process="true";
					sess.setAttribute("status", "true");
				}
				else {
					process="false";
				}
				
				System.out.println("okkkkkkkkk");
				out.println(process);
			

	}

 }
	}
