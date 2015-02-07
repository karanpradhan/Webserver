package edu.upenn.cis455.container;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tjgreen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleResponse implements HttpServletResponse {

	public SimpleResponse(Socket client) 
	{
		
		this.client = client;
		headers=new HashMap<String,Object>();
		mywriter = new StringWriter();
		redirect=false;
		commit=false;
		set_status=false;
		encoding="ISO-8559-1";
		contentType="text/html";
		set_buff=false;
		
	}
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie arg0) {
		// TODO Auto-generated method stub
		
		long time = arg0.getMaxAge();
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		
		Date sysdate=null;
		try {
			sysdate = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long a=sysdate.getTime();
		long cookie_time = a+(time*1000);
		Date cookie_exp = new Date(cookie_time);
		String cookie_expires = df.format(cookie_exp);
		String cookie_name = arg0.getName();
		String cookie_value =arg0.getValue();
		String domain="";
		if(arg0.getDomain()!=null || arg0.getDomain()!="")
			domain=arg0.getDomain();
		headers.put("Set-Cookie",cookie_name+"="+cookie_value+"; Expires= "+cookie_expires);
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String arg0) {
		// TODO Auto-generated method stub
		if(headers.containsKey(arg0))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	public String encodeURL(String arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	public String encodeUrl(String arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		if(!commit)
		{
			String err = "<HTML><BODY><H2>Error: "+arg1+"</H2></BODY><HTML>";
			PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
			pw.println(req.getProtocol()+" "+arg0+" "+arg1);
			pw.println("Content-Type: text/html");
			pw.println("Content-Length: "+err.length());
			pw.println("Connection: close");
			pw.println();
			pw.println(err);
			pw.flush();
			pw.close();
			commit=true;
			
		}
		else
			throw new IOException();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub
		if(!commit)
		{
			String err = "<HTML><BODY><H2>Error: "+arg0+"</H2></BODY><HTML>";
			PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
			pw.println(req.getProtocol()+" "+arg0);
			pw.println("Content-Type: text/html");
			pw.println("Content-Length: "+err.length());
			pw.println("Connection: close");
			pw.println();
			pw.println(err);
			pw.flush();
			pw.close();
			commit=true;
			
		}
		else
			throw new IOException();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String arg0) throws IOException {
		System.out.println("[DEBUG] redirect to " + arg0 + " requested");
		System.out.println("[DEBUG] stack trace: ");
		Exception e = new Exception();
		StackTraceElement[] frames = e.getStackTrace();
		for (int i = 0; i < frames.length; i++) {
			System.out.print("[DEBUG]   ");
			System.out.println(frames[i].toString());
		}
		redirect=true;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
	 */
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(new Date(arg1));
		setHeader(arg0,date);
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
	 */
	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		if(headers.containsKey(arg0))
		{
			String d = (String)headers.get(arg0);
			SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			String date = df.format(new Date(arg1));
			headers.put(arg0,d+"; "+date);
		}
		else
		{
			setDateHeader(arg0,arg1);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
	 */
	public void setHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub
		headers.put(arg0.trim(), arg1.trim());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub
		if(headers.containsKey(arg0))
		{
			String x = (String)headers.get(arg0);
			x = x+"; "+arg1;
			setHeader(arg0,x);
		}
		else
		{
			setHeader(arg0,arg1);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
	 */
	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub
		headers.put(arg0,arg1+"");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
	 */
	public void addIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
		if(headers.containsKey(arg0))
		{
			String x = (String)headers.get(arg0);
			x = x+"; "+arg1+"";
			setHeader(arg0,x);
		}
		else
		{
			setHeader(arg0,arg1+"");
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
	 */
	public void setStatus(int arg0) {
		// TODO Auto-generated method stub
		status = arg0;
		set_status = true;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 */
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return "ISO-8859-1";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		if(headers.containsKey("Content-Type"))
			return (String) headers.get("Content-Type");
		else
			return "text/html";
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getWriter()
	 */
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(mywriter,true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub
		encoding=arg0;
		System.setProperty("file.encoding",arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub
		headers.put("Content-Length", arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub
		headers.put("Content-Type", arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub
		buf_size=arg0;
		set_buff=true;

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getBufferSize()
	 */
	public int getBufferSize() {
		// TODO Auto-generated method stub
		if(set_buff)
			return buf_size;
		else
			return mywriter.getBuffer().length();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 */
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
		if(!commit){
		
		PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
		int length = mywriter.getBuffer().length();
		if(redirect)
		{
			pw.println(req.getProtocol()+" 302 Redirect");
			pw.println("Content-Length: "+"<HTML><BODY>redirecting....</BODY></HTML>".length());
			pw.println("Content-Type: text/html");
			pw.println("Connection: close");
			pw.println();
			pw.println("<HTML><BODY>redirecting....</BODY></HTML>");
			pw.flush();
			redirect=false;
			commit=true;
			return;
		}
		
		
		if(req.getMethod().equals("HEAD"))
		{
				
			if(!set_status)
				pw.println(req.getProtocol()+" 200 OK");
			else
				pw.println(req.getProtocol()+" "+status);
			for(String s:headers.keySet())
			{
				if(!s.contains("Status")){
					pw.println(s+": "+headers.get(s));
				}
				
			}
			if(!headers.containsKey("Content-Type"))
				pw.println("Content-Type: "+"text/html");
			if(!headers.containsKey("Content-Length"))
				pw.println("Content-Length: "+length);
			pw.println("Connection: close");
			pw.println();
			pw.flush();
			commit=true;
			return;
		}
		if(!set_status)
			pw.println(req.getProtocol()+" 200 OK");
		else
			pw.println(req.getProtocol()+" "+status);
		for(String s:headers.keySet())
		{
			if(!s.contains("Status")){
				pw.println(s+": "+headers.get(s));
			}
			
		}
		if(!headers.containsKey("Content-Type"))
			pw.println("Content-Type: "+"text/html");
		if(!headers.containsKey("Content-Length"))
			pw.println("Content-Length: "+length);
		pw.println("Connection: close");
		pw.println();
		if(set_buff)
			pw.println(mywriter.toString().substring(0,buf_size));
		else
			pw.println(mywriter.toString());
		pw.flush();
		commit=true;
		//pw.close();
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	public void resetBuffer() {
		// TODO Auto-generated method stub
		mywriter = new StringWriter();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return commit;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
		if(!commit)
		{
			headers=new HashMap<String,Object>();
			mywriter = new StringWriter();
			redirect=false;
			set_status=false;
			encoding="ISO-8559-1";
			contentType="text/html";
		}
		else
			throw new IllegalStateException();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub
		return;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
	public HashMap<String,Object> headers;
	private String contentType=null;
	private Locale loc;
	private boolean commit;
	private Socket client;
	private StringWriter mywriter;
	private SimpleRequest req;
	private boolean redirect;
	private int status;
	private boolean set_status;
	private boolean set_buff;
	private String encoding;
	private int buf_size;
	public void setRequest(SimpleRequest req)
	{
		this.req=req;
	}
	
}
