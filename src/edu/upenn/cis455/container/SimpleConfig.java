package edu.upenn.cis455.container;
import javax.servlet.*;
import java.util.*;

/**
 * @author Nick Taylor
 */
public class SimpleConfig implements ServletConfig {
	private String name;
	private SimpleContext context;
	private HashMap<String,String> initParams;
	
	public SimpleConfig(String name, SimpleContext context) {
		this.name = name;
		this.context = context;
		initParams = new HashMap<String,String>();
	}

	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public ServletContext getServletContext() {
		return context;
	}
	
	public String getServletName() {
		return name;
	}

	public void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}
