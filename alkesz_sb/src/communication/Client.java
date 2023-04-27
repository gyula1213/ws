package communication;
import java.io.*;
import java.net.*;

public class Client implements GameConnect{
	private Socket s; //Client Socket
	private DataOutputStream dos; //Send data to the server
	private BufferedReader br;
	private String Sztring_buffer;

	public void Set_String(String data)throws Exception {
		Sztring_buffer = String.copyValueOf(data.toCharArray());
	}
	
	public Client(InetAddress ip, int port)
			throws Exception
    {
		// Create client socket
		s = new Socket(ip, port);
		System.out.println("Kliens ok");
    }
	public boolean __Init__()
			throws Exception
    {
		
        // to send data to the server
       dos= new DataOutputStream(s.getOutputStream());
  
        // to read data coming from the server
        br= new BufferedReader(new InputStreamReader(s.getInputStream()));
        if(s!=null && dos!=null &&br!=null)
        	return true;
        else
        	return false;
    }
	
	public void Send()throws Exception
	{
		dos.writeBytes(Sztring_buffer + "\n");				
	}
	
	public String Receive()
			throws Exception
	{
		String receive=br.readLine();
		return receive;
	}
    
        
     public void close()
    		 throws Exception
     {
        dos.close();
        br.close();
        s.close();
     }

}
