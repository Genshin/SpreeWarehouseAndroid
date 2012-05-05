package org.genshin.spree.profiles;

public class Profile {
	public long id;
	public String server;
	public int port;
	public String profileName;
	public String apiKey;

	public void set(long id, String server, String user, String apiKey) {
		this.set(id, server, 80, user, apiKey);
	}

	public void set(long id, String server, String port, String user, String apiKey) {
		this.set(id, server, Integer.parseInt(port), user, apiKey);
	}

	public void set(long id, String server, int port, String user, String apiKey) {
		this.id = id;
		this.server = server;
		this.port = port;
		this.profileName = user;
		this.apiKey = apiKey;
	}
}
