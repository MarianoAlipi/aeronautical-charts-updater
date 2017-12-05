package com.marianoalipi.chartsupdater;

import java.util.ArrayList;

public class Airport {

	// Apparently, pairedFilesAL is not needed at all. Don't remove just yet.
	
	private String code, urlSegment;
	private String[][] files;
	private String[] pairedFiles;
	
	public ArrayList<String[]> filesAL;
	public ArrayList<String>pairedFilesAL;

	public Airport(String code, String urlSegment, String[][] files) {
		this.code = code;
		this.urlSegment = urlSegment;
		this.files = files;
		this.pairedFiles = generatePairedFiles();
		
		saveFilesToAL();
		savePairedFilesToAL();
	}
	
	public Airport(String code, String urlSegment) {
		this.code = code;
		this.urlSegment = urlSegment;
		this.pairedFiles = generatePairedFiles();
		
		saveFilesToAL();
		savePairedFilesToAL();
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrlSegment() {
		return urlSegment;
	}

	public void setUrlSegment(String urlSegment) {
		this.urlSegment = urlSegment;
	}
	
	public String[][] getFiles() {
		return files;
	}
	
	public void setFiles(String[][] files) {
		this.files = files;
	}
	
	public ArrayList<String[]> getFilesAL() {
		return filesAL;
	}
	
	public void setFilesAL(ArrayList<String[]> filesAL) {
		this.filesAL = filesAL;
	}
	
	public ArrayList<String> getPairedFilesAL() {
		return pairedFilesAL;
	}
	
	public void setPairedFilesAL(ArrayList<String> pairedFilesAL) {
		this.pairedFilesAL = pairedFilesAL;
	}
	
	public String[] generatePairedFiles() {
		String[] pairs;
		if(files != null) {
			pairs = new String[files.length + 1];
			for(byte i=0;i<files.length;i++) {
				pairs[i + 1] = String.format("%-13s%-1s", files[i][0] + ":", files[i][1]);
			}
		} else {
			pairs = new String[1];
		}
		pairs[0] = "Airport URL segment:        " + urlSegment;
		return pairs;
	}
	
	public String[] getPairedFiles() {
		return pairedFiles;
	}
	
	public void setPairedFiles(String[] pairedFiles) {
		this.pairedFiles = pairedFiles;
	}
	
	public void saveFilesToAL() {
		filesAL = new ArrayList<String[]>();
		if(files != null) {
			for(byte i=0;i<files.length;i++) {
				filesAL.add(files[i]);
			}
		}
	}
	
	public void saveFilesToArray() {
		files = new String[filesAL.size()][2];
		for (byte i=0;i<filesAL.size();i++) {
			files[i] = filesAL.get(i);
		}
	}
	
	public void savePairedFilesToAL() {
		pairedFilesAL = new ArrayList<String>();
		if(pairedFiles != null) {
			for(byte i=0;i<pairedFiles.length;i++) {
				pairedFilesAL.add(pairedFiles[i]);
			}
		}
	}
	
	public void savePairedFilesToArray() {
		pairedFiles = new String[pairedFilesAL.size()];
		for (byte i=0;i<pairedFilesAL.size();i++) {
			pairedFiles[i] = pairedFilesAL.get(i);
		}
	}

}
