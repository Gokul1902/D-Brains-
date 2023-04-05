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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class LogDetail
 */
@WebServlet("/logdetail")
public class LogDetail extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession login = request.getSession();
		HttpSession sess = request.getSession();
		String currentUsername=(String)login.getAttribute("currentusername");
		String currentUserpass=(String)login.getAttribute("currentuserpass");
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		Statement st = null;
		try {
			PrintWriter out = response.getWriter();
			JSONObject logindetail = new JSONObject();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection	con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/loguser", MasterUser, MasterPass);
			st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from logdetail;");
	        while (rs.next()) {
	        	if(rs.getObject(1).equals(currentUsername) && rs.getObject(2).equals(currentUserpass)) {
	        		String name = (String)rs.getObject(1);
	        		logindetail.put("loginsingle", name.substring(0,1));
	        		logindetail.put("loginname", rs.getObject(1));
	        		logindetail.put("loginmail", rs.getObject(3));
	        		if(currentUsername.equals("gokul") && currentUserpass.equals("Amma@143")) {
	        			logindetail.put("level", "Server");
	        		}
	        		else {
	        			logindetail.put("level", "Client");
	        		}
	        		out.println(logindetail);
	        	}
	        	
	        }
		} catch (ClassNotFoundException | SQLException | JSONException e) {
			e.printStackTrace();
		}
		
	}

}
