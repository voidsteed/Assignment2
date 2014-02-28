package bananabank.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test {
	public static void main(String[] args) throws UnknownHostException, IOException{
		Socket s = new Socket("localhost", 4444);
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out.writeBytes("10 33333 22222 \n");
		String received = r.readLine();
		System.out.println(received + "\n");
		r.close();
		s.close();

		Socket s2 = new Socket("localhost", 4444);
		DataOutputStream out2 = new DataOutputStream(s2.getOutputStream());
		BufferedReader r2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
		out2.writeBytes("SHUTDOWN\n");
		String received2 = r2.readLine();
		System.out.println(received2 + "\n");
		r2.close();
		s2.close();

	}
}
