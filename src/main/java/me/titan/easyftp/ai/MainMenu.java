package me.titan.easyftp.ai;

import de.leonhard.storage.Json;
import me.titan.easyftp.AppLogger;
import me.titan.easyftp.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainMenu extends JFrame implements AppLogger<Color> {
	private JPanel mainPanel;
	private JButton syncBtn;
	private JButton restartButton;
	private JButton ServerBtn;
	private JButton FTPConBtn;
	private JLabel logText;

	EasyFTPMain main;

	public MainMenu(String title, EasyFTPMain main) {
		super(title);
		this.main = main;

		setContentPane(mainPanel);
		this.pack();
		setLocationRelativeTo(null);

		initListeners();

	}

	@Override
	public void logError(String error) {
		log(error, Color.RED);
	}

	@Override
	public void logInfo(String info) {
		log(info, Color.YELLOW);
	}

	@Override
	public void logRaw(String msg) {
		log(msg, Color.BLACK);
	}

	@Override
	public void logSuccess(String msg) {
		log(msg, Color.GREEN);
	}

	@Override
	public void log(String txt, Color clr) {
		logText.setText(txt);
		logText.setForeground(clr);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		if (b) {
			logRaw("");
		}
	}

	private void initListeners() {
		FTPConBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				main.ai.setVisible(true);
			}
		});
		ServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				main.ai.setVisible(true);
			}
		});
		syncBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (main.getServerManager() == null) {
					log("Error: You must specify your server directory! no server directory specified ", Color.RED);
					return;
				}

				log("Checking the sync of all files, this might take some time.", Color.BLACK);

				Map<File, Integer> map = main.getServerManager().checkSync(MainMenu.this);

				String servpath = main.getServerManager().serverDirectory.getPath();

				try {
					main.getFtpConnector().ftpClient.makeDirectory("test");
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
				Json json = main.getCacheManager().getJson();

				String p = Constants.SERVERS_PATH.str();
				for (Map.Entry<File, Integer> en : map.entrySet()) {


					String path = "test" + en.getKey().getPath().replace(servpath, "");

					path = path.replace("\\", "/");
					System.out.println(path);

					if (en.getValue() == 1 || en.getValue() == 2) {
						if (!main.getFtpConnector().upload(path, en.getKey())) {
							logError("Unable to delete file " + path + ", check console!");
							return;
						}
						if (en.getValue() == 2) {
							json.set(main.getServerManager().storagePath + "." + en.getKey().getPath().replace(".", ","), en.getKey().length());
						}

					} else if (en.getValue() == 3) {
						try {
							main.getFtpConnector().ftpClient.deleteFile(path);
						} catch (IOException ioException) {
							ioException.printStackTrace();
							logError("Unable to delete file " + en.getKey() + ", check console!");
							return;
						}
						json.remove(en.getKey().getPath().replace(".", ","));
					}

				}
				logSuccess("Synced all files successfully!");
			}
		});
	}

}
