package test.edu.upenn.cis455.hw1;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.upenn.cis455.container.*;
import edu.upenn.cis455.webserver.*;
import junit.framework.TestCase;

public class HelloWorldTest extends TestCase {

	public void testRequestUrl() throws IOException
	{
		
		Socket c = new Socket();
		String request = "GET /demo?num1=2 HTTP/1.1\r\nHost: localhost:777";
		Parser p = new Parser(request,c);
		p.parse();
		Request r = new Request(1,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);

			String[] req_line = p.message[0].split(" ");
			String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
			//  	System.out.println(post_param);
			
			
 	    			SimpleResponse serv_res = new SimpleResponse(c);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(null,r,"/demo",serv_res);
	    			assertEquals("/demo",serv_req.getRequestURI());
	}
	public void testQueryString() 
	{
		Socket c = new Socket();
		String request = "GET /demo?num1=2 HTTP/1.1\r\nHost: localhost:777";
		Parser p = new Parser(request,c);
		p.parse();
		Request r = new Request(1,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);

			String[] req_line = p.message[0].split(" ");
			String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
			//  	System.out.println(post_param);
			
			
 	    			SimpleResponse serv_res = new SimpleResponse(c);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(null,r,"/demo",serv_res);
	    			assertEquals("num1=2",serv_req.getQueryString());
	}
	public void testSetHeader()
	{
		Socket c = new Socket();
		String request = "GET /demo?num1=2 HTTP/1.1\r\nHost: localhost:777";
		Parser p = new Parser(request,c);
		p.parse();
		Request r = new Request(1,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);

			String[] req_line = p.message[0].split(" ");
			String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
			//  	System.out.println(post_param);
			
			
 	    			SimpleResponse serv_res = new SimpleResponse(c);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(null,r,"/demo",serv_res);
	    			HashMap<String,Object> m =new HashMap<String,Object>();
	    			m.put("Content-Length","4");
	    			serv_res.setHeader("Content-Length", "4");
	    			assertEquals(m.entrySet(),serv_res.headers.entrySet());
	}
	public void testContentType()
	{
		Socket c = new Socket();
		String request = "GET /demo?num1=2 HTTP/1.1\r\nHost: localhost:777";
		Parser p = new Parser(request,c);
		p.parse();
		Request r = new Request(1,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);

			String[] req_line = p.message[0].split(" ");
			String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
			//  	System.out.println(post_param);
			
			
 	    			SimpleResponse serv_res = new SimpleResponse(c);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(null,r,"/demo",serv_res);
	    			assertEquals("text/html",serv_res.getContentType());
	}
	public void testSession()
	{
		Socket c = new Socket();
		String request = "GET /demo?num1=2 HTTP/1.1\r\nHost: localhost:777";
		Parser p = new Parser(request,c);
		p.parse();
		Request r = new Request(1,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);

			String[] req_line = p.message[0].split(" ");
			String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
			//  	System.out.println(post_param);
			
			
 	    			SimpleResponse serv_res = new SimpleResponse(c);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(null,r,"/demo",serv_res);
	    			SimpleSession s=  new SimpleSession(serv_res);
	    			int x=10;
	    			s.setMaxInactiveInterval(10);
	    			assertEquals(x,s.getMaxInactiveInterval());
	}
}
