package com.yukti.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;

public class SDCardManager {
	
	public static String sd_card_folder_path = Environment.getExternalStorageDirectory()+"/DriveHere/";
	
	public boolean createFile(String filePath) {
		
		File folder = new File(Environment.getExternalStorageDirectory() + filePath);
		boolean success = folder.mkdir();
		if (success) 
			return true;

		return false;
	}
	
	public boolean createFolder(String folderPath) {

		File folder = new File(Environment.getExternalStorageDirectory() + folderPath);
		boolean success = folder.mkdir();
		if (success) 
			return true;

		return false;
	}
	
	// its also works for nested folder
	 public List<File> getListFiles(File parentDir) {
		    ArrayList<File> inFiles = new ArrayList<File>();
		    File[] files = parentDir.listFiles();
		    for (File file : files) {
		        if (file.isDirectory()) {
		            inFiles.addAll(getListFiles(file));
		        } else {
		            if(file.getName().endsWith(".csv")){
		                inFiles.add(file);
		            }
		        }
		    }
		    return inFiles;
		}
	 
	/* "\Trails2Bike\trail_timestamp_folder\trail.json" */
	public boolean isFileExists(String filePath){
		
		File file = new File(Environment.getExternalStorageDirectory() + filePath);
		if (file.exists())
			return true;
		return false;
	}
	
	/* "\Trails2Bike" */
	public boolean isFolderExists(String folderPath){
		
		File folder = new File(Environment.getExternalStorageDirectory() + folderPath);
		if (folder.exists())
			return true;
		return false;
	}
	
	public boolean isSDCardExists(){
		
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){ 
			return true;
		}
		return false;
	}
	
	/**
	 * @return Number of bytes available on External storage
	 */
	public static long getAvailableSpaceInBytes() {
		
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}

	/**
	 * @return Number of kilo bytes available on External storage
	 */
	public long getAvailableSpaceInKB(){
		
	    final long SIZE_KB = 1024L;
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
	    return availableSpace/SIZE_KB;
	}
	
	/**
	 * @return Number of Mega bytes available on External storage
	 */
	public long getAvailableSpaceInMB(){
		
	    final long SIZE_KB = 1024L;
	    final long SIZE_MB = SIZE_KB * SIZE_KB;
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
	    return availableSpace/SIZE_MB;
	}

}
