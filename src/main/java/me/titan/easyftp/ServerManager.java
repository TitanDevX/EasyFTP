package me.titan.easyftp;

import de.leonhard.storage.Json;
import me.titan.easyftp.lib.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {

	private static ServerManager defaultServerManager;

	 public File serverDirectory;

	 public String storagePath;

	 public String targetDir;
	 EasyFTPMain main;
	public Map<String, Long> files = new HashMap<>(10000000);
	public ServerManager(File dir,EasyFTPMain main ) {
		this.serverDirectory = dir;

		this.main = main;

		if(isInitializedBefore(dir)) return;


		Json json = main.getCacheManager().getJson();

		String path = Constants.SERVERS_PATH.str() + "." + dir.getPath().replace(".", ",");
		this.storagePath = path;
		if(json.contains(path)) {

			for(String fpath : json.singleLayerKeySet(path)){
				String fpathA = fpath.replace(",",".");

				files.put(fpathA,json.getLong(path + "." + fpath ));
				System.out.println("gg");
			}
		}else {
			for (File f : Util.listFilesForFolder(dir)) {

				json.getFileData().insert(path + "." + f.getPath().replace(".", ","), f.length());
				files.put(f.getPath(), f.length());
			}
		}
		json.write();
	}

	/**
	 * @return a map of unsync files, the key of this map can be number from 1-3. 1 = size doesn't match. 2 = new file. 3 = deleted file.
	 */
	public Map<File, Integer> checkSync(AppLogger logger){
		Map<File, Integer> unsyncedFiles = new HashMap<>(10000000);
		Map<String, Long> nFiles = new HashMap<>(10000000);



		File dir = new File(serverDirectory.getPath());
		for(File f : Util.listFilesForFolder(dir)){
			if(logger != null){
				logger.logInfo("Processing " + f.getPath());
			}
			if(files.containsKey(f.getPath())){
				long oldSize = files.get(f.getPath());
				if(f.length() != oldSize){
					unsyncedFiles.put(f,1);
					if(logger != null){
						logger.logInfo("File " + f.getPath() + " size has been changed!");
					}
				}
				files.remove(f.getPath());
			}else{
				unsyncedFiles.put(f,2);
				if(logger != null){
					logger.logInfo("File " + f.getPath() + " is new!");
				}
			}
			if(logger != null){
				logger.logInfo("File " + f.getPath() + " is already synced!");
			}

			nFiles.put(f.getPath(),f.length());

//			try {
//				TimeUnit.SECONDS.sleep(2);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}
		if(!files.isEmpty()){
			for(String path : files.keySet()) {
				unsyncedFiles.put(new File(path),3);
				if(logger != null){
					logger.logInfo("File " + path + " has been deleted!");
				}
			}
		}
		this.files= nFiles;

		System.out.println("Synced Files amount: " + files.size() + ", unsynced files amount: " + unsyncedFiles.size());
		if(logger != null)
		  logger.logInfo("Synced Files amount: " + files.size() + ", unsynced files amount: " + unsyncedFiles.size());
		return unsyncedFiles;
	}
	public boolean isInitializedBefore(File f){
		return false;
	}

	public static ServerManager setInstance(File dir, EasyFTPMain main) {

			defaultServerManager = new ServerManager(dir,main);

		return defaultServerManager;
	}
	public static ServerManager getInstance() {
		return defaultServerManager;
	}
}
