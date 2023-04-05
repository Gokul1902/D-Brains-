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

@WebServlet("/ReviewData")
public class ReviewData extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession sess = request.getSession();
		HttpSession login = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String starCount=request.getParameter("starCount");
		String currentUser =(String) request.getParameter("currentusername");
		String currentMail =(String) request.getParameter("currentusermail");
		String reviewText =(String) request.getParameter("reviewText");
		Connection con=null;
		Statement st=null;
		System.out.println(starCount);
		System.out.println(currentUser);
		System.out.println(currentMail);
		System.out.println(reviewText);
		if(starCount!="0" || reviewText!=null || reviewText!="") {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/reviews", MasterUser, MasterPass);
			st = con.createStatement();
	        int rs = st.executeUpdate("insert into userReviews values ("+"'"+currentUser+"'"+","+"'"+currentMail+"'"+","+"'"+starCount+"'"+","+"'"+reviewText+"'"+")");
		}catch (Exception e) {
		System.out.println(e.getMessage());
		}
		}
		JSONObject 	jsonObject0 = new JSONObject();
		try {
			ResultSet rSet=st.executeQuery("select round(avg(starcount),1) from userReviews;");
			while(rSet.next()) {
				jsonObject0.put("AverageRating",rSet.getObject(1));
			}
			ResultSet rSet2=st.executeQuery("select count(*) from userReviews;");
			while(rSet2.next()) {
				jsonObject0.put("TotalRating",rSet2.getObject(1));
			}
			for(int i=1;i<=5;i++) {
				ResultSet rSet3=st.executeQuery("select count(*) from userReviews where starcount="+i);
				while(rSet3.next()) {
					jsonObject0.put(i+"starRating",rSet3.getObject(1));
				}
			}
		//	int count=0;
			//ResultSet rSet3=st.executeQuery("select * from userReview");
			//while(rSet3.next()) {
				//count+=1;
				
				//JSONObject 	jsonObject = new JSONObject();
				//jsonObject0.put("starRating",rSet3.getObject(1));
			//}
			out.println(jsonObject0);
			
			
			
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			
		}
		}

}
