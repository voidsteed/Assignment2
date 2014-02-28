package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MTServer {
	
	private static final int PORT = 4444;

	public static void main(String[] args) throws IOException {
		
		BananaBank bank = new BananaBank("accounts.txt");
		ServerSocket ss = new ServerSocket(PORT);
		System.out.println("MAIN: ServerSocket created");
		ArrayList<WorkerThread> threads = new ArrayList<WorkerThread>();
		
		try{
			for(;;) {
				System.out.println("MAIN: Waiting for client connection on port " + PORT);				
				Socket cs = ss.accept();
				System.out.println("MAIN: Client connected");
				WorkerThread t = new WorkerThread(cs,bank);
				t.start();
				threads.add(t);
			}
		
		}catch(IOException e){
			//....
		}
		//stop all workers
		for (WorkerThread workerThread : threads){
			
			//threads.join();
		}
			
	}
}
