package io.github.NadhifRadityo.ZamsNetwork.Core.Things;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.WindowHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow.WindowPosition;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.ItemUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Testing implements GetCommandExecutorClass {
	@SuppressWarnings("unused")
	private Main Plugin;
	private boolean isInit;

	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		return this.isInit;
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}
	
	@SuppressWarnings("unused")
	private void applethingy(CommandSender sender, String[] args) {
		if(args.length > 0 && args[0].equalsIgnoreCase("get")) {
			ItemStack apple = new ItemStack(Material.APPLE);
			String hide = ItemUtils.hideText("TESTING");
			sender.sendMessage(hide);
			ItemMeta meta = apple.getItemMeta();
			String fixedName = meta.getDisplayName() != null ? meta.getDisplayName() : Utils.getName(apple);
			
			meta.setDisplayName(hide + "§r" + fixedName);
			apple.setItemMeta(meta);
			
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(apple);
			}
		}else if(args.length <= 0){
			if(sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack item = player.getInventory().getItemInMainHand();
				if(item != null && item.getItemMeta().getDisplayName() != null) {
					String text = item.getItemMeta().getDisplayName();
					player.sendMessage(ItemUtils.revealText(text));
				}
;			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//		applethingy(sender, args);
//		this.Plugin.getServer()
		
		
		FrameWindow loading = new FrameWindow("Graph", 455, 256, this.Plugin.Helper.WindowHelper);
		loading.setFixedSize(455, 256);
		loading.setDraggable(false);
		loading.setResizable(false);
		
		GroupLayout loading_layout = new GroupLayout(loading.getContentPane());
		loading.getContentPane().setLayout(loading_layout);
		
		URL logo = this.getClass().getResource("/Logo.png");
		loading.setBackground(new ImageIcon(logo));
		
		
		
		URL load_img = this.getClass().getResource("/loading.gif");
		ImageIcon image = new ImageIcon(load_img);
		JLabel loading_image = loading.addImageLabel(image, new Point(loading.getWidth() / 2 - image.getIconWidth() / 2, 96 / 100 * loading.getHeight()));
		
		JLabel loading_text = new JLabel("Loading...");
		loading_text.setSize(loading.getWidth(), loading_text.getHeight());
		loading_text.setHorizontalAlignment(JLabel.CENTER);
		loading_text.setLocation(loading.getWidth() / 2 - loading_text.getWidth() / 2, 97 / 100 * loading.getHeight());
		
		loading_layout.setVerticalGroup(
			loading_layout.createSequentialGroup()
			.addGroup(loading_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(loading_image)
				.addComponent(loading_text)
			)
		);
		
		
		
//		JLabel a = new JLabel(image);
//		a.setIcon(image);
//		loading.add(a);
//		loading.setComponentZOrder(loading_image, 0);
//		loading_image.setBounds(loading_image.getX(), loading_image.getY(), 80 / 100 * image.getIconWidth(), 80 / 100 * image.getIconHeight());
		
		loading.display();
		loading.setLocation(WindowPosition.CENTER);
		
		return true;
	}
	
	public static void main(String[] args) {

		FrameWindow loading = new FrameWindow("User Interface", 455, 256, new WindowHelper());
		loading.setFixedSize(455, 256);
		loading.setDraggable(false);
		loading.setResizable(false);
		
		URL logo = Testing.class.getClass().getResource("/Logo.png");
		loading.setBackground(new ImageIcon(logo));
		
		//Main loading panel
		JPanel loading_panel = new JPanel();
		loading_panel.setLayout(new GridLayout(2, 1));
		
		loading_panel.setSize(new Dimension(loading.getWidth(), loading.getHeight() / 3));
		loading_panel.setPreferredSize(new Dimension(loading.getWidth(), loading.getHeight() / 3));
		loading_panel.setLocation(0, loading.getHeight() - loading_panel.getHeight());
		loading_panel.setOpaque(false);
		
		//Loading animation
		URL load_img = Testing.class.getClass().getResource("/loading.gif");
		ImageIcon image = new ImageIcon(load_img);
//		JLabel loading_image = FrameWindow.createImageLabel(image, new Point(loading.getWidth() / 2 - image.getIconWidth() / 2, 345));
		JLabel loading_image = FrameWindow.createImageLabel(image, new Point(0, 0));
		loading_panel.add(loading_image);
		
		//Loading text panel
		JPanel loading_text_panel = new JPanel(new GridLayout(2, 1));
		loading_text_panel.setSize(new Dimension(loading_panel.getWidth(), loading_panel.getHeight() / 2));
		loading_text_panel.setOpaque(false);
		
		//Loading text
		JLabel loading_text = new JLabel("Loading...");
		loading_text.setSize(new Dimension(loading.getWidth(), 5));
		loading_text.setOpaque(false);
		loading_text.setVisible(true);
		loading_text.setHorizontalAlignment(JLabel.CENTER);
//		loading_text.setLocation(loading.getWidth() / 2 - loading_text.getWidth() / 2, 350 - loading_text.getHeight());
		loading_text.setLocation(0,0);
		loading_text_panel.add(loading_text);
		
		loading_panel.add(loading_text_panel);
		loading.getContentPane().setLayout(null);;
		loading.add(loading_panel);

		loading.display();
		loading.setLocation(WindowPosition.CENTER);
	}
	
}
