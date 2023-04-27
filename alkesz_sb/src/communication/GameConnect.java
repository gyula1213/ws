package communication;

public interface GameConnect extends AutoCloseable {
	public boolean __Init__() throws Exception;
	public void Send()throws Exception;
	public String Receive()throws Exception;
	public void close()throws Exception;
	public void Set_String(String data)throws Exception;
}
