package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;


@WebServlet("/Signpage")
public class Signpage extends HttpServlet {
	public void service(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		String processType = request.getParameter("type");
		System.out.println(processType);
		HttpSession sess = request.getSession();
		HttpSession mess = request.getSession();
		HttpSession login = request.getSession();
		sess.setAttribute("MasterIp", "192.168.43.129");
		sess.setAttribute("MasterUser", "test");
		sess.setAttribute("MasterPass", "Amma@143");
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		Statement st = null;
		JSONObject jsonObject = new JSONObject();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection	con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/loguser", MasterUser, MasterPass);
			st = con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		if(processType.equals("Log In")) {
			String logname = request.getParameter("logname");
			String logpass = request.getParameter("logpass");
			System.out.println(logname+":"+logpass);
			ResultSet rs;
			boolean done=false;
			try {
				rs = st.executeQuery("select * from logdetail;");
			    while (rs.next()) {
		        	if(rs.getObject(1).equals(logname) && rs.getObject(2).equals(logpass)) {
		        		login.setAttribute("currentusername", logname);
		        		login.setAttribute("currentuserpass", logpass);
						/*
						 * Connection logcon =
						 * DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user",
						 * MasterUser, MasterPass); Statement logst = logcon.createStatement();
						 * logst.executeUpdate("delete from remote");
						 * logst.executeUpdate("delete from sqlRemote");
						 */
		        		jsonObject.put("loginStatus", "success");
		              done=true;
		        	}
		     }
			} catch (SQLException | JSONException  e) {
				mess.setAttribute("logmessage", "Enter a wrong username or password");
				try {
					jsonObject.put("loginStatus", "fail");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		
			}
			if(done==false) {
				mess.setAttribute("logmessage", "The username or password is not available");
				try {
					jsonObject.put("loginStatus", "fail");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

	    
			
		}
		else if(processType.equals("logout")) {
			System.out.println("logout");
			login.removeAttribute("currentusername");
    		login.removeAttribute("currentuserpass");
    		try {
				jsonObject.put("loginStatus", "fail");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			String signname = request.getParameter("signname");
			String signpass = request.getParameter("signpass");
			String signmail = request.getParameter("signmail");
			if(signname!="" && signpass!="" && signmail!="" ) {
			try {
				login.setAttribute("currentusername", signname);
        		login.setAttribute("currentuserpass", signpass);
				st.executeUpdate("insert into logdetail values ("+"'"+signname+"'"+","+"'"+signpass+"'"+","+"'"+signmail+"'"+")");
				/*
				 * Connection logcon =
				 * DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user",
				 * MasterUser, MasterPass); Statement logst = logcon.createStatement();
				 * logst.executeUpdate("delete from remote");
				 * logst.executeUpdate("delete from sqlRemote");
				 */
				jsonObject.put("loginStatus", "success");
			}
			catch (Exception e) {
				mess.setAttribute("logmessage", "The username or password is not available");
				try {
					jsonObject.put("loginStatus", "fail");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	          
			}
			}else {
				try {
					jsonObject.put("loginStatus", "fail");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		PrintWriter out= response.getWriter();
		System.out.println("j"+jsonObject);
		out.println(jsonObject);
	}

}
