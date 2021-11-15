package rpc.chat.server;

import rpc.chat.interfaces.IClient;

public class LocalClient implements IClient {
	String name;

	public LocalClient(String name) {
		this.name = name;
	}
	
	@Override
	public void empfangen(String msg) {
		System.out.println(name + " hört : " + msg);

	}

	@Override
	public String gibName() {
		return name;
	}

}
