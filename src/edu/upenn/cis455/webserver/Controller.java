package edu.upenn.cis455.webserver;
/*
 * Class which has a thread object waiting for the shutdown signal
 * Has a reference to the thread pool so that it can interrupt the working threads.
 */
class Controller implements Runnable{
	ThreadPool t_pool;
	//static Boolean shutdown;
	Thread t;
	Object shutdown_lock;
	/*
	 * Contructor for the class which starts the threads
	 * @param ThreadPool The thread pool reference
	 * 
	 * @param Object The shutdown lock for synchronization
	 */
	Controller(ThreadPool t_pool,Object shutdown_lock)
	{
		this.t_pool=t_pool;
		//shutdown=false;
		Thread t = new Thread(this,"Controller");
		this.shutdown_lock=shutdown_lock;
		t.start();
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Run method which takes care of shutting down all the threads.
	 */
	public void run()
	{
		
		System.out.println("Controller is waiting");
		
		synchronized(shutdown_lock)
		{
		{
		while(!HttpServer.shutdown){
			try {
				shutdown_lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		}
		}
		System.out.println("Controller is up");
		for( Worker w : t_pool.getWorkers())
		{
			w.t.interrupt();
		}
		Thread.currentThread().interrupt();
		System.out.println("Shutting down the controller");
	//	notifyAll();
		
	}
	
	static void setShutdown()
	{
		HttpServer.shutdown=true;
	}

}
