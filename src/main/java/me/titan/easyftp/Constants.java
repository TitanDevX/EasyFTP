package me.titan.easyftp;

public enum  Constants {

	SERVER_DIR_CACHE_PATH("serverDir"),
	FTP_CONNECTION("ftpCon"),
	SERVERS_PATH("servers");
	final Object value;


	Constants(Object value) {
		this.value = value;
	}

	public <T> T getValue() {
		return (T) value;
	}
	public String str(){
		return value +"";
	}
}
