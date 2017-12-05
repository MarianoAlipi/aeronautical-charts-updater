package com.marianoalipi.chartsupdater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DataIO {


	private static File file = new File("savedSettings.txt");

	public static int airac;
	public static String path, ip;
	public static ArrayList<Airport> airports = new ArrayList<Airport>();
	
	/** Read and load saved data from a file. */
	public static void read() {
		try {
			FileReader fileReader = new FileReader(file);
			JSONObject savedSettings = new JSONObject(new JSONTokener(fileReader)); // savedSettings = .txt saved file
			JSONArray airportsJSON, airportFiles;
			JSONObject currentAirport;
			
			airports = new ArrayList<Airport>();
			
			path = savedSettings.getString("path");
			airac = savedSettings.getInt("airac");
			ip = savedSettings.getString("ip");
			
			// aptFiles[fileByIndex][URL segment or savename]
			String[][] aptFiles;
			
			airportsJSON = savedSettings.getJSONArray("airports");
			
			String code, urlSegment;
			
			for(byte i=0;i<airportsJSON.length();i++) {

				currentAirport = airportsJSON.getJSONObject(i);
				
				code = currentAirport.getString("code");
				urlSegment = currentAirport.getString("urlSegment");
				
				aptFiles = new String[20][2];
				airportFiles = currentAirport.getJSONArray("files");
				
				for(byte j=0;j<airportFiles.length();j++) {
					aptFiles[j] = airportFiles.getJSONArray(j).toList().toArray(new String[0]);
				}
				
				airports.add(new Airport(code, urlSegment, Arrays.copyOfRange(aptFiles,0,airportFiles.length())));

				// Debugging:
//				Airport apt = airports.get(i);
//				System.out.println(apt.getCode() + "\n" + apt.getUrlSegment());
//				for (byte k=0;k<apt.getFiles().length;k++) {
//					System.out.println(apt.getFiles()[k][0] + ": " + apt.getFiles()[k][1]);
//				}
//				System.out.println("----------");
			}
			fileReader.close();
		} catch (FileNotFoundException e) { JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater, "No saved settings found.\nDefault settings will be loaded.", "Information", JOptionPane.INFORMATION_MESSAGE);
			restoreDefaultSettings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Save data to a file. */
	public static void save() {
		PrintWriter pw;		
		try {
			pw = new PrintWriter(file);
			
			JSONObject settingsToSave = new JSONObject(),
					   currentAirport;
			JSONArray airportsJSON = new JSONArray();
			Airport apt;
			
			settingsToSave.put("ip", ip)
						  .put("path", path)
						  .put("airac", airac);
			
			for (byte i=0;i<airports.size();i++) {				
				apt = airports.get(i);
				currentAirport = new JSONObject();
				currentAirport.put("code", apt.getCode())
							  .put("urlSegment", apt.getUrlSegment());

				if (apt.getFiles() != null) {
					currentAirport.put("files", apt.getFiles());
				} else {
					currentAirport.put("files", new String[0][0]);
				}
				
				airportsJSON.put(currentAirport);
			}
			
			settingsToSave.put("airports", airportsJSON);
			
			pw.print(settingsToSave);
			pw.close();
		} catch(FileNotFoundException e) { /* Do nothing */ }	
	}
	
	public static void restoreDefaultSettings() {
		try {
			InputStream input = DataIO.class.getResourceAsStream("/com/marianoalipi/chartsupdater/defaultSettings.txt");
			Files.deleteIfExists(new File("savedSettings.txt").toPath());
//			Files.copy(new File("defaultSettings.txt").toPath(), new File("savedSettings.txt").toPath());
			Files.copy(input, new File("savedSettings.txt").toPath());
			JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater, "The program will close.\nPlease reopen it to see the changes.", "Information", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater, "No default settings found.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static File getFile() {
		return file;
	}
	
	public static void setFile(String fileName) {
		DataIO.file = new File(fileName);
	}
}
