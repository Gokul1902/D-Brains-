package SYNC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.MAC;


public class CsvConvertProcess {
	public String convertProcess(String database,String table,String USER,String PASS,String ip,String csvFile) {
		String process="fail";  
	    try {
	      Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/" ,USER, PASS);
	      Statement st = con.createStatement();
	      st.executeUpdate("CREATE DATABASE IF NOT EXISTS "+database);
//	      con = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/"+database,USER, PASS);
	    BufferedReader br = new BufferedReader(new FileReader("/home/gokul/input.txt"));
	    
	    String firstLine = br.readLine();
	    System.out.println("fl "+firstLine);
	    
	    String reg = "(?:\"([^\"]*(?:\"\"[^\"]*)*)\")|([^\",]+)";
	    Pattern p = Pattern.compile(reg);
	    Matcher matcher = p.matcher(firstLine);
	    
	    ArrayList<String> headers = new ArrayList<String>();
	    
	    while (matcher.find()) {			
	    	headers.add(matcher.group());
		}
	   
	    System.out.println(headers);
	    
	    
	    String tableQuery = "create table "+table+"(";

	    String firstRow = br.readLine();
//	    System.out.println("fl "+firstRow);
	    
	    matcher = p.matcher(firstRow);
	    
	    ArrayList<String> firstRowArrayList = new ArrayList<String>();
	    
	    while (matcher.find()) {			
	    	firstRowArrayList.add(matcher.group());
		}
	    for(int i=0;i<headers.size();i++) {
	    String v = firstRowArrayList.get(i);
	    tableQuery += headers.get(i);
	    
	    try {
	    // number part
	    if(v.contains(".")) {
	    Double.parseDouble(v);
	    tableQuery += " double";
	    }
	    else {
	    Long.parseLong(v);
	    tableQuery += " varchar(255)"; 
	    }
	    }catch (NumberFormatException e) {
					try {
						// date part
						if(v.contains("/")) {
							Date.parse(v);
							tableQuery += " date";
						}
						else{
							LocalDate.parse(v);
							tableQuery += " date";
						}
						
					}catch (DateTimeParseException ee) {
						try {
							// time part
							LocalTime.parse(v);
							tableQuery += " time";
						}catch (DateTimeParseException eee) {
							tableQuery += " varchar(255)";
						}
					}
					
				}
	    if(i < headers.size()-1) {
	    tableQuery += ", ";
	    }
	    else {
	    tableQuery += ")";
	    }
	    
	    }
	    
	    
	    System.out.println(tableQuery);

	    process="success";
	    Connection conn = DriverManager.getConnection("jdbc:mysql://"+ip+":3306/"+database ,USER, PASS);
	    Statement statement = conn.createStatement();
	    statement.executeUpdate(tableQuery);
	    System.out.println("Table created in Database..!");
//	    conn.close();
	    
	    br = new BufferedReader(new FileReader("/home/gokul/input.txt"));
	    br.readLine();
	    
	        String line;
	        
	        while ((line = br.readLine()) != null) {
	        	String row = br.readLine();
//	     	    System.out.println("fl "+row);
	     	    
	     	    matcher = p.matcher(row);
	     	    
	     	    ArrayList<String> values = new ArrayList<String>();
	     	    
	     	    while (matcher.find()) {			
	     	    	values.add(matcher.group());
	     	    	
	     		} // 5.1, 3.5, 1.4, 0.2, Iris-setosa
	            int length = values.size();
	            
	            String query = "INSERT INTO "+table+" VALUES (";
	            for(int i=1;i<=length;i++) {
	            query+="?";
	            if(i==length) {
	            query+=")";
	            }
	            else {
	            query+=",";
	            }
	            }
//	            System.out.println(query);
	            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	            for(int i=0; i<length; i++) {
	            preparedStatement.setString(i+1, values.get(i));
	            }
	                preparedStatement.executeUpdate();
	                
	                
	            }
	           
	        }
	        System.out.println("Finished..!");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return process;
	    }
	
	
}
