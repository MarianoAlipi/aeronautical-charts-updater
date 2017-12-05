package com.marianoalipi.chartsupdater;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Settings extends JFrame {

	final Dimension SIZE = new Dimension(600, 600);
	public static Settings currentInstance;
	private static final long serialVersionUID = 1L;
	
	JButton saveB, cancelB, restoreDefaultB, aptAddB, aptDelB, propsAddB, propsDelB;
	JTextField urlTF;
	DefaultListModel<String> airportNamesLM = new DefaultListModel<String>();
	JList<String> airportsL = new JList<String>(airportNamesLM),
				  propsL    = new JList<String>();
	JScrollPane scrollPaneAirports = new JScrollPane(),
				scrollPaneProps    = new JScrollPane();
	
	////////
	
	private String ip = DataIO.ip;
	private ArrayList<Airport> airports = DataIO.airports;
	
	////////
	
	public Settings() {
		
		setSize(SIZE);
		setTitle("Charts Updater " + ChartsUpdater.version + " - Settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(SIZE));
		setMaximumSize(new Dimension(SIZE));
		setPreferredSize(new Dimension(SIZE));
		setResizable(false);
		setVisible(true);
		currentInstance = this;
		setAlwaysOnTop(true);
		// Action to be performed when clicking "X" button.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				cancelB.doClick();
			}
		});
		
		ButtonListener buttonListener = new ButtonListener();
		
// === LOADING READ DATA ===
		
		DataIO.read();
		for(Airport apt : airports) {
			airportNamesLM.addElement(apt.getCode());
		}
		
// === CONTENT PANE ===
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		setContentPane(pane);
		pane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
// === URL PANEL ===
		
		JPanel urlPanel = new JPanel(new BorderLayout());
		
		JLabel urlLbl = new JLabel("URL:");
		urlTF = new JTextField(ip);
		urlTF.setColumns(47);
		
		urlPanel.add(urlLbl, BorderLayout.WEST);
		urlPanel.add(urlTF,  BorderLayout.EAST);
		
// === AIRPORTS PANEL ===
		
		JPanel airportsPanel = new JPanel(new BorderLayout());
		airportsPanel.setBorder(BorderFactory.createTitledBorder("Airports"));
		
		airportsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		airportsL.setSelectedIndex(0);
		scrollPaneAirports.setViewportView(airportsL);
		scrollPaneAirports.setPreferredSize(new Dimension(70, 415));
		
		JPanel airportsButtonsPanel = new JPanel(new FlowLayout());
		aptAddB = new JButton("┼");
		aptAddB.addActionListener(buttonListener);
		
		aptDelB = new JButton("—");
		aptDelB.addActionListener(buttonListener);
		aptDelB.setEnabled(false);
		
		airportsButtonsPanel.add(aptAddB);
		airportsButtonsPanel.add(aptDelB);
		
		airportsPanel.add(scrollPaneAirports, BorderLayout.NORTH);
		airportsPanel.add(airportsButtonsPanel, BorderLayout.SOUTH);
		
// === PROPERTIES PANEL ===
		
		JPanel propsPanel = new JPanel(new BorderLayout());
		propsPanel.setBorder(BorderFactory.createTitledBorder("Properties"));
		
		propsL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		propsL.setSelectedIndex(0);
		scrollPaneProps.setViewportView(propsL);
		scrollPaneProps.setPreferredSize(new Dimension(415, 415));
		
		ListSelectionModel lsm = airportsL.getSelectionModel(); 
        lsm.addListSelectionListener(new ListSelectionListener() {	
        	public void valueChanged(ListSelectionEvent e) {
        		if (!e.getValueIsAdjusting()) {
//        			String selectedAirport = airportNamesLM.getElementAt(airportsL.getSelectedIndex());
        			propsL.setListData(airports.get(airportsL.getSelectedIndex()).getPairedFiles());
        			// Set default selection.
        			propsL.setSelectedIndex(0);
        			
        			aptDelB.setEnabled(true);
        			propsAddB.setEnabled(true);
        			propsDelB.setEnabled(true);
        		}
        	}
        });
        
		
        JPanel propsButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		propsAddB = new JButton("┼");
		propsAddB.addActionListener(buttonListener);
		propsAddB.setEnabled(false);
		
		propsDelB = new JButton("—");
		propsDelB.addActionListener(buttonListener);
		propsDelB.setEnabled(false);
		
		propsButtonsPanel.add(propsAddB);
		propsButtonsPanel.add(propsDelB);
        
        propsPanel.add(scrollPaneProps, BorderLayout.NORTH);
        propsPanel.add(propsButtonsPanel, BorderLayout.SOUTH);
        
