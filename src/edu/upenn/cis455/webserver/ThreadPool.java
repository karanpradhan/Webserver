package edu.upenn.cis455.webserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/*
 * This class is the ThreadPool class which instantiates a list of workers for handling the
 * client request. It has a reference to the blocking queue as well. Other member include the shutdown lock which
 * is used to synchronize the shutdown signal.
 */


class ThreadPool
{
	//volatile Boolean shutdown;
	List<Worker> workers;
	BlockingQueue bq;
	Object shutdown_lock;
	/*
	 * Constructor for the thread pool class.
	 * @param int Size of the thread pool
	 * @param BlockingQueue Reference to the blocking queue object
	 * @param Object The shutdown lock
	 */
	ThreadPool(int size,BlockingQueue bq,Object shutdown_lock)
	{
		//bq= new BlockingQueue();
		this.bq=bq;
		workers=new ArrayList<Worker>();
		this.shutdown_lock=shutdown_lock;
		for(int i=0;i<size;i++)
		{
			Worker w=new Worker(i,bq,this.shutdown_lock);
			workers.add(w);
		}

	}
	/*
	 * This method inserts the socket in the blocking queue.
	 * @exception InterruptedException when the interrupting a thread on shutdown
	 */
	void handle(Socket r) throws InterruptedException
	{
		bq.insert(r);
	}
	List<Worker> getWorkers()
	{
		return workers;
	}
	
	/*
	static Request control(Socket s)
	{
		File control_html = new File(System.getProperty("user.dir") + "/resources/Files/control.html");
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(control_html));
			out.write("<HTML>");
			out.write("<H1>Karan Pradhan : karanpr </H1>");
			out.write("<UL>");
			for(Worker i : workers)
			{
				String x = "Worker "+i.id + " " + i.state;
				out.write("<LI>"+x+"</LI");
			}
			out.write("</UL>");
			out.write("<A href=\"shutdown\">Shutdown</A>");
			out.write("</HTML>");
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
			out.close();
		}
		*/
		
		
	
}