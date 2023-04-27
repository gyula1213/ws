package communication;
import java.io.*;
import java.net.*;

public class Server implements GameConnect{
	
	private ServerSocket ss;
	private Socket s;
	private PrintStream ps; //Send data to the server
	private BufferedReader br;
	private String Sztring_buffer;
	
	public Server(int port)
			throws Exception
    {
		// Create server Socket
        ss = new ServerSocket(port);
        System.out.println("Server ok");
		
    }
	public void Set_String(String data)throws Exception {
		Sztring_buffer = String.copyValueOf(data.toCharArray());
	}

	public boolean __Init__()
			throws Exception
    {
    
        // connect it to client socket
        s = ss.accept();
        
        if(s==null)
        	return false;
  
        // to send data to the client
        ps= new PrintStream(s.getOutputStream());
  
        // to read data coming from the client
        br= new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        if(s!=null && ps!=null &&br!=null)
        	return true;
        else
        	return false;	
  
 
    }
	
	public void Send()throws Exception
	{
		ps.println(Sztring_buffer + "\n");				
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
    	 ps.close();
         br.close();
         ss.close();
         s.close();
     }

}
