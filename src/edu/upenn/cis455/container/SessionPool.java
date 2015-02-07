package edu.upenn.cis455.container;

import java.util.HashMap;

import javax.servlet.http.Cookie;

public class SessionPool {
	static HashMap<Integer,SimpleSession> session_list = new HashMap<Integer,SimpleSession>();
	static int pool_count =0 ;
	static Object lock= new Object();
	SimpleResponse res;
	
	
	

}
