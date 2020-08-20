package me.titan.easyftp.cacheManager;

import de.leonhard.storage.Json;
import me.titan.easyftp.EasyFTPMain;

import java.io.File;

public class CacheManager {

	private Json json;



	public Json getJson(){
		if(json == null) {
			json = new Json(new File(EasyFTPMain.getRunningDir().getPath() + "/EasyFTP/cache.json"));
		}
		return json;
	}

}
