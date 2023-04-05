package SYNC;

import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

//import org.json.simple.JSONObject;

/**
 * Servlet implementation class Mysql
 */
@WebServlet("/Mysql")
public class MysqlConvertor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> commands = new HashMap<>();
		StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
        	System.out.println(line);
            sb.append(line);
        }
        String requestBody = sb.toString();
        System.out.println(requestBody);
        JSONObject reqObj=null;
		try {
			reqObj = new JSONObject(requestBody);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String command="";
		try {
			command = reqObj.getString("command");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        command.toLowerCase();
		commands.put("\\c", "\\r");
		commands.put("\\e", "\\e");
		commands.put("\\g", "\\g");
		commands.put("\\h", "\\h");
		commands.put("\\n", "\\pset pager off");
		commands.put("mysqldump", "pg_dumpall");
		commands.put("mysqldump", "pg_dump");
		commands.put("\\p", "\\p");
		commands.put("\\q", "\\q");
		commands.put("\\r", "\\c");
		commands.put("mysql -u username -p", "sudo -u postgres psql");
		commands.put("show databases", "\\l");
		commands.put("use database", "\\c database;");
		commands.put("show tables", "\\dt");
		commands.put("describe tablename", "\\d+ tablename");
		commands.put("show index from tablename", "\\di");
		commands.put("select column from tablename", "SELECT column FROM tablename");
		commands.put("create user username identified by 'password'", "create role username with createdb login [password];");
		commands.put("grant all privileges on database.*to username@localhost", "grant all privileges on database to username");
		commands.put("your query\\g", "\\x on");
		commands.put("n/a", "pg_restore");
		commands.put("innodb_top", "pg_top");
		commands.put("mysql", "psql");
		commands.put("exit", "Exit Postgres/q");
		commands.put("show variables;", "show all;");
		commands.put("\\g", "\\x");
		commands.put("stop slave", "select pg_xlog_replay_pause();");
		commands.put("start slave", "select pg_xlog_replay_resume();");
		commands.put("limit", "LIMIT");
		commands.put("auto_increment", "SERIAL");
		commands.put("now()", "NOW()");
		commands.put("ifnull", "COALESCE");
		commands.put("tinyint", "SMALLINT");
		commands.put("double", "DOUBLE PRECISION");
		commands.put("datetime", "TIMESTAMP");
		commands.put("enum", "TEXT");
		commands.put("blob", "BYTEA");
		commands.put("if not exists", "IF NOT EXISTS");
		commands.put("select @@version", "SELECT version()");
		commands.put("show status", "\\pset format unaligned");
		commands.put("drop table tablename", "DROP TABLE tablename");
		commands.put("select", "SELECT");
		commands.put("insert", "INSERT");
		commands.put("update", "UPDATE");
		commands.put("delete", "DELETE");
		commands.put("create", "CREATE");
		commands.put("alter", "ALTER");
		commands.put("drop", "DROP");
		commands.put("truncate", "TRUNCATE");
		commands.put("use", "\\c");
		commands.put("use", "\\connect");
		commands.put("show", "\\d");
		commands.put("describe", "\\d tablename");
		commands.put("explain", "\\d tablename");
		commands.put("group by", "GROUP BY");
		commands.put("having", "HAVING");
		commands.put("order by", "ORDER BY");
		commands.put("where", "WHERE");
		Boolean found = false;
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, String> entry : commands.entrySet()) {
            if (entry.getKey().contains(command) || command.contains(entry.getKey())) {
            	
            	try {
					jsonObject.put("key",entry.getValue());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	found = true;
            }
        }
		if (!found) {
			try {
				jsonObject.put("key", "The given command does not have any alternative or check the given command once.");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.getWriter().print(jsonObject.toString());
    }
}


