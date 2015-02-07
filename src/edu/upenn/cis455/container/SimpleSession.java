package edu.upenn.cis455.container;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Todd J. Green
 */
public class SimpleSession implements HttpSession {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	private SimpleResponse r;
	
	
	
	public SimpleSession(SimpleResponse r)
	{
		this.r = r;
	//	setMaxInactiveInterval(-1);
	}
	
	SimpleResponse getResponse()
	{
		return r;
	}
	void setResponse(SimpleResponse r)
	{
		this.r =r;
	}
	
	
	public long getCreationTime() {
		// TODO Auto-generated method stub
		String s= (String) m_props.get("CreationTime");
		
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d=null;
		try {
			d = df.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.getTime();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		// TODO Auto-generated method stub
		return (String)m_props.get("Id");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		String s= (String) m_props.get("LastAccessedTime");
		
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d=null;
		try {
			d = df.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.getTime();
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return (ServletContext)m_props.get("ServletContext");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		if(arg0 < 0)
		{
			arg0 = Integer.MAX_VALUE;
		}
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
		long tot = a + arg0*1000 ;
		Date d = new Date(tot);
		m_props.put("MaxInactiveInterval", df.format(d));
		m_props.put("SetTime", arg0+"");
		Cookie c = new Cookie("JSESSIONID",this.getId());
		c.setMaxAge(arg0);
		this.getResponse().addCookie(c);
//		SessionPool.session_list.put(Integer.parseInt(this.getId()), this);
		

	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getInterval() {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		Date dnow=null;
		try {
			 dnow = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date d=null;
		try {
			d = df.parse((String)m_props.get("MaxInactiveInterval"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time_now=dnow.getTime();
		long time_session = d.getTime();
		long diff = time_session-time_now;
		if(time_now > time_session)
		{
			this.invalidate();
			return 0;
		}
		else
		{
			int i=(int)(time_session-time_now)*1000;
			return i;
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		for(Integer i: SessionPool.session_list.keySet())
		{
			if(this == SessionPool.session_list.get(i))
			{
				SessionPool.session_list.remove(i);
				Cookie c = new Cookie("JSESSIONID",this.getId());
				c.setMaxAge(0);
				this.getResponse().addCookie(c);
				
			}
		}
		
		m_valid = false;
		//Cookie c = new Cookie("JSESSIONID",this.getId());
		
	
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		// TODO Auto-generated method stub
		String b = (String) m_props.get("isNew");
		return Boolean.parseBoolean(b);
	}

	boolean isValid() {
		return m_valid;
	}
	
	private Properties m_props = new Properties();
	private boolean m_valid = true;



	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime());
		Date dnow=null;
		try {
			 dnow = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date d=null;
		try {
			d = df.parse((String)m_props.get("MaxInactiveInterval"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Integer.parseInt((String)m_props.get("SetTime"));
	}
}
