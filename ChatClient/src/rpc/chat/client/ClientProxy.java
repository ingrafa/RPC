package rpc.chat.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import rpc.chat.interfaces.IClient;
import rpc.chat.interfaces.IServer;

public class ClientProxy implements IServer {
	Socket socket;

	BufferedReader input;
	PrintWriter output;
	
	RPCRuntime rpc;

	public ClientProxy(String host, int port, RPCRuntime rpc) throws Exception {
		this.rpc = rpc;
		socket = new Socket(host, port);

		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());

		int size = Integer.parseInt(input.readLine());

		for (int i = 0; i < size; i++) {
			System.out.println(input.readLine());
		}

		output.println("ChatServer");
		output.flush();

		if (!input.readLine().equals("Methoden: (1)broadcast (2)anmelden (3)abmelden")) {
			throw new Exception("Falscher-Proxy-Alarm!");
		}
	}

	@Override
	public void broadcast(String msg, IClient client) {
		output.println("1");
		output.flush();

		try {

			input.readLine();

			output.println(msg);
			output.flush();

			evaluateErrorCode();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void anmelden(IClient client) {
		output.println("2");
		output.flush();

		try {
			input.readLine();
			output.println("172.30.30.119");
			output.flush();
			
			input.readLine();
			output.println(rpc.socket.getLocalPort());
			output.flush();

			evaluateErrorCode();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void abmelden(IClient client) {
		output.println("3");
		output.flush();

		try {
			
			evaluateErrorCode();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void evaluateErrorCode() throws IOException {
		switch (input.readLine().substring(0, 3)) {
		case "500":
			String line = input.readLine();
			System.out.println(input.readLine());
			throw new RuntimeException(line);

		case "200":
			input.readLine();
			return;

		default:
			System.out.println(input.readLine());
			throw new RuntimeException("Implementationsfehler");
		}
	}
}
