package edu.upenn.cis455.webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Parser class has a number of members for creating the headers for the response
 * It also parses the incoming HTTP requests and sets appropriate headers accordingly.
  
*/
public class Parser {
	public String request;
	public String method;
	public String url;
	public String version;
	public LinkedHashMap<String,String> headers;
	public String optMessage;
	public String ext;
	public Socket client;
	public String host;
	public String ifmod;
	public String ifunmod;
	public Date if_mod;
	public Date if_unmod;
	public boolean ifmodbool;
	public boolean ifunmodbool;
	static public boolean continue100;
	public HashMap<String,String> requestedHeaders;
	public String[] message;
	public boolean err;
	/*
	 * Constructor for the parser.
	 * @param String The client HTTP request in string format
	 * @param Socket The client socket 
	 */
	public Parser(String request,Socket client)
	{
		continue100=false;
		this.request=request;
		method="";
		url="";
		version="";
		headers=new LinkedHashMap<String,String>();
		optMessage="";
		ext="";
		this.client=client;
		ifmodbool = false;
		ifunmodbool = false;
		requestedHeaders=new HashMap<String,String>();
		err=false;
	}
	/*
	 * This method is user for paring the HTTP request and raises the appropriate exceptions and headers as and when required.
	 * 
	 */
	public void parse()
	{
		String type="";
		String err_message = "";
		String location="";
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		try{
		//String type="";
		
			
		message = request.split("\r\n");
		String[] temp = message[0].split(" ");
		method=temp[0];
		version=temp[2];
		
		for(int i=1;i<message.length;i++)
		{
			String[] hdrs = message[i].split(": ");
			if(requestedHeaders.containsKey(hdrs[0]))
			{
				String x = requestedHeaders.get(hdrs[0]);
				requestedHeaders.put(hdrs[0], x+"; "+hdrs[1]);
			}
			else
				requestedHeaders.put(hdrs[0], hdrs[1]);
		}
	
		if(!(method.equals("GET")||method.equals("HEAD")))
		{
			err_message ="501 Not Implemented";
			headers.put(version," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Not Implemented</H1></BODY></HTML>".length()+"");
			headers.put("Connection: ", "close");
			return;
		}
		
		if(version.equals("HTTP/1.1")){
			for(int i=1;i<message.length;i++)
			{	
			try{
			if(message[i].contains("Host: ")){	
				host = message[i];
				if((host.equals("") || host==null || host.length()==0 || !message[i].contains("Host: ")))
					throw new Exception("host exception");
			}
			}
			catch(Exception e)
			{
				//e.printStackTrace();
				err=true;
				err_message ="400 Bad Request";
				headers.put("HTTP/1.1"," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				headers.put("Content-Type: ", "text/html");
				headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
				headers.put("Connection: ", "close");
				return;
			}
			if(message[i].contains("If-Modified-Since"))
			{
				String temp_date=message[i].substring(message[i].indexOf(":")+1);
				ifmod = temp_date.trim();
				//System.out.println(ifmod);
				try{
				ifmod = df.format(df.parse(ifmod));
				if_mod = df.parse(ifmod);
				}
				catch(ParseException e)
				{
					err_message ="400 Bad Request";
					headers.put("HTTP/1.1"," "+err_message);
					headers.put("Date: ", date);
					headers.put("Server: ", "HTTP Server");
					headers.put("Content-Type: ", "text/html");
					headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
					headers.put("Connection:", "close");
					return;
				}
				ifmodbool = true;
				//System.out.println(ifmod);
			}
			if(message[i].contains("If-Unmodified-Since"))
			{
				String temp_date=message[i].substring(message[i].indexOf(":")+1);
				ifunmod = temp_date.trim();
			//	if_unmod = df.parse(ifunmod);
				try{
				ifunmod = df.format(df.parse(ifunmod));
				if_unmod = df.parse(ifunmod);
				}
				catch(ParseException e)
				{
					err_message ="400 Bad Request";
					headers.put("HTTP/1.1"," "+err_message);
					headers.put("Date: ", date);
					headers.put("Server: ", "HTTP Server");
					headers.put("Content-Type: ", "text/html");
					headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
					headers.put("Connection:", "close");
					return;
				}
				ifunmodbool = true;
				//System.out.println(ifunmod);
			}
			String temp_con = message[i].toLowerCase().replaceAll(" ","");
			if(temp_con.contains("expect:100-continue"))
			{
				continue100 = true;
				System.out.println(continue100);
			}
				
		   }
			if(!request.contains("Host: "))
			{
				err=true;
				err_message ="400 Bad Request";
				headers.put("HTTP/1.1"," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				headers.put("Content-Type: ", "text/html");
				headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
				headers.put("Connection: ", "close");
				return;
			}
		}
		if(temp[1].contains(System.getProperty("user.dir")))
		{
			int idx = temp[1].indexOf(System.getProperty("user.dir"))+System.getProperty("user.dir").length();
			temp[1]=temp[1].substring(idx);
		}
		
		if(!(temp[1].charAt(0)=='/'))
		{
			err=true;
			err_message ="400 Bad Request";
			headers.put("HTTP/1.1"," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
			headers.put("Connection:", "close");
			return;
		}
		
		url=temp[1].substring(1);
		if(url.contains("%20"))
		{
			url=url.replaceAll("%20", "\\ ");
			//System.out.println("The url is"+url);
		}
		if(url.contains("/*"))
    	{
			url=url.replaceAll("/\\*", "");
    	}
    	
		
		
		String url_test=(new File(url)).getCanonicalPath();
		//System.out.println(url_test);
		//System.out.println(System.getProperty("user.dir"));
		if(!url_test.startsWith(System.getProperty("user.dir")))
		{
			err_message ="403 Forbidden";
			headers.put(version," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Forbidden</H1></BODY></HTML>".length()+"");
			headers.put("Connection: ", "close");
			return;
		}
		if(!((version.equals("HTTP/1.1") || version.equals("HTTP/1.0"))))
		{
			err=true;
			err_message ="505 HTTP Version not supported";
			headers.put("HTTP/1.1"," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>HTTP version not supported</H1></BODY></HTML>".length()+"");
			headers.put("Connection: ", "close");
			return;
		}
		if(url.equals("control")&&method.equals("GET")&&(version.equals("HTTP/1.1")||version.equals("HTTP/1.0")))
		{
		//	System.out.println("Control requested");
			return;
		}
		
		
		
		location = System.getProperty("user.dir") + "/" +url;
		if(new File(location).exists()){
			if(new File(location).isDirectory()){
				
				
			System.out.println("Reading a directory");	
			directory(location,url);
			return;
			}
		}
		else
		{
			err_message ="404 Not Found";
			headers.put(version," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Not Found</H1></BODY></HTML>".length()+"");
			headers.put("Connection:", "close");
			return;
		}
		
		
		String file=temp[1].substring(url.lastIndexOf("/")+1);
	//	System.out.println("File name extracted is "+file);
		if(file.contains("."))
		{
			//System.out.println(file);
			//String[] extension = file.split(".");
			//String[] extension = {"hello","jpg"};
			Matcher m = Pattern.compile(".*/.*?(\\..*)").matcher(file);
			if(m.matches()){
				ext=m.group(1).substring(1);
			}
			
			//System.out.println(Arrays.toString(extension));
			
			//ext=extension[1];
			//String fname=extension[0];
			
			if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")||ext.equals("ico")||ext.equals("gif"))
				type="image";
			if(ext.equals("jpg"))
				ext="jpeg";
			if(ext.equals("html")||ext.equals("htm")||ext.equals("xml")||ext.equals("css"))
				type="text";
			if(ext.equals("txt"))
			{
				type="text";
				ext="plain";
			}
			if(ext.equals("pdf"))
				type="application";
		}	
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			
			err_message ="400 Bad Request";
			headers.put("HTTP/1.1"," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
			headers.put("Connection: ", "close");
			return;
			
		}
		
		
		
			
		if(new File(System.getProperty("user.dir") + "/"+this.url).isFile())
		{
			String last_modified = df.format(new File(System.getProperty("user.dir") + "/"+url).lastModified());
			Date last_mod=null;
			try {
				last_mod = df.parse(last_modified);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				err_message ="400 Bad Request";
				headers.put("HTTP/1.1"," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				headers.put("Content-Type: ", "text/html");
				headers.put("Content-Length: ", "<HTML><BODY><H1>Bad Request</H1></BODY></HTML>".length()+"");
				headers.put("Connection:", "close");
				return;
			}
			if(ifmodbool)  
			{
				if(if_mod.after(last_mod)){
				err_message="304 Not Modified";
				headers.put(version," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				headers.put("Connection:", "close");
				}
				else
				{	
				err_message="200 OK";
				headers.put(version," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				//headers.put("Last-Modified: ", last_modified);
				headers.put("Content-Type: ",type+"/"+ext);
				headers.put("Content-Length: ", new File(System.getProperty("user.dir") + "/"+url).length()+"");
				headers.put("Connection:", "close");
				}
			}
			else if(ifunmodbool)
			{
				if(if_unmod.before(last_mod))
				{
				err_message="412 Precondition Failed";
				headers.put(version," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				headers.put("Connection:", "close");
				}
				else
				{	
				err_message="200 OK";
				headers.put(version," "+err_message);
				headers.put("Date: ", date);
				headers.put("Server: ", "HTTP Server");
				//headers.put("Last-Modified: ", last_modified);
				headers.put("Content-Type: ",type+"/"+ext);
				headers.put("Content-Length: ", new File(System.getProperty("user.dir") + "/"+url).length()+"");
				headers.put("Connection:", "close");
				}
				
			}
			else
			{	
			err_message="200 OK";
			headers.put(version," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			//headers.put("Last-Modified: ", last_modified);
			headers.put("Content-Type: ",type+"/"+ext);
			headers.put("Content-Length: ", new File(System.getProperty("user.dir") + "/"+url).length()+"");
			headers.put("Connection:", "close");
			}
		}
		else
		{
			err_message="404 Not Found";
			headers.put(version," "+err_message);
			headers.put("Date: ", date);
			headers.put("Server: ", "HTTP Server");
			headers.put("Content-Type: ", "text/html");
			headers.put("Content-Length: ", "<HTML><BODY><H1>Not Found</H1></BODY></HTML>".length()+"");
			headers.put("Connection: ", "close");
		}
		
	}
	
	/*
	 * This method is exclusively used for the directory listing. 
	 * @param String Location of the directory.
	 * @param String Url of the directory requested.
	 */
	
	void directory(String location,String url)
	{	
		final File folder = new File(location);
		ArrayList<String> file_list = new ArrayList<String>();
		ArrayList<String> dir_list = new ArrayList<String>();
		for( final File entry:folder.listFiles())
		{
			if(entry.isDirectory())
			{
				dir_list.add(entry.getName());
			}
			if(entry.isFile())
			{
				file_list.add(entry.getName());
				//System.out.println("asdfasfd+"+entry.getName());
			}
		}
		File dir_html = new File(location+"/dir.html");
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(dir_html));
			out.write("<HTML>");
			out.write("<BODY>");
			out.write("<H1>DIRECTORY LIST</H1>");
			out.write("<UL>");
			for(String i:dir_list)
			{
				out.write("<LI><A HREF=\""+url+"/"+i+"\">"+i+"</A></LI>");
			}
			out.write("</UL>");
			out.write("<H1>FILE LIST</H1>");
			out.write("<UL>");
			for(String i:file_list)
			{
				out.write("<LI><A HREF=\""+url+"/"+i+"\">"+i+"</A></LI>");
			}
			out.write("</UL>");
			out.write("</BODY>");
			out.write("</HTML>");
			out.close();
			System.out.println("Writing the directory list");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.url=url+"/"+"dir.html";
		
		String err_message="200 OK";
		headers.put(version," "+err_message);
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		headers.put("Date: ", date);
		headers.put("Server: ", "HTTP Server");
		
		String last_modified = df.format(dir_html.lastModified());
		//headers.put("Last-Modified: ", last_modified);
		headers.put("Content-Type: ","text/html");
		//System.out.println("file is "+location+"/"+url);
		headers.put("Content-Length: ", new File(location+"/"+"dir.html").length()+"");
		headers.put("Connection: ", "close");
		
		
	}
	
}
