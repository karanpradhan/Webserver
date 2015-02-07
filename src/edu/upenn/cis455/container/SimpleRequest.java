package edu.upenn.cis455.container;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import edu.upenn.cis455.webserver.HttpServer;
import edu.upenn.cis455.webserver.Request;
/**
 * @author Todd J. Green
 */
public class SimpleRequest implements HttpServletRequest {

	SimpleRequest() {
	}
	
	public SimpleRequest(SimpleSession session,Request r,String m_servletName,SimpleResponse res) {
		m_session = session;
		m_method = r.getMethod();
		this.m_servletName=m_servletName;
		m_props.putAll(r.getRequestedHeaders());
		char_encoding="";
		this.r=r;
		this.res=res;
		//this.m_url=url;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		// TODO Auto-generated method stub
		return "BASIC";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		if(r.getRequestedHeaders().containsKey("Cookie")){
		String cookie_list = r.getRequestedHeaders().get("Cookie");
		String cookies[] = cookie_list.split("; ");
		Cookie[] c= new Cookie[cookies.length];
		for(int i=0;i<cookies.length;i++)
		{
			String[] temp = cookies[i].split("=");
			c[i]=new Cookie(temp[0],temp[1]);
		}
		return c;
		}
		else
		{
			return new Cookie[0];
		}
	
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(r.getRequestedHeaders().containsKey(arg0))
		{
			SimpleDateFormat df = new SimpleDateFormat();
			String date = r.getRequestedHeaders().get(arg0);
			
				try {
					return df.parse(date).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					throw new IllegalArgumentException();
				}
			
		}
		else 
			return -1;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		if(r.getRequestedHeaders().containsKey(arg0))
			return r.getRequestedHeaders().get(arg0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String arg0) {
		// TODO Auto-generated method stub
		if(r.getRequestedHeaders().containsKey(arg0)){
			
		Set<String> s =  new HashSet<String>();
		if(r.getRequestedHeaders().get(arg0).contains("; "))
		{
			String[] x=r.getRequestedHeaders().get(arg0).split("; ");
			for(int i=0;i<x.length;i++)
			{
				s.add(x[i]);
			}
			
		}
		else
			s.add(r.getRequestedHeaders().get(arg0));
		Enumeration<String> e = Collections.enumeration(s);
		return e;}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() {
		// TODO Auto-generated method stub
		Enumeration<String> e = Collections.enumeration((r.getRequestedHeaders().keySet()));
		return e;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		if(r.getRequestedHeaders().containsKey(arg0))
			return Integer.parseInt(r.getRequestedHeaders().get(arg0));
		else
			return -1;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		return m_method;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		// TODO Auto-generated method stub
		String s = r.getUrl();
		int i = s.indexOf(m_servletName);
		i=i+m_servletName.length();
		return s.substring(i);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		// TODO Auto-generated method stub
		String url=r.getUrl();
		if(getMethod().equals("GET") && url.contains("?"))
		{
			int idx = url.indexOf("?");
			return url.substring(idx+1);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		Cookie[] c = getCookies();
		for(int i=0;i<c.length;i++)
		{
			if(c[i].getName().equals("JSESSIONID"))
				return c[i].getValue();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		// TODO Auto-generated method stub
		String url = r.getUrl();
		if(url.contains("?"))
		{
			int idx = url.indexOf("?");
			return "/"+url.substring(0,idx);
		}
		else
		{
			return "/"+url;
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		String path="";
		String port=getLocalPort()+"";
		String name="";
		String method="";
		String req=getRequestURI();
		String ret = "http://"+getLocalAddr()+":"+port+req;
		StringBuffer b = new StringBuffer(ret);
		// TODO Auto-generated method stub
		return b;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		// TODO Auto-generated method stub
		return m_servletName;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		if(arg0)
		{
			return getSession();
		}
		else
		{
			Cookie[] c = this.getCookies();
			if(c!=null){
			for(int i=0;i<c.length ;i++){
				
				if(c[i].getName().equals("JSESSIONID") && SessionPool.session_list.containsKey(Integer.parseInt(c[i].getValue())) )
				{
					if((SessionPool.session_list.get(Integer.parseInt(c[i].getValue())).getInterval() > 0)){
					Calendar now = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
					df.setTimeZone(TimeZone.getTimeZone("GMT"));
					String date = df.format(now.getTime());
					m_session = SessionPool.session_list.get(Integer.parseInt(c[i].getValue()));
					m_session.setAttribute("LastAccessedTime", date);
					m_session.setAttribute("isNew", false);
					m_session.setResponse(res);
//					Cookie session_cookie = new Cookie("JSESSIONID",m_session.getId());
//					res.addCookie(session_cookie);
					return m_session;
					}
				}
			}
			return null;
		}
		return null;
	}
	}
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		Cookie[] c = this.getCookies();
		if(c!=null){
		for(int i=0;i<c.length ;i++){
			
			if(c[i].getName().equals("JSESSIONID") && SessionPool.session_list.containsKey(Integer.parseInt(c[i].getValue())) )
			{
				if((SessionPool.session_list.get(Integer.parseInt(c[i].getValue())).getInterval() > 0)){
				Calendar now = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = df.format(now.getTime());
				m_session = SessionPool.session_list.get(Integer.parseInt(c[i].getValue()));
				m_session.setAttribute("LastAccessedTime", date);
				m_session.setAttribute("isNew", false);
				m_session.setResponse(res);
//				Cookie session_cookie = new Cookie("JSESSIONID",m_session.getId());
//				res.addCookie(session_cookie);
				return m_session;
				}
				else
				{
					SimpleSession s = new SimpleSession(res);
					synchronized(SessionPool.lock){
					SessionPool.pool_count = SessionPool.pool_count + 1 ;
					}
					Calendar now = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
					df.setTimeZone(TimeZone.getTimeZone("GMT"));
					String date = df.format(now.getTime());
					s.setAttribute("isNew", "true");
					s.setAttribute("CreationTime",date);
					s.setAttribute("LastAccessedTime", date);
					s.setAttribute("Id", SessionPool.pool_count+"");
					
					SessionPool.session_list.put(SessionPool.pool_count,s);
					m_session = s;
					m_session.setMaxInactiveInterval(HttpServer.timeout);
					
					return m_session;
				}
			}
			else if(c[i].getName().equals("JSESSIONID") && !SessionPool.session_list.containsKey(Integer.parseInt(c[i].getValue())))
			{
				SimpleSession s = new SimpleSession(res);
				synchronized(SessionPool.lock){
				SessionPool.pool_count = SessionPool.pool_count + 1 ;
				}
				Calendar now = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				String date = df.format(now.getTime());
				s.setAttribute("isNew", "true");
				s.setAttribute("CreationTime",date);
				s.setAttribute("LastAccessedTime", date);
				s.setAttribute("Id", SessionPool.pool_count+"");
				
				SessionPool.session_list.put(SessionPool.pool_count,s);
				m_session = s;
				m_session.setMaxInactiveInterval(HttpServer.timeout);
				
				return m_session;
			}
		}
		}
		SimpleSession s = new SimpleSession(res);
		synchronized(SessionPool.lock){
		SessionPool.pool_count = SessionPool.pool_count + 1 ;
		}
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		s.setAttribute("isNew", "true");
		s.setAttribute("CreationTime",date);
		s.setAttribute("LastAccessedTime", date);
		s.setAttribute("Id", SessionPool.pool_count+"");
		
		SessionPool.session_list.put(SessionPool.pool_count,s);
		m_session = s;
		m_session.setMaxInactiveInterval(HttpServer.timeout);
		
		
		
		return m_session;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		Cookie[] c = this.getCookies();
		if(c!=null){
		for(int i=0;i<c.length ;i++){
			
			if(c[i].getName().equals("JSESSIONID") && SessionPool.session_list.containsKey(Integer.parseInt(c[i].getValue())) )
			{
				if((SessionPool.session_list.get(Integer.parseInt(c[i].getValue())).getInterval() > 0)){
					return true;
				}
			}
			
		}
		}
		return false;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		Cookie[] c = this.getCookies();
		if(c!=null){
		for(int i=0;i<c.length ;i++){
			
			if(c[i].getName().equals("JSESSIONID") && SessionPool.session_list.containsKey(Integer.parseInt(c[i].getValue())) )
			{
				return true;
			}
		}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub

		if(char_encoding.equals(""))
			return "ISO-8859-1";
		else
			return char_encoding;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		char_encoding=arg0;
		try{
			System.setProperty("file.encoding",arg0);
		}
		catch(Exception e)
		{
			throw new UnsupportedEncodingException();
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		// TODO Auto-generated method stub
		if(r.getRequest() != null)
			return Integer.parseInt(m_props.getProperty("Content-Length"));
		else
			return -1;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		return "text/html";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
		if(m_params.containsKey(arg0))
			return m_params.get(arg0).get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		Set<String> s =m_params.keySet();
		if(s==null){
			Set<String> k = new HashSet<String>();
			return Collections.enumeration(k);
		}
		Enumeration<String> e = Collections.enumeration(s);
		return e;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		if(m_params.containsKey(arg0))
		{
		ArrayList<String> p = m_params.get(arg0);
		String[] br = new String[p.size()];
		int iv=0;
		for(String i : p){
			br[iv]=i;
			iv++;
		}
		return br;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return m_params;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		// TODO Auto-generated method stub
		return r.getVersion();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		// TODO Auto-generated method stub
		return "http";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		// TODO Auto-generated method stub
		return "HTTP Server";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		// TODO Auto-generated method stub
		return Integer.parseInt(HttpServer.port);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		InputStream i = new ByteArrayInputStream(r.getRequest().getBytes());
		BufferedReader br =  new BufferedReader(new InputStreamReader(i,getCharacterEncoding()));
		return br;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return r.getClient().getInetAddress().toString();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return r.getClient().getInetAddress().toString();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
		r.getRequestedHeaders().put(arg0, arg1.toString());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		m_props.remove(arg0);
		r.getRequestedHeaders().remove(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemotePort()
	 */
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return r.getClient().getLocalPort();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {
		// TODO Auto-generated method stub
		return "HTTP Server";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return HttpServer.localAddr;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return getServerPort();
	}

	public void setMethod(String method) {
		m_method = method;
	}
	
    public void setParameter(String key, String value) {
    	
    	if(m_params.containsKey(key))
    	{
    		ArrayList<String> x = m_params.get(key);
    		x.add(value);
    		m_params.put(key, x);
    	}
    	else
    	{	
    	ArrayList<String> x = new ArrayList<String>();
    	x.add(value);
		m_params.put(key, x);
    	}
	}
	
	void clearParameters() {
		m_params.clear();
	}
	
	boolean hasSession() {
		return ((m_session != null) && m_session.isValid());
	}
		
	String helper(){
		return r.request;
	}
	private HashMap<String,ArrayList<String>> m_params = new HashMap<String,ArrayList<String>>();
	private Properties m_props = new Properties();
	private SimpleSession m_session = null;
	private int m_session_id = -1;
	private String m_servletName;
	private String m_method;
	private Request r;
	private String char_encoding;
	private SimpleResponse res;
	private String m_url;
}
