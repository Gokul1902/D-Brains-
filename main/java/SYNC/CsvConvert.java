package SYNC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.pastdev.jsch.DefaultSessionFactory;
import com.pastdev.jsch.command.CommandRunner;

/**
 * Servlet implementation class CsvConvert
 */
@WebServlet("/CsvConvert")
public class CsvConvert extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean bool=true;
		boolean success=false;
		JSONObject jsonObject = new JSONObject();
		String database=request.getParameter("csvdatabase");
		String table=request.getParameter("csvtable");
		String mysqluser=request.getParameter("uservalue");
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String ip="";
	       String name="";
	       String password="";
	       String mysqlpassword="";
	       String mysqlIp="";
	       System.out.println(mysqluser);
	       try {
				Class.forName("com.mysql.cj.jdbc.Driver");
	        	Connection con = DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/user", MasterUser, MasterPass);
	        	Statement st = con.createStatement();
	        	ResultSet rs = st.executeQuery("select * from sqlRemote;");
	        	 while (rs.next()) {
	        		 if(rs.getObject(2).equals(mysqluser)) {
	        			 ip=(String)rs.getObject(1);
	        			 name=(String)rs.getObject(4);
	        			 password=(String)rs.getObject(5);
	        			 mysqlpassword=(String)rs.getObject(3);
	        			 mysqlIp=(String)rs.getObject(6);
	        	 }
	     		
				
			}
	       }catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("error2");
			} 
		
		try {
		boolean status = false;
        String directory = request.getParameter("csvfolder");
        String fileName=request.getParameter("csvfilename");
        String myLine = "";
        JSch jsch = new JSch();
        Session session = jsch.getSession(name, ip, 22);
        session.setPassword(password);
        String version = directory;
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        Channel channel = session.openChannel("exec");
         ((ChannelExec) channel).setCommand("find / -name " + version + " 2>/dev/null");
        channel.setInputStream(null);
        channel.connect();
       

        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        String line;
        CsvConvertProcess obj = new CsvConvertProcess();
        CsvFileReader obj1= new CsvFileReader();
       
        while ((line = reader.readLine()) != null) {
            myLine = line.trim();
            System.out.println(myLine);
            Channel channel2 = session.openChannel("sftp");
            channel2.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel2;
            sftpChannel.cd(myLine);
            Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
            for (ChannelSftp.LsEntry file : files) {
                if (file.getAttrs().isDir()) {
                    // list files in the subdirectory
                    String subdir = file.getFilename();
                    Vector<ChannelSftp.LsEntry> subfiles = sftpChannel.ls(subdir);
                    for (ChannelSftp.LsEntry subfile : subfiles) {
                        System.out.println(subdir + "/" + subfile.getFilename());
                        if((subdir + "/" + subfile.getFilename()).contains(fileName)){
                        	System.out.println("in"+myLine);
                        	myLine=(myLine+"/"+subdir + "/" + subfile.getFilename());
                        	myLine= myLine.replace("./", "");
                        	System.out.println("out"+myLine);
                        	
                        	obj1.CsvFileSaver(ip,name,password,myLine);
                        	String process=obj.convertProcess(database,table,mysqluser,mysqlpassword,mysqlIp,myLine);
                        	System.out.println(process);
                        	jsonObject.put("process", process);
                        }
                    }
                } else {
                    System.out.println(file.getFilename());
                    if (file.getFilename().equals(fileName)) {
                    	obj1.CsvFileSaver(ip,name,password,myLine);
                    	String process=obj.convertProcess(database,table,mysqluser,mysqlpassword,mysqlIp,myLine);
                    	System.out.println(process);
                    	jsonObject.put("process", process);
                    }
                }
            }


            
        }
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				jsonObject.put("process", "fail");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO: handle exception
		}

		PrintWriter out = response.getWriter();
		out.println(jsonObject);


	}

}
