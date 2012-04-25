package org.genshin.warehouse.profiles;

public class Profile {
	public long id;
	public String server;
	public long port;
	public String user;
	public String password;
	public String key;

	public void set(long id, String server, String user, String password) {
		this.set(id, server, 80, user, password);
	}

	public void set(long id, String server, String port, String user, String password) {
		this.set(id, server, Long.parseLong(port), user, password);
	}

	public void set(long id, String server, long port, String user, String password) {
		this.id = id;
		this.server = server;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
