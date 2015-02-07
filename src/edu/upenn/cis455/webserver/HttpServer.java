package edu.upenn.cis455.webserver;



import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
/*
 * This is the main class. It is responsible for a number of things from creating the server socket, accepting connections
 * and inserting connections in the blocking queue.
 * It has the main method which accepts two parameters for instantiating the server
 * 1.port number
 * 2.root address
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.upenn.cis455.container.*;


public class HttpServer {

	public static ServerSocket serverSocket;
	public static Boolean shutdown;
	public static String port;
	public static String localAddr;
	public static Set<String> servlet_set;
	public static HashMap<String,HttpServlet> servlets;
	public static HashMap<String,String> urlmapping;
	public static int timeout;
	
	private static Handler parseWebdotxml(String webdotxml) throws Exception {
		Handler h = new Handler();
		File file = new File(webdotxml);
		if (file.exists() == false) {
			System.err.println("error: cannot find " + file.getPath());
			System.exit(-1);
		}
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(file, h);
		
		return h;
	}
	
	private static SimpleContext createContext(Handler h) {
		SimpleContext fc = new SimpleContext();
		for (String param : h.m_contextParams.keySet()) {
			fc.setInitParam(param, h.m_contextParams.get(param));
		}
		return fc;
	}
	
	private static HashMap<String,HttpServlet> createServlets(Handler h, SimpleContext fc) throws Exception {
		HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
		for (String servletName : h.m_servlets.keySet()) {
			SimpleConfig config = new SimpleConfig(servletName, fc);
			String className = h.m_servlets.get(servletName);
			Class servletClass = Class.forName("edu.upenn.cis455.container."+className);
			HttpServlet servlet = (HttpServlet) servletClass.newInstance();
			HashMap<String,String> servletParams = h.m_servletParams.get(servletName);
			if (servletParams != null) {
				for (String param : servletParams.keySet()) {
					config.setInitParam(param, servletParams.get(param));
				}
			}
			servlet.init(config);
			servlets.put(servletName, servlet);
		}
		return servlets;
	}

	private static void usage() {
		System.err.println("usage: java TestHarness <path to web.xml> " 
				+ "[<GET|POST> <servlet?params> ...]");
	}
	
	
	
	
	
	
	
	public static void main(String args[]) throws Exception
	{
		PropertyConfigurator.configure("log/log4j.properties");
		if(args.length < 2)
		{
			System.out.println("Name:Karan Pradhan");
			System.out.println("SEAS login:karanpr");
		}
		else
		{
			serverSocket = new ServerSocket(Integer.parseInt(args[0]),2000);
			localAddr=serverSocket.getInetAddress().toString();
			//Boolean shutdown = new Boolean(false);
			shutdown=false;
			port=args[0];
			if(!args[1].equals("."))
				System.setProperty("user.dir", new File(args[1]).getCanonicalPath());
			//int i=1;
			
			if (args.length < 3 || args.length % 2 == 0) {
				usage();
				System.exit(-1);
			}
			
			Handler h = parseWebdotxml(args[2]);
			SimpleContext context = createContext(h);
			
			 servlets = createServlets(h, context);
			 urlmapping= new HashMap<String,String>();
			 urlmapping=h.m_servletUrls;
			 timeout = h.m_session_timeout*60;

			
			
			BlockingQueue bq = new BlockingQueue(600);
			System.out.println("Blocking Queue initialised");
			Object shutdown_lock = new Object();
			ThreadPool pool = new ThreadPool(60,bq,shutdown_lock);
			
			Controller con = new Controller(pool,shutdown_lock);
			System.out.println("Thread Pool initialised");
			while(!shutdown)
			{
				Socket client=null;
				try{
					
					
					client = serverSocket.accept();
					
					
				}
				catch(SocketException se)
				{
					if(shutdown)
						break;
				}
				catch(SocketTimeoutException ste)
				{
					continue;
				}
				
				/*
    	Socket client = serverSocket.accept();
    	InputStreamReader in = new InputStreamReader(client.getInputStream());
    	BufferedReader br = new BufferedReader(in);
    	PrintWriter out = new PrintWriter(client.getOutputStream(),true);
    	String request = br.readLine();
    	Parser p = new Parser(request,client);
    	p.parse();
    	System.out.println("Client request "+request);
    	Request req =  new Request(i,p.method,p.url,p.version,p.headers,p.optMessage,p.ext,p.client);
    	//out.println("HTTP/1.1 200 OK\n\n");
    	//String currentLine;
    	//File f=new File("/resources/index.html");s
    	//System.out.println(f.getAbsolutePath());
    	System.out.println("Reading "+p.url);

				 */
				//InputStreamReader in = new InputStreamReader(client.getInputStream());

				//BufferedReader br = new BufferedReader(in);
				//String request = br.readLine();
				//System.out.println("hello main read "+request);
				//client.close();
				pool.handle(client);
				//Response res = new Response(req);
				//System.out.print(out);
				//res.write(client);
				//System.out.print(client);
				//i++;
				if(shutdown){
					break;
				}
			}
			//serverSocket.close();
			System.out.println("Shutting down main");

		}
	}
}


class Handler extends DefaultHandler {
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.compareTo("servlet-name") == 0) {
			m_state = 1;
		} else if (qName.compareTo("servlet-class") == 0) {
			m_state = 2;
		} else if (qName.compareTo("context-param") == 0) {
			m_state = 3;
		} else if (qName.compareTo("init-param") == 0) {
			m_state = 4;
		} else if (qName.compareTo("param-name") == 0) {
			m_state = (m_state == 3) ? 10 : 20;
		} else if (qName.compareTo("param-value") == 0) {
			m_state = (m_state == 10) ? 11 : 21;
		}
		else if (qName.compareTo("url-pattern")==0){
			m_state =7;
		}
		else if(qName.compareTo("session-timeout") == 0){
			m_state = 8;
		}
	}
	public void characters(char[] ch, int start, int length) {
		String value = new String(ch, start, length);
		if (m_state == 1) {
			m_servletName = value;
			m_state = 0;
		} else if (m_state == 2) {
			m_servlets.put(m_servletName, value);
			m_state = 0;
		} else if (m_state == 10 || m_state == 20) {
			m_paramName = value;
		} else if (m_state == 11) {
			if (m_paramName == null) {
				System.err.println("Context parameter value '" + value + "' without name");
				System.exit(-1);
			}
			m_contextParams.put(m_paramName, value);
			m_paramName = null;
			m_state = 0;
		} else if (m_state == 21) {
			if (m_paramName == null) {
				System.err.println("Servlet parameter value '" + value + "' without name");
				System.exit(-1);
			}
			HashMap<String,String> p = m_servletParams.get(m_servletName);
			if (p == null) {
				p = new HashMap<String,String>();
				m_servletParams.put(m_servletName, p);
			}
			p.put(m_paramName, value);
			m_paramName = null;
			m_state = 0;
		}
		else if(m_state == 7){
			m_servletUrls.put(value,m_servletName);
			m_state = 0;
		}
		else if(m_state == 8){
			m_session_timeout = Integer.parseInt(value);
			m_state = 0;
		}
	}
	private int m_state = 0;
	private String m_servletName;
	private String m_paramName;
	HashMap<String,String> m_servlets = new HashMap<String,String>();
	HashMap<String,String> m_contextParams = new HashMap<String,String>();
	HashMap<String,HashMap<String,String>> m_servletParams = new HashMap<String,HashMap<String,String>>();
	HashMap<String,String> m_servletUrls = new HashMap<String,String>();
	int m_session_timeout;
}
