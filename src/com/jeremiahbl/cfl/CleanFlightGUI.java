package com.jeremiahbl.cfl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import net.net16.jeremiahlowe.shared.SwingUtility;
import net.net16.jeremiahlowe.shared.gui.BrowseButton;

public class CleanFlightGUI extends JFrame {
	private File cleanflightDirectory;
	
	private JPanel mainPanel = new JPanel();
	private JPanel mainConfigPanel = new JPanel();
	private FeaturePanel featurePanel = new FeaturePanel();
	
	private JTextField cleanflightDirectoryField = new JTextField();
	private JList<Target> targets = new JList<Target>();
	private JButton cleanflightOkBtn = new JButton("OK");
	private BrowseButton cleanflightBrowseButton = new BrowseButton(BrowseButton.DIRECTORY, cleanflightDirectoryField);
	private JButton selectBtn = new JButton("Select");
	
	public static void main(String[] args) {
		CleanFlightGUI cf = new CleanFlightGUI();
		cf.setSize(800, 600);
		cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SwingUtility.centerJFrame(cf);
		cf.setVisible(true);
	}
	
	public CleanFlightGUI() {
		Box vb, hb;
		vb = Box.createVerticalBox();
		vb.add(new JLabel("Cleanflight directory"));
		int f = cleanflightDirectoryField.getFont().getSize();
		cleanflightDirectoryField.setPreferredSize(new Dimension(f * 10, f));
		
		hb = Box.createHorizontalBox();
		hb.add(cleanflightDirectoryField);
		hb.add(cleanflightBrowseButton);
		hb.add(cleanflightOkBtn);
		vb.add(hb);
		
		targets.setEnabled(false);
		JScrollPane targetVP = new JScrollPane(targets);
		hb = Box.createVerticalBox();
		hb.add(new JLabel("Please select a target"));
		hb.add(selectBtn);
		
		mainConfigPanel.setLayout(new BorderLayout());
		mainConfigPanel.add(vb, BorderLayout.NORTH);
		mainConfigPanel.add(targetVP, BorderLayout.CENTER);
		mainConfigPanel.add(hb, BorderLayout.SOUTH);
		
		mainPanel.setLayout(new BorderLayout());
		hb = Box.createHorizontalBox();
		hb.add(mainConfigPanel);
		hb.add(Box.createHorizontalStrut(3));
		hb.add(new JSeparator(JSeparator.VERTICAL));
		mainPanel.add(hb, BorderLayout.WEST);
		mainPanel.add(featurePanel, BorderLayout.CENTER);
		setContentPane(mainPanel);
		
		addListeners();
	}
	
	private void addListeners() {
		ActionListener al = (ae) -> {
			System.out.println("Cleanflight directory selected");
			cleanflightDirectory = new File(cleanflightDirectoryField.getText());
			String msg = null;
			if(!cleanflightDirectory.isDirectory())
				msg = "Cleanflight directory is not a directory!";
			else if(!cleanflightDirectory.exists())
				msg = "Cleanflight directory does not exist!";
			if(msg == null) {
				ArrayList<Target> dat = getTargets(cleanflightDirectory);
				if(dat != null) {
					targets.setListData(dat.toArray(new Target[0]));
					targets.setEnabled(true);
				} else {
					msg = "Target directory invalid";
					targets.setListData(new Target[0]);
					targets.setEnabled(false);
				}
			}
			if(msg != null)
				JOptionPane.showMessageDialog(mainPanel, msg);
		};
		selectBtn.addActionListener((ae) -> {
			Target t = targets.getSelectedValue();
			if(t != null && t.headerOK())
				featurePanel.setTargetFile(t.HEADER);
		});
		cleanflightOkBtn.addActionListener(al);
		cleanflightDirectoryField.addActionListener(al);
	}
	
	private static ArrayList<Target> getTargets(File dir) {
		File tsrc = new File(dir.getPath() + "/src/main/target/");
		if(!tsrc.exists() || !tsrc.isDirectory())
			return null;
		ArrayList<Target> out = new ArrayList<Target>();
		for(File targ : tsrc.listFiles()) {
			if(targ != null && targ.isDirectory()) {
				Target t = new Target(targ);
				if(t.valid()) out.add(t);
			}
		}
		return out;
	}
}
class Target {
	public final String NAME;
	public final File DIRECTORY;
	public final File HEADER;

	public Target(File dir) {
		NAME = dir.getName();
		DIRECTORY = dir;
		HEADER = new File(dir.getPath() + "/target.h");
	}
	
	public boolean nameOK() { return NAME != null; }
	public boolean dirOK()  { return DIRECTORY.exists() && DIRECTORY.isDirectory(); }
	public boolean headerOK() { return HEADER != null && HEADER.exists() && HEADER.isFile(); }
	public boolean valid() { return nameOK() && dirOK() && headerOK(); }
	
	@Override public String toString() {
		return NAME;
	}
}