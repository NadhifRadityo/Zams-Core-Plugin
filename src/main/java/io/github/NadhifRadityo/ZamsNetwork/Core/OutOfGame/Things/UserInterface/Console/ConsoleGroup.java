package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Things.UserInterface.Console;

import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleGroup {
	private String groupName;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private PrintStream printStream;
	private JPanel panel;
	private JButton button;
	
	public ConsoleGroup(String groupName, JTextArea textArea, JScrollPane scrollPane, PrintStream printStream, JPanel panel, JButton button) {
		this.groupName = groupName;
		this.textArea = textArea;
		this.scrollPane = scrollPane;
		this.printStream = printStream;
		this.panel = panel;
		this.button = button;
	}
	
	public String getName() {
		return groupName;
	}
	public JTextArea getTextArea() {
		return textArea;
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	public PrintStream getPrintStream() {
		return printStream;
	}
	public JPanel getPanel() {
		return panel;
	}
	public JButton getButton() {
		return button;
	}
}
