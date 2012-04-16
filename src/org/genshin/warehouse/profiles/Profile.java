package org.genshin.warehouse.profiles;

public class Profile {
	public long id;
	public String server;
	public String user;
	public String password;
	public String key;

	public void set(long id, String server, String user, String password) {
		this.id = id;
		this.server = server;
		this.user = user;
		this.password = password;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
