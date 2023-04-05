package SYNC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.pastdev.jsch.DefaultSessionFactory;
import com.pastdev.jsch.command.CommandRunner;
import com.pastdev.jsch.command.CommandRunner.ExecuteResult;

public class CsvFileReader {
	
	public void CsvFileSaver(String ip,String user,String pass,String Filepath) throws JSchException, IOException {
		DefaultSessionFactory sessionFactory = new DefaultSessionFactory(
                user,ip, 22
        );
        Map props = new HashMap<String, String>();
        props.put("StrictHostKeyChecking", "no");
        sessionFactory.setConfig(props);
        sessionFactory.setPassword(pass);

        CommandRunner runner = new CommandRunner(sessionFactory);

        String command = "cat "+Filepath ;
        ExecuteResult result = runner.execute(command);


        if (result.getStderr().isEmpty()) {
            String input = result.getStdout();
            try{
                FileWriter fw = new FileWriter("/home/gokul/input.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(input);
                bw.close();
                System.out.println("Saved successfully");
            }
            catch (Exception ee){
                System.out.println("Error");
            }
        } else {
            System.out.println(result.getStderr());
        }

        runner.close();
	}
}
