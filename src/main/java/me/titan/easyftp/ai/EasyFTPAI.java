package me.titan.easyftp.ai;

import de.leonhard.storage.Json;
import me.titan.easyftp.Constants;
import me.titan.easyftp.EasyFTPConnector;
import me.titan.easyftp.EasyFTPMain;
import me.titan.easyftp.ServerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EasyFTPAI extends JPanel {


	private JLabel jcomp1;
	private JLabel jcomp2;
	private JTextField HostTextField;
	private JLabel jcomp4;
	private JTextField portTextField;
	private JLabel jcomp6;
	private JTextField UserTextField;
	private JLabel jcomp8;
	private JTextField PassTextField;
	private JButton connectButton;
	private JLabel jcomp11;
	private JTextField serverLoc;
	private JLabel jcomp13;
	private JTextField ftpDirField;
	private JButton chooseBtn;
	private JButton ftpDirChoose;
	private JButton mainMenuButton;
	private JLabel status;


	EasyFTPMain main;

	public EasyFTPAI( EasyFTPMain main) {

		setupAI();
		this.main = main;



		initCache();
		initListeners();

	}
	public static JFrame getJFrame(EasyFTPMain main){
		if(main.ai != null){

			return main.ai;
		}

		JFrame j = new JFrame("Easy FTP");
		j.setContentPane(new EasyFTPAI(main));
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		j.pack();
		j.setLocationRelativeTo(null);

		return j;
	}

	public void setStatus(String str, Color clr) {
		status.setText(str);
		status.setForeground(clr);
	}


	private void initCache() {
		Json json = main.getCacheManager().getJson();

		if (json.contains(Constants.SERVER_DIR_CACHE_PATH.getValue())) {
			serverLoc.setText(json.getString(Constants.SERVER_DIR_CACHE_PATH.str()));
			
			File f = new File(serverLoc.getText());
			if (!f.exists()) {
				setStatus("File does not exist!", Color.RED);
				return;
			}
			if (!f.isDirectory()) {
				setStatus("File path must be a folder, it must be the folder where all your worlds and /plugins/ folder are!", Color.RED);
				return;
			}


			try {
				main.setServerManager(ServerManager.setInstance(f, main));
			} catch (Exception ex) {

				setStatus( "An exception occoured while setting the server dir!", Color.RED);
				ex.printStackTrace();
				return;
			}
			
		}
		String path = Constants.FTP_DIR_CACHE_PATH.str();
		if(json.contains(path)){
			String dir = json.getString(path);
			if(main.getServerManager()== null){

				return;
			}
			ftpDirField.setText(dir);
			main.getServerManager().targetDir = dir;
		}
		 path = Constants.FTP_CONNECTION.str();
		if (json.contains(path)) {
			String host = json.getString(path + ".host");
			int port = json.getInt(path + ".port");
			String user = json.getString(path + ".user");
			String pass =json.getString(path + ".pass");
			
			
			HostTextField.setText(host);
			portTextField.setText(port + "");
			UserTextField.setText(user);
			PassTextField.setText(pass);

			

			setStatus("Connecting...", Color.YELLOW);
			EasyFTPConnector ftpConnector = new EasyFTPConnector();
			int r = ftpConnector.connect(host, port, user, pass);
			if (r == 1) {
				setStatus("Unable to connect! please check console.", Color.RED);

				return;
			} else if (r == 2) {
				setStatus("Unable to login! please check console.", Color.RED);
				return;
			} else {

				setStatus(" Successful!", Color.GREEN);

			}

			main.setFtpConnector(ftpConnector);
			

		}

	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			setStatus("", Color.BLACK);
		}
	}

	private void initListeners() {
		ftpDirChoose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(main.getServerManager()== null){
					setStatus("You must specify the server directory first!", Color.RED);
				
					return;
				}
				main.getServerManager().targetDir = ftpDirField.getText();
				
			}
		});
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				main.ai.setVisible(false);
				if (main.mainMenu == null) {
					main.mainMenu = new MainMenu("Easy FTP", main);
				}
				main.mainMenu.setVisible(true);
			}
		});
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {


				//add(new JFilePicker("idk","idk"));
				String host = HostTextField.getText();
				int port = Integer.parseInt(portTextField.getText());
				String user = UserTextField.getText();
				String pass = PassTextField.getText();

				setStatus("Connecting...", Color.YELLOW);
				EasyFTPConnector ftpConnector = new EasyFTPConnector();
				int r = ftpConnector.connect(host, port, user, pass);
				if (r == 1) {
					setStatus("Unable to connect! please check console.", Color.RED);

					return;
				} else if (r == 2) {
					setStatus("Unable to login! please check console.", Color.RED);
					return;
				} else {

					setStatus("Successful!", Color.GREEN);

				}

				main.setFtpConnector(ftpConnector);
				Json json = main.getCacheManager().getJson();

				String path = Constants.FTP_CONNECTION.str();
				json.set(path + ".host", host);
				json.set(path + ".port", port);
				json.set(path + ".user", user);
				json.set(path + ".pass", pass);

			}
		});
		chooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				File f = new File(serverLoc.getText());
				System.out.println(serverLoc.getText());
				if (!f.exists()) {
					setStatus("File does not exist!", Color.RED);
					return;
				}
				if (!f.isDirectory()) {
					setStatus("File path must be a folder, it must be the folder where all your worlds and /plugins/ folder are!", Color.RED);
					return;
				}


				try {
					main.setServerManager(ServerManager.setInstance(f, main));
				} catch (Exception ex) {

					setStatus("An exception occoured!", Color.RED);
					ex.printStackTrace();
					return;
				}
				main.getCacheManager().getJson().set(Constants.SERVER_DIR_CACHE_PATH.getValue(), f.getPath());

				System.out.println(main.getCacheManager().getJson().getFile());
				setStatus("Success", Color.GREEN);


			}
		});
	}

	private  void setupAI(){
		jcomp1 = new JLabel ("FTP Connection");
		jcomp2 = new JLabel ("Host");
		HostTextField = new JTextField ("", 5);
		jcomp4 = new JLabel ("Port");
		portTextField = new JTextField (5);
		jcomp6 = new JLabel ("Username");
		UserTextField = new JTextField ("", 5);
		jcomp8 = new JLabel ("Password");
		PassTextField = new JTextField ("", 5);
		connectButton = new JButton ("Connect");
		jcomp11 = new JLabel ("Specify Server Location");
		serverLoc = new JTextField ("", 5);
		jcomp13 = new JLabel ("Server Directory In WebServer");
		ftpDirField = new JTextField ("", 5);
		chooseBtn = new JButton ("Choose Directory");
		ftpDirChoose = new JButton ("Choose Directory");
		mainMenuButton = new JButton ("Main Menu");
		status = new JLabel ("");

		//adjust size and set layout
		setPreferredSize (new Dimension (683, 514));
		setLayout (null);

		//add components
		add (jcomp1);
		add (jcomp2);
		add (HostTextField);
		add (jcomp4);
		add (portTextField);
		add (jcomp6);
		add (UserTextField);
		add (jcomp8);
		add (PassTextField);
		add (connectButton);
		add (jcomp11);
		add (serverLoc);
		add (jcomp13);
		add (ftpDirField);
		add (chooseBtn);
		add (ftpDirChoose);
		add (mainMenuButton);
		add (status);

		//set component bounds (only needed by Absolute Positioning)
		jcomp1.setBounds (15, 10, 100, 20);
		jcomp2.setBounds (15, 40, 100, 25);
		HostTextField.setBounds (15, 65, 180, 25);
		jcomp4.setBounds (15, 110, 100, 25);
		portTextField.setBounds (15, 135, 180, 25);
		jcomp6.setBounds (15, 180, 100, 25);
		UserTextField.setBounds (15, 205, 180, 25);
		jcomp8.setBounds (15, 250, 100, 25);
		PassTextField.setBounds (15, 275, 180, 25);
		connectButton.setBounds (15, 315, 180, 55);
		jcomp11.setBounds (300, 10, 150, 25);
		serverLoc.setBounds (300, 35, 315, 25);
		jcomp13.setBounds (300, 180, 320, 40);
		ftpDirField.setBounds (300, 225, 315, 30);
		chooseBtn.setBounds (300, 110, 140, 30);
		ftpDirChoose.setBounds (300, 300, 135, 40);
		mainMenuButton.setBounds (30, 455, 615, 45);
		status.setBounds (190, 405, 685, 25);
	}
}
