package me.titan.easyftp;

import me.titan.easyftp.ai.EasyFTPAI;
import me.titan.easyftp.ai.MainMenu;
import me.titan.easyftp.cacheManager.CacheManager;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;

public class EasyFTPMain {

	private EasyFTPConnector ftpConnector;
	private ServerManager serverManager;

	private CacheManager cacheManager;
	public JFrame ai;
	public MainMenu mainMenu;

	public static File runningDir;
	//private static EasyFTPMain instance;

	public static void main(String[] args) {
//		JFrame jf  = new JFrame("gg");
//		JFilePicker filePicker =  new JFilePicker("Choose server directory ","search");
//		filePicker.setMode(JFilePicker.MODE_OPEN);
//
//		jf.setSize(500,500);
//		jf.setLocationRelativeTo(null);
//		jf.add(filePicker);
//		jf.setVisible(true);
		try {
			String path = new File(EasyFTPMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
			runningDir =new File(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println(runningDir);
		new EasyFTPMain();


	}

	public static File getRunningDir() {
		return runningDir;
	}

	public EasyFTPMain(){
		cacheManager = new CacheManager();


		ai = EasyFTPAI.getJFrame(this);
		ai.setVisible(true);

	}

	public void setServerManager(ServerManager serverManager) {
		this.serverManager = serverManager;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public ServerManager getServerManager() {
		return serverManager;
	}

	public void setFtpConnector(EasyFTPConnector ftpConnector) {
		this.ftpConnector = ftpConnector;
	}

	public EasyFTPConnector getFtpConnector() {
		return ftpConnector;
	}
}
