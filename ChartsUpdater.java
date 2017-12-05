package com.marianoalipi.chartsupdater;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChartsUpdater extends JFrame {

	protected static final String version = "1.1"; 
	
	static ChartsUpdater chartsUpdater;
	DownloadWorker downloadWorker = new DownloadWorker();
	private static final long serialVersionUID = 1L;
	final Dimension SIZE = new Dimension(510, 100);

	static File file;
	static int airac;
	JTextField airacTF;
	JButton downloadB;
	JLabel downloadingL;

	public ChartsUpdater() {

		setSize(SIZE);
		setTitle("Charts Updater" + " " + version);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(SIZE));
		setMaximumSize(new Dimension(SIZE));
		setPreferredSize(new Dimension(SIZE));
		setResizable(false);
		setVisible(true);

		ButtonListener buttonListener = new ButtonListener();
		
		Container pane = this.getContentPane();
		pane.setLayout(new FlowLayout());

		JPanel pathPane = new JPanel(), downloadPane = new JPanel();
		pathPane.setLayout(new BorderLayout());
		downloadPane.setLayout(new BorderLayout());

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Set the destination folder");
		chooser.setAcceptAllFileFilterUsed(false);

		JTextField pathTF = new JTextField();
		pathTF.setText("");
		pathTF.setEditable(false);
		pathTF.setColumns(33);

		JButton select = new JButton("Set path...");
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(ChartsUpdater.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					pathTF.setText(file.getAbsolutePath());
					//This is where the application should open the file.
				} else {
					// Cancelled by user.
				}
			}
		});
		
		JLabel airacL = new JLabel("AIRAC:");
		
		airacTF = new JTextField();
		airacTF.setText("");
		airacTF.setColumns(10);

		downloadB = new JButton("Download");
		downloadB.addActionListener(buttonListener);
		
		downloadingL = new JLabel("");

		pathPane.add(select, BorderLayout.LINE_START);
		pathPane.add(pathTF, BorderLayout.LINE_END);
		pane.add(pathPane,	 BorderLayout.PAGE_START);
		pane.add(airacL,	 BorderLayout.LINE_START);
		pane.add(airacTF,	 BorderLayout.CENTER);
		downloadPane.add(downloadB,	 BorderLayout.PAGE_END);
		pane.add(downloadPane, BorderLayout.PAGE_END);
		pane.add(downloadingL, BorderLayout.PAGE_END);
		
		pack();
	}

	public static void main(String[] args) {
		chartsUpdater = new ChartsUpdater();
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Download button.
			if(e.getSource() == downloadB) {
				// Check that a folder has been selected and a valid AIRAC has been provided.
				try {				
					file.getAbsolutePath();
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(getParent(), "No folder selected.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					airac = Integer.parseInt(airacTF.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(getParent(), "Invalid AIRAC.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				downloadWorker = new DownloadWorker();
				downloadWorker.execute();
				downloadB.setEnabled(false);
				downloadingL.setText("Downloading...");
			}
		}
	}
}
