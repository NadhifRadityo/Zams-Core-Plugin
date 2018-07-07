package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Utilization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class WindowUtils {
	public static void setFlatStyleButton(JButton button) {
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
	}
	
	public static void setFlatStyleButtonWithBackground(JButton button) {
		button.setBorderPainted(false);
		button.setFocusPainted(false);
	}
	
	public static void setBackgroundWhenHovered(Component button, Color colorWhenHovered, Color colorAfterHovered) {
		button.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent evt) {
		    	button.setBackground(colorWhenHovered);
		    }

		    public void mouseExited(MouseEvent evt) {
		    	button.setBackground(colorAfterHovered);
		    }
		});
	}
	
	public static void setBackgroundWhenHovered(Component button, Color colorWhenHovered) {
		setBackgroundWhenHovered(button, colorWhenHovered, button.getBackground());
	}
	
	public static void setFontSize(Component label, int size) {
		Font labelFont = label.getFont();
		label.setFont(new Font(labelFont.getName(), labelFont.getStyle(), size));
	}
}
