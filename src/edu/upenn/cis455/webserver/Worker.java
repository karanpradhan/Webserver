package edu.upenn.cis455.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import edu.upenn.cis455.container.SimpleRequest;
import edu.upenn.cis455.container.SimpleResponse;
import edu.upenn.cis455.container.SimpleSession;

/*
 * The worker class which implements the interface runnable of the implementation of threads
 * The threads accept request, parse it and hand it over to the response object.
 */



public class Worker implements Runnable
{
	Thread t;
	public String state;
	public int id;
	//static volatile Boolean shutdown;
	BlockingQueue q;
	Object shutdown_lock;
	BufferedReader br;
	PrintWriter out;
	Socket client;
	StringBuilder buff;
	boolean timer = false;
	boolean servlet_request ;
	static Logger logger = Logger.getLogger(Worker.class);
	/*
	 * Constructor for the class worker. 
	 * @param int Identify the threads
	 * @param BlockingQueue Reference to the blocking queue to dequeue sockets.
	 * @param Object Shutdown lock for synchronizing the shutdown signal
	 * 
	 */
	Worker(int id,BlockingQueue q,Object shutdown_lock){
		this.q=q;
		this.id=id;
	//	this.shutdown = shutdown;
		t=new Thread(this,"");
		t.start();
		//state="waiting";
		this.shutdown_lock=shutdown_lock;
		client = null;
	}
	/*
	 * This method does the work of dequeuing the socket, parsing and sending the request to the resposne object.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	public void run(){
		int i=1;
		
		while(!HttpServer.shutdown){
			try{
				t.setName("WAITING,"+(id+1));
			//System.out.println("Worker "+id+" is working");
		
			//if(!q.isEmpty())
			//	System.out.println(q.print());
			
			//	System.out.println("Worker "+id+" got value "+q.remove());
			//Request req= q.remove();
			//Response res = new Response(req);
			//res.write();
				
				
				
				
			client = q.remove();
			/*
			try {
				client.setSoTimeout(15000);
				timer = true;
				
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				//e2.printStackTrace();
				//System.out.println("Timed out");
				continue;
			}
			*/
			servlet_request = false;
			System.out.println("Worker "+id+" is processing request "+i);
			InputStreamReader in = new InputStreamReader(client.getInputStream());
	        br = new BufferedReader(in);
	    	out = new PrintWriter(client.getOutputStream(),true);
	    	String request;
	    	StringBuffer r=new StringBuffer();
	    	int len=0;
	    	boolean ispost = false;
	    	while((request = br.readLine()) != null && (request.length()!=0)){
	    		//System.out.println("Inside");
	    		r.append(request+"\r\n");
	    		if(request.contains("Content-Length: "))
	    		{
	    			String temp = request.substring("Content-Length: ".length());
	    			len = Integer.parseInt(temp);
	    		}
	    		
	    	}
	    	while(len!=0){
	    	r.append((char)br.read());
	    	len=len-1;
	    	}
	    	String concat = new String(r);
	    	synchronized(shutdown_lock)
	    	{
	    	if(concat.contains("GET /shutdown HTTP/1.1") || concat.contains("GET /shutdown HTTP/1.0"))
	    	{
	    		System.out.println("Shutting down "+id);
	    		HttpServer.shutdown=true;
	    		br.close();
	    		out.close();
	    		client.close();
	    		HttpServer.serverSocket.close();
	    		shutdown_lock.notifyAll();
	    		for(String iter:HttpServer.servlets.keySet())
	    		{
	    			HttpServer.servlets.get(iter).destroy();
	    		}
	    		break;
	    	}
	    	
	    	}
	    	System.out.println(concat);
	    	Parser p = new Parser(concat,client);
	    	p.parse();
	    	Request req =  new Request(i,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client,p.host,p.requestedHeaders,p.request);
	    	if(p.url.contains("/*"))
	    	{
	    		p.url=p.url.replaceAll("/\\*", "");
	    	}
	    	
