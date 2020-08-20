package me.titan.easyftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EasyFTPConnector {

	public FTPClient ftpClient;

	public int connect(String host, int port, String user, String pass){

		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(host,port);
		} catch (IOException ioException) {
//			status.setForeground(Color.RED);
//

			ioException.printStackTrace();
			return 1;
		}
//		status.setForeground(Color.GREEN);
//
//		status.setText("Status: Connection has been made, logging in...");

		try {
			ftp.login(user,pass);
		} catch (IOException ioException) {
//			status.setForeground(Color.RED);
//			status.setText("Status: Unable to login! please check console.");

			ioException.printStackTrace();
			return 2;
		}
//		status.setForeground(Color.GREEN);
//
//		status.setText("Status: Successful!");
		ftp.enterLocalPassiveMode();
		this.ftpClient = ftp;



//		try {
//			ftp.storeFile("text2",new FileInputStream(new File("C:/Users/Lenovo/Desktop/lobbyBackup/whitelist.json")));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return 0;
	}
	public boolean upload(String path, File f){
		//ftpClient.mod
		if(this.ftpClient == null) return false;
		try {
			ftpClient.storeFile(path,new FileInputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
