package Test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JPanelTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame("aw");
		
		JPanel main = new JPanel( new FlowLayout(FlowLayout.CENTER, 0, 0) );
//		main.setLayout(null);
		main.setBackground(Color.BLACK);
		main.setOpaque(true);

		JPanel panel1 = new JPanel();
		panel1.setPreferredSize( new Dimension(100, 100) );
		panel1.add( new JButton("Panel1") );
		panel1.setBackground(Color.BLUE);
		panel1.setLayout(null);
		main.add( panel1 );

		JPanel panel2 = new JPanel();
		panel2.setPreferredSize( new Dimension(100, 100) );
		panel2.add( new JButton("Panel2") );
		panel2.setBackground(Color.YELLOW);
		panel2.setLayout(null);
		main.add( panel2 );

		frame.add( main );
		frame.pack();
		frame.setVisible(true);
	}
}