	    	if(!p.err)
	    	{
	    	String[] req_line = p.message[0].split(" ");
	    	String post_param="";
			if(p.method.equals("POST"))
			{
				post_param = p.message[p.message.length-1];
			}
//	    	System.out.println(post_param);
	    	LinkedHashMap<String,String> array_req = new LinkedHashMap<String,String>();
	    	
	    	for(int itr = 0;itr<req_line.length-1;itr++)
	    	{
	    		if(req_line[itr].equals("POST"))
	    		{
	    			req_line[itr+1]=req_line[itr+1]+"?"+post_param;
	    		}
//	    		System.out.println(req_line[itr]+" "+req_line[itr+1]);
	    		if(req_line[itr].equals("GET")||req_line[itr].equals("POST")||req_line[itr].equals("HEAD"))
	    		{
	    			String temp = req_line[itr+1];
	    			if(temp.contains("/*"))
	    	    	{
	    	    		temp=temp.replaceAll("/\\*", "");
	    	    	}
	    			
	    			if(temp.contains("?"))
	    			{
	    				int qidx = temp.indexOf("?");
	    				temp = temp.substring(0,qidx);
	    			}
	    			array_req.put(req_line[itr]+" "+req_line[itr+1], temp);
	    		}
	    	}
	    	
	    	for(String req_mes : array_req.keySet())
	    	{
	    	if(HttpServer.urlmapping.containsKey(array_req.get(req_mes))){
	    		servlet_request = true;
	    		SimpleSession fs = null;
	    		String[] firstline = req_mes.split(" ");
 	    			SimpleResponse serv_res = new SimpleResponse(client);
 	    			
	    			SimpleRequest serv_req = new SimpleRequest(fs,req,array_req.get(req_mes),serv_res);
	    			serv_res.setRequest(serv_req);
	    			
	    			String[] strings = firstline[1].split("\\?|&|=");
	    			
	    			if(strings[0].contains(System.getProperty("user.dir")))
	    			{
	    				int idx = strings[0].indexOf(System.getProperty("user.dir"))+System.getProperty("user.dir").length();
	    				strings[0]=strings[0].substring(idx);
	    			}
	    			if(strings[0].contains("/*"))
	    	    	{
	    				strings[0]=strings[0].replaceAll("/\\*", "");
	    	    	}
	    			
	    			HttpServlet servlet = HttpServer.servlets.get(HttpServer.urlmapping.get(strings[0]));
	    			if (servlet == null) {
	    				System.err.println("error: cannot find mapping for servlet " + strings[0]);
	    				//System.exit(-1);
	    			}
	    			for (int k = 1; k < strings.length - 1; k += 2) {
	    				if(strings[k+1].contains("+"))
	    					strings[k+1]=strings[k+1].replaceAll("+"," ");
	    				serv_req.setParameter(strings[k], strings[k+1]);
	    			}
	    			if (firstline[0].compareTo("GET") == 0 || firstline[0].compareTo("POST") == 0 || firstline[0].compareTo("HEAD") == 0) {
	    				serv_req.setMethod(firstline[0]);
	    				if(p.continue100)
	    				{
	    					out.println("HTTP/1.1 100 Continue");
	    					out.println();
	    				}
	    				try{
	    				servlet.service(serv_req,serv_res);
	    				serv_res.flushBuffer();
	    				
	    				}
	    				catch(Exception e)
	    				{
	    					
	    					logger.debug("Servlet name: "+HttpServer.urlmapping.get(strings[0])+" "+e);
	    					out.print(p.version+" 500 Internal Server Error\n");
	    					out.print("Server: HTTP Server");
	    					out.println("Content-Type: text/html");
	    					out.println("Content-Length: "+ "<HTML><BODY><H1>Internal Server Error</H1></BODY></HTML>".length()+"");
	    					out.println("Connection: close");
	    					out.println();
	    					out.println("<HTML><BODY><H1>Internal Server</H1></BODY></HTML>");
	    				}
	    				
	    				
	    				
	    				
	    			} else {
	    				System.err.println("error: expecting 'GET' or 'POST', not '" + firstline[0] + "'");
	    				//usage();
	    				//System.exit(-1);
	    			}
 	    		
	    			fs = (SimpleSession) serv_req.getSession(false);
	    		
	    		
	    		
	    	}
	    	}
	    	}
	    	if(!servlet_request){
	    	
	    	t.setName("/"+p.url);
	    	if(p.url.equals("control"))
	    	{
	    		
	    		// buff = new StringBuilder(1000);
	    		ThreadGroup tgrp = Thread.currentThread().getThreadGroup();
	    		Thread t_array[] = new Thread[tgrp.activeCount()];
	    		tgrp.enumerate(t_array);
	    		//System.out.println(t_array.length);
	    		File control=new File(System.getProperty("user.dir")+"/control.html");
	    		BufferedWriter fout =  new BufferedWriter(new FileWriter(control));
	    		fout.write("<HTML><BODY><H1>Karan Pradhan</H1><H2>SEAS id : karanpr</H2><A HREF=\"/shutdown\"><Input type='button' value='Shutdown' /></A><UL>");
	    		
	    		
	    		//System.out.println(t_array.length);
	    		for(int v=0;v<t_array.length;v++)
	    		{
	    			if(!(t_array[v].getName().equals("main") || t_array[v].getName().equals("Controller"))){
	    			fout.write("<LI> Thread "+v+" ");
	    			String s=t_array[v].getState().toString();
	    			if(s.equals("RUNNABLE"))
	    				fout.write(t_array[v].getName());
	    			else
	    				fout.write(s);
	    			fout.write("</LI>");
	    			}
	    		}
	    		fout.write("</UL>");
	    		fout.close();
	    		
	    		File log=new File("log/log.out");
	    		BufferedWriter fout_log =  new BufferedWriter(new FileWriter(control,true));
	    		fout_log.write("<H1>Servlet errors</H1>");
	    		fout_log.write("<UL>");
	    		BufferedReader bx = new BufferedReader(new FileReader(log));
	    		String t;
	    		while((t=bx.readLine())!=null && t.length()!=0)
	    		{
	    			String f = t.replace("WAITING,","Thread ");
	    			fout_log.write("<LI>"+f+"</LI>");
	    		}
	    		
	    		fout_log.write("</UL></BODY></HTML>");
	    		fout_log.close();
	    		p.url="control.html";
	    		req.setUrl(p.url);
	    		req.setExt("html");
	    		
	    		String err_message="200 OK";
	    		p.headers.put(p.version," "+err_message);
	    		Calendar now = Calendar.getInstance();
	    		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
	    		df.setTimeZone(TimeZone.getTimeZone("GMT"));
	    		String date = df.format(now.getTime());
	    		p.headers.put("Date: ", date);
	    		p.headers.put("Server: ", "HTTP Server");
	    		
	    		//String last_modified = df.format(dir_html.lastModified());
	    		//headers.put("Last-Modified: ", last_modified);
	    		p.headers.put("Content-Type: ","text/html");
	    		//System.out.println("file is "+location+"/"+url);
	    		p.headers.put("Content-Length: ", control.length()+"");
	    		p.headers.put("Connection: ", "close");
	    		
	//    		System.out.println("Control written");
	    		
	    	}
	    	
	    	//System.out.println("Client requested "+request);
	    	
	    	//t.setName("WAITING");
			i++;
	//		System.out.println("Reading "+p.url);
			Response res = new Response(req,p.client);
			try{
			
				
				res.write();
			
			}
			catch(Exception e){
				out.print(p.version+" 500 Internal Server Error\n");
				out.println("Content-Type: text/html");
				out.println("Content-Length: "+ "<HTML><BODY><H1>Internal Server Error</H1></BODY></HTML>".length()+"");
				out.println("<HTML><BODY><H1>Internal Server</H1></BODY></HTML>");
				out.println("Connection: close");
				e.printStackTrace();
				logger.debug(e);
				out.close();
			}
//			br.close();
//			out.close();
//			client.close();
			}
			}
			catch(Exception e){
				//out.println("");
				e.printStackTrace();
				if(HttpServer.shutdown){
				System.out.println("Shutting down "+id);
				try {
					if(client!=null)
						client.close();
					if(HttpServer.serverSocket!=null)
						HttpServer.serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					
					e1.printStackTrace();
				}
			//	e.printStackTrace();
				break;
			}
				if(timer ==  true){
				System.out.println("Timed out");
				out.print("HTTP/1.1 408 Request Timeout\n");
				out.println("Content-Type: text/html");
				out.println("Content-Length: "+ "<HTML><BODY><H1>Request Timeout</H1></BODY></HTML>".length()+"");
				out.println("<HTML><BODY><H1>Request Timeout</H1></BODY></HTML>");
				out.println("Connection: close");
				continue;}
				
			}
			finally
			{
				if(out!=null){
					out.close();
				}
				if(client!=null){
				
				try {
					client.close();
					//HttpServer.serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				}
				}
			}
			//this.state="waiting";
			
			
			
		}
	
	
	}
}
