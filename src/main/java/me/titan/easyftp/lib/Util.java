package me.titan.easyftp.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {

	public static List<File> listFilesForFolder(final File folder) {

		return listFilesForFolder(folder,null);
	}
		public static List<File> listFilesForFolder(final File folder, List<File> copy) {
		List<File> list =new ArrayList<File>(10000000);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				list.addAll(listFilesForFolder(fileEntry, list));
			} else {
				if(copy != null) copy.add(fileEntry);
				list.add(fileEntry);
			}

		}
			return list;
	}
}
