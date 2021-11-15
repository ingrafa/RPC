package rpc.chat.server;
import java.util.ArrayList;
import java.util.List;

import rpc.chat.interfaces.IClient;
import rpc.chat.interfaces.IServer;

public class Server implements IServer {
	
	List<IClient> clients = new ArrayList<>();

	@Override
	public void broadcast(String msg, IClient client) {
		
		for (IClient iClient : clients) {
			// Über Leiche gehen und die dann "beerdigen"
			try {
				iClient.empfangen(client.gibName() + " schreibt: " + msg);
			} catch (Exception e) {
				this.abmelden(iClient);
			}
		}
	}

	@Override
	public void anmelden(IClient client) {
		clients.add(client);

	}

	@Override
	public void abmelden(IClient client) {
		clients.remove(client);

	}

}