// === BUTTONS PANEL ===
		
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		JPanel buttonsLeftPanel = new JPanel();
		JPanel buttonsRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		restoreDefaultB = new JButton("Restore default settings");
		restoreDefaultB.addActionListener(buttonListener);
		
		saveB = new JButton("Save");
		saveB.addActionListener(buttonListener);
		
		cancelB = new JButton("Cancel");
		cancelB.addActionListener(buttonListener);
		cancelB.requestFocus();
		
		buttonsLeftPanel.add(restoreDefaultB);
		
		buttonsRightPanel.add(saveB);
		buttonsRightPanel.add(cancelB);
		
		buttonsPanel.add(buttonsLeftPanel, BorderLayout.WEST);
		buttonsPanel.add(buttonsRightPanel, BorderLayout.EAST);
		
// === ADD COMPONENTS TO CONTENT PANE ===
		
		pane.add(urlPanel,      BorderLayout.NORTH);
		pane.add(airportsPanel, BorderLayout.WEST);
		pane.add(propsPanel,    BorderLayout.EAST);
		pane.add(buttonsPanel,  BorderLayout.SOUTH);
		
		// Change the selected item to detect the change and show the properties.
		airportsL.setSelectedIndex(1);
		airportsL.setSelectedIndex(0);
		
		cancelB.requestFocusInWindow();
	}
	
