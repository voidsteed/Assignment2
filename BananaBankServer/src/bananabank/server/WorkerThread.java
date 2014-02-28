package bananabank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WorkerThread extends Thread {

	Socket clientSocket;
	BananaBank bank;
	ServerSocket serverSocket;

	public WorkerThread(Socket cs,BananaBank bank, ServerSocket ss) {
		this.clientSocket = cs;
		this.bank = bank;
		this.serverSocket = ss;
	}

	@Override
	public void run() {
		BufferedReader r;
		PrintStream ps = null;
		try {
			r = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				//Stop accepting connection when it received 'SHUTDOWN' string
				if(line.equals("SHUTDOWN")){
					//close the server socket
					serverSocket.close();
					/*for (Thread t :threads){
					 * 		if (t!=Thread.getcurrentThread()){
					 * 				t.join();}}
					 * save to file
					 * compute total write it to client*/
					return;
				}
				else{
					ps = new PrintStream(clientSocket.getOutputStream());
					
					StringTokenizer st = new StringTokenizer(line);
					int balance = Integer.parseInt(st.nextToken());
					int srcAccountNumber = Integer.parseInt(st.nextToken());
					int dstAAccountNumber = Integer.parseInt(st.nextToken());
					
					Account source = bank.getAccount(srcAccountNumber);
					Account destination = bank.getAccount(dstAAccountNumber);
					
					if(source == null){
						ps.println("Invalid Source Account Number!");
					}
					else if(destination == null){
						ps.println("Invalid Destination Account!");
					}
					else{
						if(srcAccountNumber < dstAAccountNumber){
							synchronized(source){
								synchronized(destination){
									source.transferTo(balance, destination);
								}
							}
						}
						else{
							synchronized(destination){
								synchronized(source){
									source.transferTo(balance, destination);
								}
							}
						}
						ps.println("SourceAccount: " + srcAccountNumber + " to " + " Destination: "+ dstAAccountNumber+ " Balance is " + balance );
					}
					
				}
				
			}
			
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error occured in WorkerThread");
		}

	}

}
