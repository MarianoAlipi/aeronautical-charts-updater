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
		
		String[] airportNames = {"KBOS", "KDFW", "KEWR", "KFLL", "KIAH", "KJFK", "KLAX", "KSEA"};

		String IP = "http://155.178.201.160/d-tpp/";
		// 0 KBOS, 1 KDFW, 2 KEWR, 3 KFLL, 4 KIAH, 5 KJFK, 6 KLAX, 7 KSEA
		String[] airportCodes = {"/00058", "/06039", "/00285", "/00744", "/05461", "/00610", "/00237", "/00582"};
		String[] airportURLs = new String[airportCodes.length];
		for(byte i=0;i<airportCodes.length;i++) {
			airportURLs[i] = IP + ChartsUpdater.airac + airportCodes[i];
		}
		
		/* Old code used to download APDs. Address changed.
		
		String[] uniqueURLs = new String[airportNames.length];
		for(byte i=0;i<airportNames.length;i++) {
			uniqueURLs[i] = "http://es.flightaware.com/resources/airport/" + airportNames[i].substring(1,4) + "/APD/AIRPORT+DIAGRAM/pdf";
		}
		
		*/
		
		// Format: airport[fileNumber[address][savedFileName]]
		String[][] kbos = { {"ILD15R", "ILS 15R"}, {"IL22L", "ILS 22L"}, {"IL27", "ILS 27"}, {"IL33L", "ILS 33L"}, {"IL4R", "ILS 4R"}, {"AD", "APD"} };
		String[][] kdfw = { {"IL31R", "ILS 31R"}, {"IL35C", "ILS 35C"}, {"IL35R", "ILS 35R"}, {"IL36L", "ILS 36L"}, {"AD", "APD"} };
		String[][] kewr = { {"IL11", "ILS 11"}, {"IL22L", "ILS 22L"}, {"IL22R", "ILS 22R"}, {"IL4L", "ILS 4L"}, {"IL4R", "ILS 4R"}, {"AD", "APD"} };
		String[][] kfll = { {"IL10L", "ILS 10L"}, {"IL10R", "ILS 10R"}, {"IL28L", "ILS 28L"}, {"IL28R", "ILS 28R"}, {"AD", "APD"} };
		String[][] kiah = { {"IL8L", "ILS 8L"}, {"IL8R", "ILS 8R"}, {"IL9", "ILS 9"}, {"IL15R", "ILS 15R"}, {"IL26L", "ILS 26L"}, {"IL26R", "ILS 26R"}, {"IL27", "ILS 27"}, {"IL33R", "ILS 33R"}, {"AD", "APD"} };
		String[][] kjfk = { {"IL13L", "ILS 13L"}, {"IL22L", "ILS 22L"}, {"IL31L", "ILS 31L"}, {"IL31R", "ILS 31R"}, {"IL4L", "ILS 4L"}, {"IL4R", "ILS 4R"}, {"AD", "APD"} };
		String[][] klax = { {"IL24L", "ILS 24L"}, {"IL24R", "ILS 24R"}, {"IL25L", "ILS 25L"}, {"IL25R", "ILS 25R"}, {"IL6L", "ILS 6L"}, {"IL7L", "ILS 7L"}, {"IL7R", "ILS 7R"}, {"AD", "APD"} };
		String[][] ksea = { {"IL16L", "ILS 16L"}, {"IL16R", "ILS 16R"}, {"IL34L", "ILS 34L"}, {"IL34R", "ILS 34R"}, {"AD", "APD"} };

		String[][][] airportFileNames = {kbos, kdfw, kewr, kfll, kiah, kjfk, klax, ksea};
		// Important arrays: airportNames, airportURLs, uniqueURLs, airportFileNames


		// Download process.
		byte total = 0, notDownloaded = 0;
		URL url = null;
		// url = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/1908-kl-t-zamenhof.jpg/800px-1908-kl-t-zamenhof.jpg"); // L.L. Zamenhof

		// Download process for each airport.
		for(byte i=0;i<airportFileNames.length;i++) {
			
			// Create a folder for current airport, if it doesn't exist yet.
			try {
				Files.createDirectory(Paths.get(ChartsUpdater.file.getAbsolutePath()+ "/" + airportNames[i]));
				System.out.println("\nNo \"" + airportNames[i] + "\" folder found. Creating one.");
			} catch (FileAlreadyExistsException e1) {
				System.out.println("\nFolder \"" + airportNames[i] + "\" found.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// Download regular charts.
			for(byte j=0;j<airportFileNames[i].length;j++) {
				try {
					url = new URL(airportURLs[i] + airportFileNames[i][j][0].toString() + ".PDF");
					System.out.println(airportFileNames[i][j][1].toString());
					InputStream in = url.openStream();
					Files.copy(in, Paths.get(ChartsUpdater.file.getAbsolutePath()+ "/" + airportNames[i] + "/" + airportNames[i] + " " + airportFileNames[i][j][1].toString() + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
					in.close();
				} catch (MalformedURLException e1) {
					System.out.println("MalformedURLException");
					JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "Malformed URL. Wrong address in code?", "Malformed URL", JOptionPane.WARNING_MESSAGE);
					notDownloaded++;
				} catch (FileNotFoundException e1) {
//					JOptionPane.showMessageDialog(ChartsUpdater.chartsUpdater.getParent(), "File not found. Wrong AIRAC?", "File not found", JOptionPane.WARNING_MESSAGE);
					System.out.println("File not found. Wrong AIRAC?\n");
					notDownloaded++;
				} catch (IOException e1) {
					System.out.println("FileNotFoundException.\ni = " + i + "\nj = " + j + "\nFile: " + url.toString() + "\n");
				}
				total++;
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
