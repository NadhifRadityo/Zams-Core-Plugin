package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Things.UserInterface.Console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UnsupportedLookAndFeelException;

import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.WindowHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.Button;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow.WindowPosition;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.ModernScrollPane;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Utilization.ConsoleUtils;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Utilization.CustomOutputStream;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Utilization.WindowUtils;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class ConsoleUserInterface {
	private FrameWindow console;
	private JPanel consoleContentPanelHeader;
	private JPanel consoleContentPanelMain;
	private JPanel consoleToolbarPanel;
	private JPanel consoleGroupPanel;
	private JPanel consoleContentPanelGroup;
	
	private Map<String, JButton> groupButton = new HashMap<String, JButton>();
	private Map<String, ConsoleGroup> consoleGroup = new HashMap<String, ConsoleGroup>();
	private String currentGroup;
	
	public ConsoleUserInterface(String title, int width, int height, List<String> group, WindowHelper helper) {
		this(title, width, height, group.toArray(new String[group.size()]), helper);
	}
	
	public ConsoleUserInterface(String title, int width, int height, String[] group, WindowHelper helper) {
		console = new FrameWindow(title, width, height, helper);
		console.getContentPane().setLayout(new MigLayout());
		
		initHeaderPanel();
		initHeader();
		initConsoleMainPanel();
		initToolbarPanel();
		initToolbarTools();
		initConsoleGroupPanel();
		
		for(String item : group) {
			//Group Panel
			JPanel consolePanel = new JPanel(new MigLayout());
			consolePanel.setOpaque(false);
			consolePanel.setVisible(true);
			
			//Text Area
			JTextArea consoleTextArea = new JTextArea(console.getWidth() + consoleToolbarPanel.getWidth() + 100, console.getHeight());
			consoleTextArea.setEditable(false);
			consoleTextArea.setWrapStyleWord(true);
			consoleTextArea.setLineWrap(true);
			consoleTextArea.setBackground(new Color(206, 207, 212));
			consoleTextArea.setForeground(new Color(77, 77, 79));
			consoleTextArea.setOpaque(true);
			
			//PrintStream & ScrollPane
			PrintStream printStream = new PrintStream(new CustomOutputStream(consoleTextArea));
			JScrollPane scrollPane = new ModernScrollPane(consoleTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			consolePanel.add(scrollPane);
			
			//Button
			Button groupButton = new Button(item);
			groupButton.setForeground(new Color(206, 207, 212));
			groupButton.setBackground(consoleContentPanelGroup.getBackground());
			groupButton.setOpaque(true);
			Color hoverColor = consoleContentPanelGroup.getBackground();
			groupButton.setHoverBackgroundColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue()).darker());
			groupButton.setPressedBackgroundColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue()).darker().darker());
			WindowUtils.setFlatStyleButtonWithBackground(groupButton);
			consoleContentPanelGroup.add(groupButton, "wrap, center");
			
//			consoleGroupPanel.add(consolePanel);
			
			//Button Event
			groupButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	selectGroup(groupButton.getText());
	            }
	        });
			
			consoleGroup.put(item, new ConsoleGroup(item, consoleTextArea, scrollPane, printStream, consolePanel, groupButton));
		}
		
		selectGroup(group[0]);
		
