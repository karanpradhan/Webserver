package edu.upenn.cis455.webserver;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
/*
 * Class for blocking queue
 */

class BlockingQueue
{
	Queue<Socket> queue; /* The data structure for representing queue */
	int size; /* The data structure for representing queue size */
	boolean using;
	/*
	 * Contructor 
	 * @param size Size of the queue.
	 */
	BlockingQueue(int size){
		this.size=size;
		using=false;
		queue = new LinkedList();
	}
	/*
	 * Function  for inserting a socket in the queue. Synchronized on the queue resource
	 * @param Socket Socket to be enqueued.
	 */
	synchronized void insert(Socket o) 
	{
			while(queue.size() == size){
				
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			queue.add(o);
			//using=false;
			notifyAll();	
			//System.out.println("Queue status:"+print());
			
		
	}
	/*
	 * Function for dequeuing sockets
	 * @return Socket Returns socket enqueued in the queue.
	 * @exception InterruptedException thrown on interrupt for the operation of shutdown command.
	 */
			
	synchronized Socket remove() throws InterruptedException
	{
		
			while(queue.isEmpty()){
				
				
						wait();
					
			}
			//using=false;
			notifyAll();	
			//System.out.println("Queue status:"+print());
			//System.out.println("Queue size is "+queue.size());
			return (Socket) queue.remove();
		
	}
	
	boolean isEmpty()
	{
		synchronized(queue){


			return queue.isEmpty();
		}
	}
	boolean isFull()
	{
		synchronized(queue){
			return queue.size() == size;
		}
	}
	public static void main(String arg[])
	{
		BlockingQueue q = new BlockingQueue(10);
		
		Work[] w = new Work[5];
		Work2[] w2 = new Work2[2];
		for(int i=0;i<5;i++)
			w[i] = new Work(q);
		for(int i=0;i<2;i++)
			w2[i] = new Work2(q);
		System.out.println(q.queue.toArray());
		
	}

}
class Work implements Runnable
{
	Thread t;
	BlockingQueue q;
	Work(BlockingQueue q)
	{	
		Thread t = new Thread();
		this.q = q;
		t.start();
	}
	public void run()
	{
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		q.insert(new Socket());
		System.out.println(q.queue.toArray());
	}
}
class Work2 implements Runnable
{
	Thread t;
	BlockingQueue q;
	Work2(BlockingQueue q)
	{	
		Thread t = new Thread();
		this.q = q;
		t.start();
	}
	public void run()
	{
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			q.remove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(q.queue.toArray());
	}
}