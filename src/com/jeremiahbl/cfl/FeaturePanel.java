package com.jeremiahbl.cfl;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.*;

public class FeaturePanel extends JPanel {
	private File targetFile;
	
	private JTabbedPane tabPane = new JTabbedPane();
	private JPanel featurePanel = new JPanel();	
	private JPanel overviewPanel = new JPanel();
	private JScrollPane fileAreaScrollPane = new JScrollPane();
	private JTextArea fileTextArea = new JTextArea();
	
	public FeaturePanel() {
		setLayout(new BorderLayout());
		add(tabPane, BorderLayout.CENTER);
		tabPane.addTab("Feature list", featurePanel);
		tabPane.addTab("Overview", overviewPanel);
		
		initOverviewPanel();
		initFeaturePanel();
		setTargetFile(null);
		
		addListeners();
	}
	
	private void initOverviewPanel() {
		overviewPanel.setLayout(new BorderLayout());
		overviewPanel.add(fileAreaScrollPane, BorderLayout.CENTER);
		fileAreaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		fileAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fileAreaScrollPane.setViewportView(fileTextArea);
	}
	private void initFeaturePanel() {
		featurePanel.setLayout(new BorderLayout());
	}
	private void addListeners() {
		
	}
	
	public void setTargetFile(File f) {
		targetFile = f;
		fileTextArea.setEditable(f != null ? f.canWrite() : false);
		String txt = "";
		if(targetFile != null) {
			try {
				FileInputStream fis = new FileInputStream(targetFile);
				while(fis.available() > 0)
					txt += (char) fis.read();
				fis.close();
			} catch(IOException ioe) {
				txt = "IOException when reading target file!";
				txt += "\n" + ioe.getMessage();
			}
		} else txt = "Please select a target";
		fileTextArea.setText(txt);
	}
	public File getTargetFile() { return targetFile; }
}
