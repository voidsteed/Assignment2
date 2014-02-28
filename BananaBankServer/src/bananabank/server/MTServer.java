package bananabank.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class MTServer {
	
	private static final int PORT = 4444;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		BananaBank bank = new BananaBank("accounts.txt");
		ServerSocket ss = new ServerSocket(PORT);
		Socket cs = null;
		ArrayList<WorkerThread> threads = new ArrayList<WorkerThread>();
		
		System.out.println("MAIN: ServerSocket created");
		
		
		try{
			for(;;) {	
				cs = ss.accept();
				System.out.println("MAIN: Client connected");
				WorkerThread t = new WorkerThread(cs,bank,ss);
				t.start();
				threads.add(t);
			}
		
		}catch(IOException e){
			//....
			for (WorkerThread workerThread : threads){
				workerThread.join();
				//threads.join();
			}
			BufferedReader reader;
			//save the account info in file
			bank.getAllAccounts();
			bank.save("temp");
			
			//cal total
			String currentLine;
			int total = 0;
			reader = new BufferedReader(new FileReader("temp"));
			while((currentLine = reader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(currentLine);
				int account = Integer.parseInt(st.nextToken());
				int balance = Integer.parseInt(st.nextToken());
				total += balance;
			}
			System.out.println();
			reader.close();
			
			//send back the total to client
			
			PrintStream ps = new PrintStream(cs.getOutputStream());
			ps.println("printout the total after send back to client" + total);	
		}
		cs.close();
	}
}
