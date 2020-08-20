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

public class EasyFTPAI extends JFrame {


	private JPanel mainFrame;
	private JLabel PortLabel;
	private JTextField portTextField;
	private JTextField UserTextField;
	private JTextField PassTextField;
	private JTextField HostTextField;
	private JButton connectButton;
	private JLabel status;
	private JTextArea serverLoc;
	private JButton chooseBtn;
	private JButton mainMenuButton;
	private JTextField ftpDirField;
	private JButton ftpDirChoose;


	EasyFTPMain main;

	public EasyFTPAI(String title, EasyFTPMain main) {
		super(title);

		this.main = main;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainFrame);
		this.pack();

		setLocationRelativeTo(null);


		initCache();
		initListeners();

	}

	public void setStatus(String str, Color clr) {
		status.setText(str);
		status.setForeground(clr);
	}


	private void initCache() {
		Json json = main.getCacheManager().getJson();

		if (json.contains(Constants.SERVER_DIR_CACHE_PATH.getValue())) {
			serverLoc.setText(json.getString(Constants.SERVER_DIR_CACHE_PATH.str()));
		}
		String path = Constants.FTP_CONNECTION.str();
		if (json.contains(path)) {
			HostTextField.setText(json.getString(path + ".host"));
			portTextField.setText(json.getInt(path + ".port") + "");

			UserTextField.setText(json.getString(path + ".user"));

			PassTextField.setText(json.getString(path + ".pass"));

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
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
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

				setStatus("Status: Connecting...", Color.YELLOW);
				EasyFTPConnector ftpConnector = new EasyFTPConnector();
				int r = ftpConnector.connect(host, port, user, pass);
				if (r == 1) {
					setStatus("Status: Unable to connect! please check console.", Color.RED);

					return;
				} else if (r == 2) {
					setStatus("Status: Unable to login! please check console.", Color.RED);
					return;
				} else {

					setStatus("Status: Successful!", Color.GREEN);

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
					setStatus("Status: File does not exist!", Color.RED);
					return;
				}
				if (!f.isDirectory()) {
					setStatus("Status: File path must be a folder, it must be the folder where all your worlds and /plugins/ folder are!", Color.RED);
					return;
				}


				try {
					main.setServerManager(ServerManager.setInstance(f, main));
				} catch (Exception ex) {

					setStatus("Status: An exception occoured!", Color.RED);
					ex.printStackTrace();
					return;
				}
				main.getCacheManager().getJson().set(Constants.SERVER_DIR_CACHE_PATH.getValue(), f.getPath());

				System.out.println(main.getCacheManager().getJson().getFile());
				setStatus("Success", Color.GREEN);


			}
		});
	}

}