//		FrameWindow console = new FrameWindow("Console", 455, 256, new WindowHelper());
	}
	
	private void initToolbarPanel() {
		consoleToolbarPanel = new JPanel();
		consoleToolbarPanel.setBackground(new Color(89, 90, 91));
		consoleToolbarPanel.setOpaque(true);
		console.add(consoleToolbarPanel, new CC().dockEast());
	}
	
	private void initToolbarTools() {
		initClearConsoleButton();
	}
	
	private void initClearConsoleButton() {
		Button clearConsoleButton = new Button("Clear console");
		clearConsoleButton.setForeground(new Color(206, 207, 212));
		clearConsoleButton.setBackground(consoleToolbarPanel.getBackground());
		clearConsoleButton.setOpaque(true);
		Color hoverColor = consoleToolbarPanel.getBackground();
		clearConsoleButton.setHoverBackgroundColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue()).darker());
		clearConsoleButton.setPressedBackgroundColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue()).darker().darker());
		WindowUtils.setFlatStyleButtonWithBackground(clearConsoleButton);
		consoleToolbarPanel.add(clearConsoleButton);
		
		clearConsoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	consoleGroup.get(currentGroup).getTextArea().setText("");
            }
        });
	}
	
	private void initHeaderPanel() {
		consoleContentPanelHeader = new JPanel(new MigLayout());
		consoleContentPanelHeader.setBackground(new Color(59, 63, 72));
		consoleContentPanelHeader.setOpaque(true);
		console.add(consoleContentPanelHeader, new CC().dockNorth());
	}
	
	private void initHeader() {
		JLabel headerTitle = new JLabel("Console: " );
		WindowUtils.setFontSize(headerTitle, 20);
		headerTitle.setForeground(new Color(22, 161, 130));
		headerTitle.setSize(new Dimension(headerTitle.getWidth(), 768));
		consoleContentPanelHeader.add(headerTitle, "span, gapleft 2%, gapbottom 2%, gaptop 2%");
	}
	
	private void initConsoleMainPanel() {
		consoleContentPanelMain = new JPanel(new MigLayout());
		console.add(consoleContentPanelMain);
	}
	
	private void initConsoleGroupPanel() {
		consoleContentPanelGroup = new JPanel(new MigLayout());
		consoleContentPanelGroup.setBackground(new Color(83, 94, 100));
		consoleContentPanelGroup.setOpaque(true);
		consoleContentPanelMain.add(consoleContentPanelGroup, new CC().dockWest());
		
		consoleGroupPanel = new JPanel(new MigLayout());
		consoleGroupPanel.setOpaque(false);
		consoleContentPanelMain.add(consoleGroupPanel, new CC().dockEast());
	}
	
	private void selectGroup(String g) {
//		System.out.println("from panel: " + currentGroup);
//		System.out.println("to panel: " + g);
		
		if(currentGroup != null) {
			JPanel panel = consoleGroup.get(currentGroup).getPanel();
			consoleGroupPanel.remove(panel);
		}
		JPanel panel = consoleGroup.get(g).getPanel();
		consoleGroupPanel.add(panel);
		currentGroup = g;
		
		consoleGroupPanel.validate();
		consoleGroupPanel.repaint();
	}
	
	public Map<String, JButton> getGroupButton() {
		return groupButton;
	}
	
	public void setToConsoleOutput(String g) {
		ConsoleUtils.setOuput(consoleGroup.get(g).getPrintStream());
	}
	public void setToConsoleErrorOutput(String g) {
		ConsoleUtils.setError(consoleGroup.get(g).getPrintStream());
	}
	public void add(String g, String text) {
		consoleGroup.get(g).getPrintStream().println(text);
	}
	
	public PrintStream getConsolePrintStream(String g) {
		return consoleGroup.get(g).getPrintStream();
	}
	public JTextArea getConsoleTextArea(String g) {
		return consoleGroup.get(g).getTextArea();
	}
	public FrameWindow getConsoleWindow() {
		return this.console;
	}
	
	public void show() {
		console.setLocation(WindowPosition.TOP_RIGHT);
		
//		for(Entry<String, ConsoleGroup> pair : consoleGroup.entrySet()) {
//			Dimension fixedSize = new Dimension(console.getWidth() - consoleContentPanelGroup.getWidth() - consoleToolbarPanel.getWidth(), console.getHeight() - consoleContentPanelHeader.getHeight());
//			pair.getValue().getPanel().setSize(fixedSize);
//			pair.getValue().getPanel().setPreferredSize(fixedSize);
//		}
		
		this.console.display();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		List<String> g = new ArrayList<String>();
		g.add("Chat");
		g.add("Command");
		
		ConsoleUserInterface console = new ConsoleUserInterface("Console", 455, 256, g.toArray(new String[g.size()]), new WindowHelper());
//		console.show();
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		console.show();
		
		console.setToConsoleOutput("Chat");
		System.out.println("AWWWW!!");
		ConsoleUtils.setOutputToDefault();
		System.out.println("HILIHHH!!");
		console.add("Chat", "APASIHHHH");
//		console.setToConsoleOutput("Chat");
		
	}
}
