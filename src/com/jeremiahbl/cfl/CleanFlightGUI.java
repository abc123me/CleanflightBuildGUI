package com.jeremiahbl.cfl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.net16.jeremiahlowe.shared.SwingUtility;
import net.net16.jeremiahlowe.shared.gui.BrowseButton;

public class CleanFlightGUI extends JFrame {
	private File cleanflightDirectory;
	
	private JPanel mainConfigPanel = new JPanel();
	private JTextField cleanflightDirectoryField = new JTextField();
	private JList<Target> targets = new JList<Target>();
	
	public static void main(String[] args) {
		CleanFlightGUI cf = new CleanFlightGUI();
		cf.setSize(500, 400);
		cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SwingUtility.centerJFrame(cf);
		cf.setVisible(true);
	}
	
	public CleanFlightGUI() {
		mainConfigPanel.setLayout(new BorderLayout());
		
		Box vb = Box.createVerticalBox();
		Box hb = Box.createHorizontalBox();
		hb.add(new JLabel("Cleanflight directory"));
		hb.add(Box.createHorizontalStrut(5));
		hb.add(cleanflightDirectoryField);
		hb.add(new BrowseButton(BrowseButton.DIRECTORY, cleanflightDirectoryField));
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener((ae) -> {
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
					targets.setListData(new Target[0]);
					targets.setEnabled(false);
				}
			} else JOptionPane.showMessageDialog(mainConfigPanel, msg);
		});
		hb.add(okBtn);
		vb.add(hb);
		mainConfigPanel.add(vb, BorderLayout.NORTH);
		
		targets.setEnabled(false);
		JScrollPane targetVP = new JScrollPane(targets);
		mainConfigPanel.add(targetVP, BorderLayout.CENTER);
		
		hb = Box.createVerticalBox();
		hb.add(new JLabel("Please select a target"));
		hb.add(new JButton("Select"));
		mainConfigPanel.add(hb, BorderLayout.SOUTH);
		
		setContentPane(mainConfigPanel);
		
	}
	
	private static ArrayList<Target> getTargets(File dir) {
		ArrayList<Target> out = new ArrayList<Target>();
		File tsrc = new File(dir.getPath() + "/src/main/target/");
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