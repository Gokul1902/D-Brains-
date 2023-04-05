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

import org.json.JSONObject;

/**
 * Servlet implementation class RemoteUser
 */
@WebServlet("/RemoteUser")
public class RemoteUser extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		JSONObject jsonObject = new JSONObject();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection masterConnection=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
            Statement statement= masterConnection.createStatement();
            ResultSet rs = statement.executeQuery("select * from remote;");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int count=0;
            JSONObject databaselist = new JSONObject();
            while (rs.next()) {
          	  count+=1;
                  			
                            databaselist.put("Data"+count,rs.getObject(2));
                            
                       }
            
     
         
            PrintWriter printWriter = response.getWriter();
            printWriter.println(databaselist);
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println(e.getMessage());
		}
		
	}

}
