package me.titan.easyftp;

public interface AppLogger<COLOR> {

	 void logError(String error);

	 void logInfo(String info);

	 void logRaw(String msg);

	 void logSuccess(String msg);

	 void log(String str, COLOR color);

}
