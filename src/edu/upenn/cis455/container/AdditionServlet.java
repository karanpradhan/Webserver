package edu.upenn.cis455.container;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
public class AdditionServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	//	response.sendRedirect("www.google.com");
	//	response.setStatus(234);
		long v=1393720359000L;
		response.setDateHeader("Date",v );
		PrintWriter out = response.getWriter();
		String i1 = request.getParameter("num1");
		String i2 = request.getParameter("num2");
//		String[] karan = request.getParameterValues("karan");
		
		out.println("<HTML><HEAD></HEAD><BODY>The name is "+(Integer.parseInt(i1)+Integer.parseInt(i2)));
/*		for(int i=0;i<karan.length;i++)
		{
			out.println("<P>"+karan[i]+"</P>");
		}*/
		out.println(" </BODY></HTML>");
		
	}
	
}
