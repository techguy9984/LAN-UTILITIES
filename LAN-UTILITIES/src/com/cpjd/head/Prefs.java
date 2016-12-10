package com.cpjd.head;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Saves the program's preferences.
 * @author Will Davies
 *
 */
public class Prefs {

private static File dir;
	
	/**
	 * Should be called whenever the application is started.
	 * This will locate the save dirs / create them in accordance with the os
	 */
	public static void initDirs() {
		String osName = System.getProperty("os.name").toLowerCase();
		
		if(osName.contains("win")) {
			dir = new File((System.getenv("APPDATA") + File.separator + "LAN-UTILS" + File.separator));
		} else if(osName.contains("mac")) {
			dir = new File(System.getProperty("user.home") + "/Library/Application Support/LAN-UTILS"+File.separator);
		} else if(osName.contains("nux")) {
			dir = new File(System.getProperty("user.home") + "/LAN-UTILS");
		}
		
		if(!dir.exists()) dir.mkdir();
	}
	
	public static boolean serializeSave(Save save) {
		return serializeObject(save, "lan-save.ser");
	}
	
	public static Save deserializeSave() {
		Save save = (Save) deserializeObject("lan-save.ser");
		if(save == null) {
			save = new Save();
			save.setDisplay(-1);
			save.setTolerance(75);
			serializeSave(save);
		}
		return save;
	}
	public static void deleteSave() {
		deleteFile("lan-save.ser");
	}
	
	private static boolean serializeObject(Object object, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(dir + File.separator + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(object);
			out.close();
			fos.close();
			return true;
		} catch(Exception e) {
			System.out.println("Could not find the droids you're looking for.");
			e.printStackTrace();
			return false;
		}
	}
	
	private static Object deserializeObject(String location) {
		try {
			FileInputStream fis = new FileInputStream(dir + File.separator + location);
			ObjectInputStream in = new ObjectInputStream(fis);
			Object o = in.readObject();
			in.close();
			fis.close();
			return o;
		} catch(Exception e) {
			System.out.println("Could not find the droids you're looking for.");
			return null;
		}
	}
	
	private static void deleteFile(String location) {
		File file = new File(dir + File.separator + location);
		file.delete();
	}
	
	public static class Save implements Serializable {
		
		private static final long serialVersionUID = 5042436675881979222L;
		
		public ArrayList<String> database;
		public String message;
		public int tolerance;
		public int display;
		
		public int getDisplay() {
			return display;
		}
		
		public void setDisplay(int display) {
			this.display = display;
		}
		
		public void setDatabase(ArrayList<String> database) {
			this.database = database;
		}
		
		public ArrayList<String> getDatabase() {
			return database;
		}
		
		public void addDatabaseEntry(String entry) {
			if(database == null) database = new ArrayList<String>();
			database.add(entry);
		}
		
		public void setTolerance(int tolerance) {
			this.tolerance = tolerance;
		}
		
		public int getTolerance() {
			return tolerance;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
	}
}

