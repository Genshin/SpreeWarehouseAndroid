package org.genshin.warehouse.profiles;

public class Profile {
	public long id;
	public String server;
	public int port;
	public String name;
	public String apiKey;

	public void set(long id, String name, String server, String apiKey) {
		this.set(id, name, server, 80, apiKey);
	}

	public void set(long id, String name, String server, String port, String apiKey) {
		this.set(id, name, server, Integer.parseInt(port), apiKey);
	}

	public void set(long id, String name, String server, int port, String apiKey) {
		this.id = id;
		this.name = name;
		this.server = server;
		this.port = port;
		this.apiKey = apiKey;
	}
}
