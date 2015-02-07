package edu.upenn.cis455.webserver;
/*
 * This class is merely used for transfer of data from the parser to the response object.
 * 
 */

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
public class Request {
	
	//private String request;
	private String method;
	private String url;
	private String version;
	private LinkedHashMap<String,String> headers;
	private String optMessage;
	private String ext;
	private Socket client;
	private int req_number;
	private String host;
	private HashMap<String,String> requestedHeaders;
	public String request;
	private String port;
	
	public Request(int req_number,String method,String url,String version,LinkedHashMap<String,String> headers,String optMessage,String ext,Socket client,String host,HashMap<String,String> hdrs,String request){
		this.setMethod(method);
		this.setUrl(url);
		this.setVersion(version);
		this.setHeaders(headers);
		this.optMessage=optMessage;
		this.ext=ext;
		this.setClient(client);
		this.req_number=req_number;
		this.host=host;
		this.setRequestedHeaders(hdrs);
		this.request=request;
		
		//this.request = request;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public LinkedHashMap<String,String> getHeaders() {
		return headers;
	}

	public void setHeaders(LinkedHashMap<String,String> headers) {
		this.headers = headers;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	

	public int getReq_number() {
		return req_number;
	}

	public void setReq_number(int req_number) {
		this.req_number = req_number;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public HashMap<String,String> getRequestedHeaders() {
		return requestedHeaders;
	}

	public void setRequestedHeaders(HashMap<String,String> requestedHeaders) {
		this.requestedHeaders = requestedHeaders;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

}
