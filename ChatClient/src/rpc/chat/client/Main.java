package rpc.chat.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

import rpc.chat.interfaces.IProxy;
import rpc.chat.interfaces.IProxyFactory;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("-- CLIENT --");
		Client client = new Client("TestName");
		
		RPCRuntime rpc = new RPCRuntime(new ServerSocket(7777));
		rpc.register("ChatClient", new IProxyFactory() {
			@Override
			public IProxy createProxy(BufferedReader inputStream, PrintWriter outputStream) {
				return new ClientSideServerProxy(inputStream, outputStream, client);
			}
		});
		(new Thread(rpc)).start();

		ClientProxy clientP = new ClientProxy("localhost", 6666, rpc);
		clientP.anmelden(client);
		clientP.broadcast("testMsg", client);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;
		
		while (!((msg = br.readLine()).equals("quit"))) {
			clientP.broadcast(msg, client);
		}
		
		clientP.abmelden(client);
		
	}

}
