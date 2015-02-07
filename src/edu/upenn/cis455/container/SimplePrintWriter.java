package edu.upenn.cis455.container;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import edu.upenn.cis455.webserver.*;

public class SimplePrintWriter extends PrintWriter{
	
	private final StringBuffer  buffer= new StringBuffer();
	public SimplePrintWriter(Socket client, boolean autoFlush) throws IOException {
		super(client.getOutputStream(),false);
		// TODO Auto-generated constructor stub
	}
	public void print(String s)
	{
		buffer.append(s);
	}
	public void println(String s)
	{
		buffer.append(s+"\n");
	}
	public void flush()
	{
		synchronized(lock)
		{
			super.println(buffer);
			super.flush();
		}
	}
	
	

}
