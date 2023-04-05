package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

//import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Statement;
@WebServlet ("/checker")
public class LogChecker extends HttpServlet {

	Connection con=null;
	java.sql.Statement st =null;
	String database="";
	public void service(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		
		HttpSession sess = req.getSession();
		HttpSession error = req.getSession();
		PrintWriter out = res.getWriter();
		String ip = req.getParameter("logip");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
//		sess.setAttribute("ip", ip);
//		sess.setAttribute("username", username);
//		sess.setAttribute("password", password);
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		JSONObject jsonObject = new JSONObject();
		
		
		 try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("error");
		}
		if(ip==" " || username==" " || password=="") {
			RequestDispatcher Rd = req.getRequestDispatcher("/Mainwork.html");
			Rd.forward(req, res);
		}
		else {
			try {
				con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" + database, username, password);
                st = con.createStatement();
                Connection masterConnection=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
                Statement statement = masterConnection.createStatement();
                statement.executeUpdate("insert into remote values("+"'"+ip+"'"+","+"'"+username+"'"+","+"'"+password+"'"+")");
                jsonObject.put("connect","success");
			}
			catch (Exception e) {
				try {
					jsonObject.put("connect","fail" );
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			
			out.println(jsonObject);
		}
		
	}

}
