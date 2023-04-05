package SYNC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
 * Servlet implementation class AddSqlClient
 */
@WebServlet("/AddSqlClient")
public class AddSqlClient extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user =request.getParameter("sysuser");
        String sqltoipadd =request.getParameter("sqltoipadd");
        String mysqlUser=request.getParameter("myuser");
        String password = request.getParameter("syspass");
        String mysqlpassword = request.getParameter("mypass");
        String ip = request.getParameter("ipadd");
        HttpSession sess = request.getSession();
    	String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
//        sess.setAttribute("sqlip", ip);
// 		sess.setAttribute("mysqlUser", mysqlUser);
// 		sess.setAttribute("syspassword", password);
// 		sess.setAttribute("mysqlpassword", mysqlpassword);
// 		sess.setAttribute("sysuser", user);
        try {
			Connection con = DriverManager.getConnection("jdbc:mysql://"+sqltoipadd+":3306/", mysqlUser, mysqlpassword);
            Statement st = con.createStatement();
            Connection masterConnection=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
            Statement statement = masterConnection.createStatement();
            statement.executeUpdate("insert into sqlRemote values("+"'"+ip+"'"+","+"'"+mysqlUser+"'"+","+"'"+mysqlpassword+"'"+","+"'"+user+"'"+","+"'"+password+"'"+","+"'"+sqltoipadd+"')");
            jsonObject.put("connect","success");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
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
