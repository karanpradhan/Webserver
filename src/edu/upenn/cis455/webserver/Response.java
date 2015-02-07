package edu.upenn.cis455.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
/*
 * This class is responsible to write the response on the output stream.
 * It handles the different HTTP error codes as parsed by the parser class.
 */

public class Response {

	private String method;
	private String url;
	private String version;
	private String ext;
	private Socket client;
	
	LinkedHashMap<String,String> headers ;
	/*
	 * Constructor for the class
	 * Creates the object from the request parsed.
	 * @param Request
	 */
	Response(Request r,Socket c)
	{
		this.method=r.getMethod();
		this.url=r.getUrl();
		this.version=r.getVersion();
		this.headers = r.getHeaders();
		this.ext=r.getExt();
		this.client = c;
	}
	/*
	 * This method is responsible for writing the response on the output stream.
	 * @exception Exception When some internal server error occurs
	 * 
	 */
	void write() throws Exception
	{
		//System.out.println(client);
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);

		//System.out.println(out);
		//File f =new File(System.getProperty("user.dir") + "/resources/Files/"+url);
		//out.print("Hello");
		//System.out.println("Inside write");
		//System.out.println("File>>> "+f);

		String currentLine;
		try
		{
			//System.out.println("Inside try");
			BufferedReader brFile=null;
			BufferedInputStream br=null;

			
			//out.print(version+" "+"200 OK"+"\n");

			//System.out.println(date);
			//System.out.println("Status is "+version+" "+"200 OK"+"\n");
			//System.out.println(headers);
			if((!version.equals("HTTP/1.0") && headers.containsValue(" 200 OK") && Parser.continue100) )
			{
				out.println("HTTP/1.1 100 Continue");
				out.println();
			}
			
			for(Map.Entry<String,String> me : headers.entrySet())
			{
				out.println(me.getKey()+me.getValue());
			}
			out.println("");
			
			if(headers.containsValue(" 400 Bad Request"))
			{
				out.println("<HTML><BODY><H1>Bad Request</H1></BODY></HTML>");
			}
			
			if(headers.containsValue(" 304 Not Modified"))
			{
				out.println("<HTML><BODY><H1>304 Not Modified</H1></BODY></HTML>");
			}
			
			if(headers.containsValue(" 412 Precondition Failed"))
			{
				out.println("<HTML><BODY><H1>412 Precondition Failed</H1></BODY></HTML>");
			}
			
			if(headers.containsValue(" 404 Not Found"))
			{
				out.println("<HTML><BODY><H1>Not Found</H1></BODY></HTML>");
			}
			
			if(headers.containsValue(" 403 Forbidden"))
			{
				out.println("<HTML><BODY><H1>Forbidden</H1></BODY></HTML>");
			}
			if(headers.containsValue(" 505 HTTP Version not supported"))
			{
				out.println("<HTML><BODY><H1>HTTP Version not supported</H1></BODY></HTML>");
			}
			
			if(headers.containsValue(" 501 Not Implemented"))
			{
				out.println("<HTML><BODY><H1>501 Not Implemented</H1></BODY></HTML>");
			}
			
			
			if(headers.containsValue(" 200 OK") && method.equals("GET")){
				
				
				
				
				if(ext.equals("txt")||ext.equals("html")||ext.equals("htm")||ext.equals("xml"))
				{	
					brFile = new BufferedReader(new FileReader( System.getProperty("user.dir") + "/"+this.url));
					//System.out.println(status);
				}
				else
				{	
					//System.out.println(this.url);
					br= new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/"+this.url));

				}
				
				
				if(ext.equals("txt")||ext.equals("html")||ext.equals("htm")||ext.equals("xml"))
				{
					while((currentLine = brFile.readLine())!=null)
					{
						out.println(currentLine); 
					}	
					//System.out.print(headers);
					//out.println("Connection :close");
					brFile.close();
					out.close();
				}
				else
				{
					int data;
					BufferedOutputStream out1=new BufferedOutputStream(client.getOutputStream());
				//	System.out.println("BufferedOutput Stream is writing "+url);
					while((data=br.read())!=-1)
					{
						out1.write(data);
					}
					//out.println("Connection :close");
					br.close();
					out1.close();
				}
			}
		}
		catch(Exception e)
		{
			out.print(version+" 500 Internal Server Error\n");
			out.print("Server: HTTP Server");
			out.println("Content-Type: text/html");
			out.println("Content-Length: "+ "<HTML><BODY><H1>Internal Server Error</H1></BODY></HTML>".length()+"");
			out.println("Connection : close");
			out.println();
			out.println("<HTML><BODY><H1>Internal Server</H1></BODY></HTML>");
			e.printStackTrace();
			out.close();

		}
		client.close();


	}
	
	void writeControl(StringBuilder buff) throws Exception
	{
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		if((!version.equals("HTTP/1.0") && headers.containsValue(" 200 OK")) )
		{
			out.println("HTTP/1.1 100 Continue");
			out.println();
		}
		headers.put("Content-Length: ", buff.length()+"");
		for(Map.Entry<String,String> me : headers.entrySet())
		{
			out.println(me.getKey()+me.getValue());
		}
		out.println("");
		out.println(buff);
	}
	

}
