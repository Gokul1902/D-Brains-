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
import java.sql.*;

/**
 * Servlet implementation class MasterClientConnect
 */
@WebServlet("/MasterClient")
public class MasterClientConnect extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String addType=request.getParameter("addType");
		String currentMasterName =null;
		String currentUserIp=null;
		String currentUserPass=null;
		Connection con=null;
		Connection connection=null;
		Statement st =null;
		JSONObject jsonObject= new JSONObject();
		System.out.println("sadhana1");
		if(addType.equals("masterAdd")) {
			currentMasterName= request.getParameter("currentMasterName");
			currentUserIp= request.getParameter("currentUserIp");
			currentUserPass= request.getParameter("currentUserPass");
			System.out.println("sadhana2");
			System.out.println(currentMasterName);
			System.out.println("kk"+currentUserIp);
			System.out.println(currentUserPass);
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
			try {
				System.out.println("sadhana3");
				con = DriverManager.getConnection("jdbc:mysql://"+currentUserIp+":3306/", currentMasterName, currentUserPass);
                st = con.createStatement();
                System.out.println("sadhana4");
                sess.setAttribute("currentMasterIp", currentUserIp);
        		sess.setAttribute("currentMasterUser", currentMasterName);
        		sess.setAttribute("currentMasterPass", currentUserPass);
                jsonObject.put("Master", "success"); 
			}
			catch (Exception e) {
				try {
					
					jsonObject.put("Master", "Fail");
					System.out.println(e.getMessage());
				} catch (JSONException e1) {
					System.out.println(e.getMessage());
				} 
			}
			
		}
		else {
			
			try {
				String currentUserMaster=(String) sess.getAttribute("currentMasterUser");
				String currentipMaster=(String) sess.getAttribute("currentMasterIp");
				String currentpassMaster=(String) sess.getAttribute("currentMasterPass");
				String clientName= request.getParameter("clientName");
				String clientIp= request.getParameter("clientIp");
				String clientPass= request.getParameter("clientPass");
				String database = request.getParameter("Database");
				System.out.println(clientName);
				System.out.println(clientPass);
				System.out.println(clientIp);
				con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
				System.out.println("sadhana25");
				Connection mascon = DriverManager.getConnection("jdbc:mysql://"+currentipMaster+":3306/"+database, currentUserMaster, currentpassMaster);
				System.out.println("sadhana26");
				connection = DriverManager.getConnection("jdbc:mysql://"+clientIp+":3306/", clientName, clientPass);
				System.out.println("sadhana27");
				Statement st2=con.createStatement();
				 st = con.createStatement();
				 ResultSet rSet= st.executeQuery("select * from userdetail");
				 boolean check=true;
				 while(rSet.next()) {
					 if(rSet.getObject(2).equals(clientName) && rSet.getObject(3).equals(clientPass) ) {
						System.out.println("sadhana9");
						 int resultSet= st2.executeUpdate("update userdetail set ipaddress="+"'"+clientIp+"'"+" where name="+"'"+clientName+"'"+" and password="+"'"+clientPass+"'");
						 jsonObject.put("Client", "success");
						 check=false;
					 }
					 
				 }if(check==true) {
					 System.out.println("else part");
					 int resultSet=st2.executeUpdate("insert ignore into userdetail values("+"'"+clientIp+"'"+","+"'"+clientName+"'"+","+"'"+clientPass+"'"+")");
					 jsonObject.put("Client", "success");
				 }
				 if(currentUserMaster!=null) {
					 Compare obj = new Compare();
					 System.out.println(obj.summa2(mascon, connection,currentipMaster, currentUserMaster, currentpassMaster,clientIp,clientName,clientPass,database));
					 
				 }
			} catch (SQLException | JSONException  e) {
				 try {
					 System.out.println(e.getMessage());
					jsonObject.put("Client", "Fail");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
           
		}
		System.out.println(jsonObject);
		PrintWriter out = response.getWriter();
		out.println(jsonObject);
	}	

}
