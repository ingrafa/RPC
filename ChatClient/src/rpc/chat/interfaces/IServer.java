package rpc.chat.interfaces;

public interface IServer {
	void broadcast(String msg, IClient client);
	void anmelden(IClient client);
	void abmelden(IClient client);
}
