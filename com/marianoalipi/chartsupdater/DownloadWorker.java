package com.marianoalipi.chartsupdater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DownloadWorker extends SwingWorker<Integer, String> {

	
	protected Integer doInBackground() throws Exception {
		// Example URL: http://aeronav.faa.gov/d-tpp/1707/00610IL13L.PDF

		String[] airportURLs = new String[DataIO.airports.size()];
		for(byte i=0;i<DataIO.airports.size();i++) {
			airportURLs[i] = DataIO.ip + DataIO.airac + "/" + DataIO.airports.get(i).getUrlSegment();
		}

		// Download process.
		byte total = 0, notDownloaded = 0;
		URL url = null;
		//url = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/1908-kl-t-zamenhof.jpg/800px-1908-kl-t-zamenhof.jpg"); // L.L. Zamenhof

		InputStream in;
		
		// Download process for each airport.
		for(byte i=0;i<DataIO.airports.size();i++) {
			
			String aptCode = DataIO.airports.get(i).getCode();
			
			// Create a folder for current airport, if it doesn't exist yet.
			try {
				Files.createDirectory(Paths.get(ChartsUpdater.file.getAbsolutePath()+ "/" + aptCode));
				System.out.println("\nNo \"" + aptCode + "\" folder found. Creating one.");
			} catch (FileAlreadyExistsException e1) {
				System.out.println("\nFolder \"" + aptCode + "\" found.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// Download regular charts. Each iteration is an airport's file.
			for(byte j=0;j<DataIO.airports.get(i).getFiles().length;j++) {
				try {
					url = new URL(airportURLs[i] + DataIO.airports.get(i).getFiles()[j][0] + ".PDF");
					System.out.print("Downloading \"" + DataIO.airports.get(i).getFiles()[j][1].toString() + "\" from:\t");
					System.out.println(url.toString());
					in = url.openStream();
					Files.copy(in, Paths.get(DataIO.path + "/" + aptCode + "/" + aptCode + " " + DataIO.airports.get(i).getFiles()[j][1] + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
					in.close();
					total++;
				} catch (MalformedURLException e1) {
					System.out.println("MalformedURLException");
					JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "Malformed URL. Error in code?", "Malformed URL", JOptionPane.WARNING_MESSAGE);
					notDownloaded++;
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "File not found. Wrong AIRAC or file address?\n" + aptCode + ": " + DataIO.airports.get(i).getFiles()[j][0], "File not found", JOptionPane.WARNING_MESSAGE);
					System.out.println("File not found. Wrong AIRAC or file address?\n");
					notDownloaded++;
				} catch (IOException e1) {
					System.out.println("FileNotFoundException.\ni = " + i + "\nj = " + j + "\nFile: " + url.toString() + "\n");
				} catch (Exception ex) {
					System.out.println("Other exception:");
					ex.printStackTrace();
				}
			}
			
			
		}
		
		/* Old code to download APDs. Address changed to a regular one. Implemented in the "Download regular charts" for loop, and in the array for each airport. 
		
		// Download APDs.
		for(byte i=0;i<uniqueURLs.length;i++) {
			try {
				url = new URL(uniqueURLs[i]);
				System.out.println(airportNames[i]);
				InputStream in = url.openStream();
				Files.copy(in, Paths.get(ChartsUpdater.file.getAbsolutePath()+ "/" + airportNames[i] + "/" + airportNames[i] + " APD.pdf"), StandardCopyOption.REPLACE_EXISTING);
				in.close();
			} catch (MalformedURLException e1) {
				System.out.println("MalformedURLException");
				JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "Malformed URL. Wrong address in code?", "Malformed URL", JOptionPane.WARNING_MESSAGE);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "File not found. Wrong address in code?", "File not found", JOptionPane.WARNING_MESSAGE);						
			} catch (IOException e1) {
				System.out.println("FileNotFoundException.\ni = " + i + "\nFile: " + url.toString() + "\n");
			}
			total++;
		}
		
		*/
		
		if(notDownloaded > 0) {
			System.out.println(notDownloaded + "/" + total + " files couldn't be retrieved.");
			JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), notDownloaded + "/" + total + " files couldn't be retrieved.", "Some files couldn't be retrieved", JOptionPane.INFORMATION_MESSAGE);
		}
		System.out.println("Download completed.");
		JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "Download completed.", "Download completed", JOptionPane.INFORMATION_MESSAGE);
		
		ChartsUpdater.chartsUpdater.downloadB.setEnabled(true);
		ChartsUpdater.chartsUpdater.downloadingL.setText("");
		return 0;
	}
	
}
