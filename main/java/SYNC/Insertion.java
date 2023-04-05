package SYNC;

import java.io.BufferedReader;
import java.io.File;
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

import org.apache.jasper.compiler.JspUtil;
import org.json.JSONException;
import org.json.JSONObject;

import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.pastdev.jsch.DefaultSessionFactory;
import com.pastdev.jsch.command.CommandRunner;

/**
 * Servlet implementation class Insertion
 */
@WebServlet("/Insertion")
public class Insertion extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean bool=true;
		boolean success=false;
		String mysqluser=request.getParameter("uservalue");
		HttpSession sess = request.getSession();
		String MasterIp = (String)sess.getAttribute("MasterIp");
		String MasterUser =(String)sess.getAttribute("MasterUser");
		String MasterPass = (String)sess.getAttribute("MasterPass");
		String ip="";
	       String name="";
	       String password="";
	       String mysqlpassword="";
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
	        			 sess.setAttribute("currentsqlip", ip);
	        			sess.setAttribute("currentsqluser", mysqluser);
	        			sess.setAttribute("currentsqlpass", mysqlpassword);
	        	 }
	     		
				
			}
	       }catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("error2");
			} 
        try {
            boolean status = false;
            String directory = request.getParameter("folder");
            String fileName=request.getParameter("file");
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
                            	myLine=(myLine+"/"+subdir + "/" + subfile.getFilename());
                            	DefaultSessionFactory sessionFactory = new DefaultSessionFactory(
                                        name, ip, 22
                                );
                                Map props = new HashMap<String, String>();
                                props.put("StrictHostKeyChecking", "no");
                                sessionFactory.setConfig(props);
                                sessionFactory.setPassword(password);

                                CommandRunner runner = new CommandRunner(sessionFactory);

                                String command = "mysql -u "+mysqluser+" -p"+mysqlpassword+" < "+myLine;
                                CommandRunner.ExecuteResult result = runner.execute(command);
                                success=true;

                                if (result.getStderr().isEmpty()) {
                                    System.out.println(result.getStdout());
                                    
                                } else {
                                    System.out.println(result.getStderr());
                                }

                                runner.close();
                                bool=false;
                            }
                        }
                    } else {
                        System.out.println(file.getFilename());
                        if (file.getFilename().equals(fileName)) {
                            myLine +=("/"+fileName);
                            myLine.replace(" ","");
                            System.out.println(fileName);
                            DefaultSessionFactory sessionFactory = new DefaultSessionFactory(
                                    name, ip, 22
                            );
                            Map props = new HashMap<String, String>();
                            props.put("StrictHostKeyChecking", "no");
                            sessionFactory.setConfig(props);
                            sessionFactory.setPassword(password);

                            CommandRunner runner = new CommandRunner(sessionFactory);

                            String command = "mysql -u "+mysqluser+" -p"+mysqlpassword+" < "+myLine;
                            CommandRunner.ExecuteResult result = runner.execute(command);
                            success=true;

                            if (result.getStderr().isEmpty()) {
                                System.out.println(result.getStdout());
                                
                            } else {
                                System.out.println(result.getStderr());
                            }

                            runner.close();
                            bool=false;
                        }
                    }
                }


                
            }
            if (bool==true){
                System.out.println("file not found");
            }




        }catch(Exception ex){
            System.out.println(ex.getMessage());
            }
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        
        try {
        if(success==true) {
				jsonObject.put("process", "success");
				System.out.println("ok");
        }
        else {
			jsonObject.put("process", "fail");
			System.out.println("not ok");

        }
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.println(jsonObject);

	}

}
