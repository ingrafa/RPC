package rpc.chat.client;
import java.io.BufferedReader;
import java.io.PrintWriter;

import rpc.chat.interfaces.IProxy;

public class ClientSideServerProxy implements Runnable, IProxy {
	BufferedReader in;
	PrintWriter out;

	Client client;

	boolean running = true;

	public ClientSideServerProxy(BufferedReader in, PrintWriter out, Object client) {
		this.client = (Client) client;
		this.in = in;
		this.out = out;
	}

	private void gibName() {

		try {

			out.println("400 - Success");
			out.println(client.gibName());

		} catch (Exception e) {

			out.println("500 - Internal Server Error");
			out.println(e.getMessage());

		}
		out.flush();

	}

	private void empfangen() {
		try {
			out.println("Gib mir die Message");
			out.flush();
			client.empfangen(in.readLine());
			out.println("200 - Success");

		} catch (Exception e) {

			out.println("500 - Internal Server Error");
			out.println(e.getMessage());

		}
		out.flush();

	}

	public void run() {
		while (running) {
			try {
				out.println("Methoden: (1)Empfangen (2)GibName");
				out.flush();

				String line = in.readLine();

				switch (line) {
				case "1":
					this.empfangen();
					break;

				case "2":
					this.gibName();
					break;

				default:
					out.println("Protokollfehler!");
					out.flush();
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				running = false;
			}
		}
	}
}
