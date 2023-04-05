package SYNC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class Compare {
	
	public Object summa(Connection mastercon,Connection remotecon,String MasterIp,String MasterUser,String MasterPass,String Database) {
		
		try
        {
			Object data="";
             Class.forName("com.mysql.jdbc.Driver");
//             localcon=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/sample", MasterUser, MasterPass);
             Statement st=mastercon.createStatement();
             System.out.println("connection established successfully...!!"); 


             ResultSet rs = st.executeQuery("show tables");
//             RequestDispatcher rd = req.getRequestDispatcher("/index.html");
//             rd.include(req, res);

//             remotecon=DriverManager.getConnection("jdbc:mysql://"+ClientIP+":3306/test",ClientUser,ClientPass);
             Statement localst = remotecon.createStatement();
             
             Connection newcon=DriverManager.getConnection("jdbc:mysql://"+MasterIp+":3306/"+Database, MasterUser, MasterPass);
             Statement newst=newcon.createStatement();
             
                 while(rs.next())
                 {
                	 System.out.println("again");
                	 String table = rs.getString(1);
                	 
                	 
                	 ResultSet localrs = localst.executeQuery("show tables");
                	 
                	 
                	 boolean check = false;
                	 while(localrs.next()){
                		 System.out.println(table);
	                	 String localtable = localrs.getString(1);
	                    
	                    if(table.equals(localtable)){
	                    	System.out.println("Exits");
	                    	check = true;
	                    	break;
	                    }
	                    else {
	                    	System.out.println("Not Exits");
	                    }
	                    
	//                	pw.println("<tr><td>"+rs.getObject(1)+"</td></tr>");
//	                    System.out.println("innerloop");
                	 }
                	 if(check==false){
                		 boolean once=false;
                		 ResultSet tableStrcture = newst.executeQuery("desc "+table);
                		 int count=0;
                		 while(tableStrcture.next()) {
                    		 String field = tableStrcture.getString(1);
                        	 String datatype = tableStrcture.getString(2);
                        	 String key = tableStrcture.getString(4);
                        	 if(count==0){
                        		 if(key.equals("PRI") && once==false) {
                        			 localst.executeUpdate("Create table "+table+"("+field+" "+datatype+" not null primary key)");
                        			 once=true;
                        		 }
//                        		 else if(key.equals("MUL")) {
//                        			 localst.executeUpdate("Create table "+table+"("+field+" "+datatype+" foreign key)");
//                        		 }
                        		 else {
                        			 localst.executeUpdate("Create table "+table+"("+field+" "+datatype+" not null )");
                        		 }
                        		 
                        		 count+=1;
                        	 }
                        	 else {
                        		 if(key.equals("PRI")&& once==false) {
                        			 localst.executeUpdate("alter table "+table+" add "+field+" "+datatype+"not null primary key");
                        			 once=true;
                        		 }
//                        		 else if(key.equals("MUL")) {
//                        			 localst.executeUpdate("alter table "+table+" add "+field+" "+datatype+" foreign key"); 
//                        		 }
                        		 else {
                        			 localst.executeUpdate("alter table "+table+" add "+field+" "+datatype+" not null");
                        		 }
                        		  
                        		 
                        	 }
                        	 
                		 }
                	 }
                ResultSet tableData = newst.executeQuery("select * from "+table);
                 ResultSetMetaData rsmd = (ResultSetMetaData) tableData.getMetaData();
                 int noOfColumns = rsmd.getColumnCount();
                 while(tableData.next()) {
                 Object tableValue="";
//                 Object data="";
                 for(int i=1;i<=noOfColumns;i++) {
                 if(i==noOfColumns && (rsmd.getColumnType(i)==12 || rsmd.getColumnType(i)==91 || rsmd.getColumnType(i)==16 || rsmd.getColumnType(i)==92 || rsmd.getColumnType(i)==1)){
                 tableValue +="'"+tableData.getObject(i)+"')";
                 data +=" "+tableData.getObject(i);
                 break;
                 }
                 else if(i==noOfColumns && (rsmd.getColumnType(i)!=12 || rsmd.getColumnType(i)!=91 || rsmd.getColumnType(i)!=16 || rsmd.getColumnType(i)!=92 || rsmd.getColumnType(i)!=1)){
                 tableValue +=tableData.getObject(i)+")";
                 data +=" "+tableData.getObject(i);
                 break;
                 }
                 else if(rsmd.getColumnType(i)==12 || rsmd.getColumnType(i)==91 || rsmd.getColumnType(i)==16 || rsmd.getColumnType(i)==92 || rsmd.getColumnType(i)==1){
                 tableValue +="'"+tableData.getObject(i)+"', ";
                 data +=" "+tableData.getObject(i)+", ";
                 }
                 else {
                 tableValue += tableData.getObject(i)+" , ";
                 data +=" "+tableData.getObject(i)+", ";
                 }
                 
                 }
                 localst.executeUpdate("insert ignore into " +table+" values("+tableValue);
                 
                 }
                 }
                 return data;
        }
        catch (Exception e1){
        	System.out.println("i am kutty catch");
            e1.printStackTrace();
        }
		return null;
	}
	public boolean universalChange(Connection localcon, Connection remotecon, boolean check,String queryinput) {
		boolean result;
		try {
			Statement localst = localcon.createStatement();
			Statement remotest = remotecon.createStatement();
			String sql = queryinput;
			if(check==true) {
				localst.executeUpdate(sql);
				check=false;
			}
			remotest.executeUpdate(sql);
			result = true;
			return result;
		}
		catch(Exception e){
			result = false;
			System.out.println(e.getMessage());
			System.out.println("i am main error");
			return result;
		}
	
	}
	public Object summa2(Connection mastercon,Connection remotecon,String MasterIp,String MasterUser,String MasterPass,String userip,String username,String userpass,String DatabaseName) {
		Object tableValues =null;
		try {
			remotecon=DriverManager.getConnection("jdbc:mysql://"+userip+":3306/"+DatabaseName,username,userpass);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("con catch");
			e.printStackTrace();
		}
		
//		boolean status = universalChange(mastercon, remotecon, true, query);
//		System.out.println("status ="+status);
		tableValues=summa(mastercon, remotecon, MasterIp, MasterUser, MasterPass,DatabaseName);
		
//		if(status==false) {
////			Compare obj = new Compare();
//			tableValues=summa(mastercon, remotecon, MasterIp, MasterUser, MasterPass,DatabaseName);
//			universalChange(mastercon, remotecon, false, query);
//		}
		return tableValues;
		}
	}

