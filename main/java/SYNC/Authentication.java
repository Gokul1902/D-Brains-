package SYNC;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Authentication
 */
@WebServlet("/Authentication")
public class Authentication extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String ip = (String)sess.getAttribute("ip");
	    String name = (String)sess.getAttribute("username");
	    String password = (String)sess.getAttribute("password");
	    System.out.println("o"+MasterIp);
	    System.out.println(MasterUser);
	    System.out.println(MasterPass);
	    System.out.println(ip);
	    System.out.println(name);
	    System.out.println(password);
	    PrintWriter printWriter = response.getWriter();
	    JSONObject access = new JSONObject();

			try {
			if(ip.equals(MasterIp) && name.equals(MasterUser) && password.equals(MasterPass)) {
				access.put("access", "True");
				}
			else {
				access.put("access", "false");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			printWriter.println(access);
		}
		

	}


