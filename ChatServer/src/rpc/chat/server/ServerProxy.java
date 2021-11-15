package rpc.chat.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import rpc.chat.interfaces.IProxy;

public class ServerProxy implements Runnable, IProxy {
	BufferedReader in;
	PrintWriter out;

	Server server;

	boolean running = true;

	ServerSideClientProxy lc;

	public ServerProxy(BufferedReader in, PrintWriter out, Object server) {
		this.server = (Server) server;
		this.in = in;
		this.out = out;
	}

	private void broadcast() {
		try {
			
			out.println("Gib mir deine Nachricht!");
			out.flush();
			String msg = in.readLine();
			
			server.broadcast(msg, lc);
			out.println("200 - Success");
			
		} catch (Exception e) {
			
			out.println("500 - Internal Server Error");
			out.println(e.getMessage());
			
		}
		
		out.flush();
	}

	private void anmelden() throws IOException {
		out.println("Gib mir deine IP-Adresse!");
		out.flush();
		String IP = in.readLine();
		System.out.println("IP: " + IP);
		
		out.println("Gib mir deinen Port!");
		out.flush();
		int port = Integer.parseInt(in.readLine());
		System.out.println("Port: " + port);
		
		try {
			lc = new ServerSideClientProxy(IP, port);
			
			server.anmelden(lc);
			out.println("200 - Success");

		} catch (Exception e) {
			
			out.println("500 - Internal Server Error");
			out.println(e.getClass().getName());
			
		}
		
		out.flush();

	}

	private void abmelden() throws IOException {
		try {
			
			server.abmelden(lc);
			out.println("200 - Success");
			
		} catch (Exception e) {
			
			out.println("500 - Internal Server Error");
			out.println(e.getMessage());
			
		}
		
		out.flush();
		running = false;
		in.close();
		out.close();
	}

	public void run() {
		while (running) {
			try {
				out.println("Methoden: (1)broadcast (2)anmelden (3)abmelden");
				out.flush();

				String line = in.readLine();

				switch (line) {
				case "1":
					this.broadcast();
					break;

				case "2":
					this.anmelden();
					break;

				case "3":
					this.abmelden();
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
