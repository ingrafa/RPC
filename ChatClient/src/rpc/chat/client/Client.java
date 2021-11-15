package rpc.chat.client;

import rpc.chat.interfaces.IClient;

public class Client implements IClient {
	
	String name;
	
	public Client(String name) {
		this.name = name;
	}

	@Override
	public void empfangen(String msg) {
		System.out.println(msg);
	}

	@Override
	public String gibName() {
		return name;
	}

}
