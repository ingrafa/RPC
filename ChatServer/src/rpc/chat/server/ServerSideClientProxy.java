package rpc.chat.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import rpc.chat.interfaces.IClient;
import rpc.chat.interfaces.IServer;

public class ServerSideClientProxy implements IClient {
	Socket socket;

	BufferedReader input;
	PrintWriter output;

	public ServerSideClientProxy(String host, int port) throws Exception {
		socket = new Socket(host, port);

		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
		
		int size = Integer.parseInt(input.readLine());

		for (int i = 0; i < size; i++) {
			System.out.println(input.readLine());
		}

		output.println("ChatClient");
		output.flush();
		
		String msg = input.readLine();
		System.out.println("Msg: " + msg);
		if (!msg.equals("Methoden: (1)Empfangen (2)GibName")) {
			throw new Exception("Falscher-Proxy-Alarm!");
		}
	}

	private Object evaluateErrorCode() throws IOException {
		switch (input.readLine().substring(0, 3)) {
		case "500":
			String line = input.readLine();
			System.out.println(input.readLine());
			throw new RuntimeException(line);

		case "200":
			System.out.println(input.readLine());
			return null;
			
		case "400":
			String s = input.readLine();
			System.out.println(input.readLine());
			return s;

		default:
			System.out.println(input.readLine());
			throw new RuntimeException("Implementationsfehler");
		}
	}

	@Override
	public void empfangen(String msg) {
		output.println("1");
		output.flush();

		try {

			System.out.println(input.readLine());

			output.println(msg);
			output.flush();

			evaluateErrorCode();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public String gibName() {
		output.println("2");
		output.flush();

		try {
			
			return (String) evaluateErrorCode();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
