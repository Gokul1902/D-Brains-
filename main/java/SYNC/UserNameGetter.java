package SYNC;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserNameGetter")
public class UserNameGetter extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String getuser= (String)request.getParameter("userName"); 
		HttpSession sess = request.getSession();
		sess.setAttribute("getusername",getuser);
		RequestDispatcher rd = 
        		request.getRequestDispatcher("/usershow.html");
        rd.forward(request,response);
	}

}