// === BUTTONS HANDLER ===
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			int option, index, index2;
			String code, urlSegment, fileName;
			Object source = e.getSource();

			if (source == restoreDefaultB) {
				
				option = JOptionPane.showConfirmDialog(currentInstance, "Are you sure you want to restore the default settings?\nAll custom airports and files will be lost.", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					
					dispose();
					DataIO.restoreDefaultSettings();
					
					ChartsUpdater.chartsUpdater.setEnabled(true);
					ChartsUpdater.chartsUpdater.toFront();
					
				} else {
					return;
				}
				
			} else if (source == saveB) {
				option = JOptionPane.showConfirmDialog(currentInstance, "Are you sure you want to save the changes?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					// Save the data.
					DataIO.airports = airports;
					ip = urlTF.getText();
					DataIO.ip = ip;
					DataIO.save();
					
					ChartsUpdater.chartsUpdater.setEnabled(true);
					ChartsUpdater.chartsUpdater.toFront();
					dispose();
				} else {
					// Return to "Settings" window.
					return;
				}
			} else if (source == cancelB) {
				option = JOptionPane.showConfirmDialog(currentInstance, "Are you sure you want to discard the changes?\nThe previous settings will be kept.", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					
					ip = DataIO.ip;
					airports = new ArrayList<>();
					airports = DataIO.airports;
					airportNamesLM = new DefaultListModel<String>();
					
					ChartsUpdater.chartsUpdater.setEnabled(true);
					ChartsUpdater.chartsUpdater.toFront();
					dispose();
				} else {
					// Return to "Settings" window.
					return;
				}
			} else if (source == aptAddB) {
				  code = JOptionPane.showInputDialog(currentInstance, "Enter the airport's code:\nE.g. \"KBOS\"", "Add airport", JOptionPane.QUESTION_MESSAGE);
				  if (code == null) return;
				  urlSegment = JOptionPane.showInputDialog(currentInstance, "Enter the airport's URL segment:\n\nE.g. In the URL \"http://155.178.201.160/d-tpp/1707/00058ILD15R.pdf\",\n\"00058\" is the airport's segment.", "Add airport", JOptionPane.QUESTION_MESSAGE);
				  try {
					  urlSegment = String.format("%05d", Integer.parseInt(urlSegment));
				  } catch (Exception ex) {
					  // nothing
				  }
				  
				  if (code != null && urlSegment != null) {
					  code = code.toUpperCase();
					  Airport apt = new Airport(code, urlSegment);
					  airports.add(apt);
					  airportNamesLM.addElement(apt.getCode());
					  airportsL.setSelectedIndex(airportNamesLM.size() - 1);
				  }	  
			} else if (source == aptDelB) {
				
				option = JOptionPane.showConfirmDialog(currentInstance, "Are you sure you want to delete this airport?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (option == JOptionPane.YES_OPTION) {
					index = airportsL.getSelectedIndex();
					airportsL.setSelectedIndex( (index + 1 >= airportsL.getModel().getSize() ) ? index - 1 : index + 1 );
					airports.remove(index);
					airportNamesLM.remove(index); // Refresh/regenerate airportNamesLM!
					// This should be temporary. If user saves changes, delete from savedSettings.
				} else {
					return;
				}
			} else if (source == propsAddB) {
				urlSegment = JOptionPane.showInputDialog(currentInstance, "Enter the file's URL segment:\n\nE.g. In the URL \"http://155.178.201.160/d-tpp/1707/00058ILD15R.pdf\",\n\"ILD15R\" is the file's segment.", "Add file", JOptionPane.QUESTION_MESSAGE);
				if (urlSegment == null) return;
				fileName = JOptionPane.showInputDialog(currentInstance, "Enter the name of the file once saved:\n\nE.g. The file downloaded from \"http://155.178.201.160/d-tpp/1707/00058ILD15R.pdf\",\nis saved as\"ILS 15R\".", "Add file", JOptionPane.QUESTION_MESSAGE);
				
				index = airportsL.getSelectedIndex();
				Airport workingAirport = airports.get(index);
				workingAirport.filesAL.add(new String[]{urlSegment, fileName});
				workingAirport.saveFilesToArray();
				workingAirport.setPairedFiles(workingAirport.generatePairedFiles());
				// Reload propsL:
				propsL.setListData(workingAirport.getPairedFiles());
				
			} else if (source == propsDelB) {

				option = JOptionPane.showConfirmDialog(currentInstance, "Are you sure you want to delete this file?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (option == JOptionPane.YES_OPTION) {

					index = airportsL.getSelectedIndex();
					index2 = propsL.getSelectedIndex();

					propsL.setSelectedIndex( (index2 + 1 >= propsL.getModel().getSize() + 1) ? index2 - 1 : index2 );

					if (index2 > 0) {
						Airport workingAirport = airports.get(index);

						workingAirport.filesAL.remove(index2 - 1);
						workingAirport.saveFilesToArray();
						workingAirport.setPairedFiles(workingAirport.generatePairedFiles());
						workingAirport.savePairedFilesToAL();

						// Reload propsL:
						propsL.setListData(workingAirport.getPairedFiles());
						propsL.setSelectedIndex( (index2 + 1 >= propsL.getModel().getSize() + 1) ? index2 - 1: index2);
					} else {
						JOptionPane.showMessageDialog(currentInstance, "You can't delete the airport's URL segment.", "Action not allowed", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				} else {
					return;
				}
			}
		}
	}
	
// === MISC GETTERS ===
	
	public String getIp() {
		return this.ip;
	}
	
	public ArrayList<Airport> getAirports() {
		return this.airports;
	}
	
}
