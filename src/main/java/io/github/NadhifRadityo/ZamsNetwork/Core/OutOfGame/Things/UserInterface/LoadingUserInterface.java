package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Things.UserInterface;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.WindowHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow.WindowPosition;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class LoadingUserInterface {
	private WindowHelper helper;
	private FrameWindow loading;
	
	private JPanel loading_panel;
	private JPanel loading_text_panel;
	private JLabel loading_text;
	private JLabel loading_desc;
	
	public LoadingUserInterface(String title, int width, int height, WindowHelper helper) {
		this.helper = helper;
		this.loading = new FrameWindow(title, width, height, this.helper);
//		this.loading = new FrameWindow("User Interface", 455, 256, new WindowHelper());

		this.initLoadingPanel();
		this.initLogo();
		this.initLoadingTextPanel();
		this.initLoadingText();
		this.initLoadingDescription();
	}
	
	private void initLoadingPanel() {
		//Main loading panel
		JPanel loading_panel = new JPanel();
		loading_panel.setLayout(new GridLayout(2, 1));
		
		loading_panel.setSize(new Dimension(loading.getWidth(), loading.getHeight() / 3));
		loading_panel.setPreferredSize(new Dimension(loading.getWidth(), loading.getHeight() / 3));
		loading_panel.setLocation(0, loading.getHeight() - loading_panel.getHeight());
		loading_panel.setOpaque(false);
		this.loading_panel = loading_panel;
	}
	
	private void initLogo() {
		URL logo = Main.class.getClass().getResource("/Logo.png");
		if(logo != null)
			loading.setBackground(new ImageIcon(logo));
	}
	
	private JLabel initLoadingAnim() {
		URL load_img = Main.class.getClass().getResource("/loading.gif");
		if(load_img != null) {
			ImageIcon image = new ImageIcon(load_img);
	//		JLabel loading_image = FrameWindow.createImageLabel(image, new Point(loading.getWidth() / 2 - image.getIconWidth() / 2, 345));
			JLabel loading_image = FrameWindow.createImageLabel(image, new Point(0, 0));
			return loading_image;
		}
		return new JLabel();
	}
	
	private void initLoadingTextPanel() {
		JPanel loading_text_panel = new JPanel(new GridLayout(2, 1));
		loading_text_panel.setSize(new Dimension(loading_panel.getWidth(), loading_panel.getHeight() / 2));
		loading_text_panel.setOpaque(false);
		this.loading_text_panel = loading_text_panel;
	}
	
	private void initLoadingText() {
		JLabel loading_text = new JLabel("Loading...");
		loading_text.setSize(new Dimension(loading.getWidth(), 5));
		loading_text.setOpaque(false);
		loading_text.setVisible(true);
		loading_text.setHorizontalAlignment(JLabel.CENTER);
//		loading_text.setLocation(loading.getWidth() / 2 - loading_text.getWidth() / 2, 350 - loading_text.getHeight());
		loading_text.setLocation(0,0);
		this.loading_text = loading_text;
	}
	
	private void initLoadingDescription() {
		JLabel loading_desc = new JLabel("");
		loading_desc.setSize(new Dimension(loading.getWidth(), 5));
		loading_desc.setOpaque(false);
		loading_desc.setVisible(true);
		loading_desc.setHorizontalAlignment(JLabel.CENTER);
//		loading_desc.setLocation(loading.getWidth() / 2 - loading_text.getWidth() / 2, 350 - loading_text.getHeight());
		loading_desc.setLocation(0,0);
		this.loading_desc = loading_desc;
	}
	
	public void setLoadingTextMessage(String msg) {
		if(this.loading_text != null) {
			this.loading_text.setText(msg);
		}
	}
	public void setLoadingTextDescription(String desc) {
		if(this.loading_desc != null) {
			this.loading_desc.setText(desc);
		}
	}
	
	public void showWindow() {
		loading.setFixedSize(455, 256);
		loading.setDraggable(false);
		loading.setResizable(false);
		
		loading_panel.add(this.initLoadingAnim());
		loading_text_panel.add(this.loading_text);
		loading_text_panel.add(this.loading_desc);
		
		loading_panel.add(this.loading_text_panel);
		loading.getContentPane().setLayout(null);
		loading.add(loading_panel);

		loading.display();
		loading.setLocation(WindowPosition.CENTER);
	}
}
