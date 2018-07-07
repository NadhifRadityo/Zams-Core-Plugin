package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.WindowHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Destroyable;

public class FrameWindow extends JFrame implements Destroyable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8521525554973784763L;
	
	private List<JLabel> labels;
	private WindowHelper helper;
	private boolean isDestroyed;
	private boolean autoDestroy;
	
	
	public FrameWindow(String title, int width, int height, WindowHelper helper) {
		super(title);
		this.setSize(width, height);
		
		this.helper = helper;
		this.isDestroyed = false;
		this.autoDestroy = true;
		
		this.helper.addFrameWindow(this);
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	removeOpenedWindow();
		        e.getWindow().dispose();
		    }
		});
	}
	
	public FrameWindow(String title, WindowHelper helper) {
		this(title, 0, 0, helper);
	}
	
	public FrameWindow setLocation(WindowPosition pos) {
		super.setLocation(pos.getPoint(super.getSize()));
		return this;
	}
	
	public void removeAllListeners() {
		for(ComponentListener ComponentListener : super.getComponentListeners()) {
			super.removeComponentListener(ComponentListener);
		}
		for(ContainerListener ContainerListener : super.getContainerListeners()) {
			super.removeContainerListener(ContainerListener);
		}
		for(FocusListener FocusListener : super.getFocusListeners()) {
			super.removeFocusListener(FocusListener);
		}
		for(HierarchyBoundsListener HierarchyBoundsListener : super.getHierarchyBoundsListeners()) {
			super.removeHierarchyBoundsListener(HierarchyBoundsListener);
		}
		for(HierarchyListener HierarchyListener : super.getHierarchyListeners()) {
			super.removeHierarchyListener(HierarchyListener);
		}
		for(InputMethodListener InputMethodListener : super.getInputMethodListeners()) {
			super.removeInputMethodListener(InputMethodListener);
		}
		for(KeyListener KeyListener : super.getKeyListeners()) {
			super.removeKeyListener(KeyListener);
		}
		for(MouseListener MouseListener : super.getMouseListeners()) {
			super.removeMouseListener(MouseListener);
		}
		for(MouseMotionListener MouseMotionListener : super.getMouseMotionListeners()) {
			super.removeMouseMotionListener(MouseMotionListener);
		}
		for(MouseWheelListener MouseWheelListener : super.getMouseWheelListeners()) {
			super.removeMouseWheelListener(MouseWheelListener);
		}
		for(PropertyChangeListener PropertyChangeListener : super.getPropertyChangeListeners()) {
			super.removePropertyChangeListener(PropertyChangeListener);
		}
		for(WindowFocusListener WindowFocusListener : super.getWindowFocusListeners()) {
			super.removeWindowFocusListener(WindowFocusListener);
		}
		for(WindowListener WindowListener : super.getWindowListeners()) {
			super.removeWindowListener(WindowListener);
		}
		for(WindowStateListener WindowStateListener : super.getWindowStateListeners()) {
			super.removeWindowStateListener(WindowStateListener);
		}
	}
	
	public List<JLabel> getLabels(){
		return this.labels;
	}
	
	public void setFixedSize(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
	}
	public void setFixedSize(Dimension dim) {
		this.setPreferredSize(dim);
		this.setSize(dim);
	}
	
	public void display() {
		this.setVisible(true);
		this.helper.addOpenedFrameWindow(this);
	}
	
	public FrameWindow close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		this.removeOpenedWindow();
		
		return this;
	}
	private void removeOpenedWindow() {
		this.helper.removeOpenedFrameWindow(this);
	}
	
	public JLabel addLabel(String desc, int width, int height, BorderLayout layout) {
		JLabel label = createLabel(desc, width, height, layout);
		labels.add(label);
		this.addLabel(label);
		
		return label;
	}
	public JLabel addLabel(String desc, Dimension dim, BorderLayout layout) {
		JLabel label = createLabel(desc, dim, layout);
		labels.add(label);
		this.addLabel(label);
		
		return label;
	}
	public FrameWindow addLabel(JLabel label) {
		this.getContentPane().add(label);
//		this.add(label);
		return this;
	}
	
	public FrameWindow setBackground(Icon background) {
		this.setContentPane(createLabel(background));
		this.update();
		
		return this;
	}
	
	public static void update(FrameWindow frame) {
		frame.setFixedSize(frame.getWidth() - 1, frame.getHeight() - 1);
		frame.setFixedSize(frame.getWidth() + 1, frame.getHeight() + 1);
		frame.setVisible(true);
		frame.setVisible(false);
	}
	
	public void update() {
		this.setFixedSize(this.getWidth() - 1, this.getHeight() - 1);
		this.setFixedSize(this.getWidth() + 1, this.getHeight() + 1);
		this.setVisible(true);
		this.setVisible(false);
	}
	
	public static JLabel createImageLabel(Icon image, Point pos) {
		JLabel container = createLabel(image);
		container.setVisible(true);
		container.setLocation(pos);
		container.setSize(image.getIconWidth(), image.getIconHeight());
		container.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
		
		return container;
	}
	
	public JLabel addImageLabel(Icon image, Point pos) {
		JLabel container = createImageLabel(image, pos);
		
		this.addLabel(container);
		this.validate();
		this.repaint();
		return container;
	}
	
	public static JLabel createLabel(String desc, int width, int height, BorderLayout layout) {
		return createLabel(desc, new Dimension(width, height), layout);
	}
	public static JLabel createLabel(String desc, Dimension dim, BorderLayout layout) {
		JLabel label = new JLabel(desc);
		label.setPreferredSize(dim);
		label.setLayout(layout);
		return label;
	}
	public static JLabel createLabel(Icon icon) {
		JLabel label = new JLabel(icon);
		return label;
	}
	public static JLabel createLabel(String desc, Icon icon, int align) {
		JLabel label = new JLabel(desc, icon, align);
		return label;
	}
	
	public FrameWindow setDraggable(boolean flag) {
		this.setUndecorated(!flag);
		return this;
	}

	@Override
	public boolean isDestroyed() {
		return this.isDestroyed;
	}

	@Override
	public boolean isAutoDestroy() {
		return this.isAutoDestroy();
	}

	@Override
	public void setAutoDestroy(boolean autoDestroy) {
		this.autoDestroy = autoDestroy;
	}

	@Override
	public void checkAutoDestroy() {
		if(this.autoDestroy) {
			this.destroy();
		}
	}

	@Override
	public void destroy() {
		this.close();
		this.removeAllListeners();
		this.isDestroyed = true;
		this.helper = null;
		this.labels = null;
	}
	
	public enum WindowPosition{
		CENTER, LEFT, TOP, RIGHT, BOTTOM,
		LEFT_CENTER,
		RIGHT_CENTER,
		TOP_LEFT, TOP_CENTER, TOP_RIGHT,
		BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;
		
		private Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		public Point getPoint(Dimension size) {
			Point point = new Point();
			switch(this) {
				case CENTER:
					point.setLocation(ScreenSize.width / 2 - size.width / 2, ScreenSize.height / 2 - size.height / 2);
					break;
				case LEFT:
					point.setLocation(0, 0);
					break;
				case TOP:
					point.setLocation(0, 0);
					break;
				case RIGHT:
					point.setLocation(ScreenSize.width - size.getWidth(), 0);
					break;
				case BOTTOM:
					point.setLocation(0, ScreenSize.height - size.getHeight());
					break;
				case LEFT_CENTER:
					point.setLocation(0, ScreenSize.height / 2 - size.height / 2);
					break;
				case RIGHT_CENTER:
					point.setLocation(ScreenSize.width - size.getWidth(), ScreenSize.height / 2 - size.height / 2);
					break;
				case TOP_LEFT:
					point.setLocation(0, 0);
					break;
				case TOP_CENTER:
					point.setLocation(ScreenSize.width / 2 - size.width / 2, 0);
					break;
				case TOP_RIGHT:
					point.setLocation(ScreenSize.width - size.width, 0);
					break;
				case BOTTOM_LEFT:
					point.setLocation(0, ScreenSize.height - size.getHeight());
					break;
				case BOTTOM_CENTER:
					point.setLocation(ScreenSize.width / 2 - size.width / 2, ScreenSize.height - size.getHeight());
					break;
				case BOTTOM_RIGHT:
					point.setLocation(ScreenSize.width - size.getWidth(), ScreenSize.height - size.getHeight());
					break;
			}
			return point;
		}
	}

	
//	public void addWindowListener(WindowAdapter event) {
//		this.eventList.add(event);
//		super.addWindowListener(event);
//	}
//	public void addComponentListener(ComponentListener event) {
//		this.eventList.add(event);
//		super.addComponentListener(event);
//	}
//	public void addContainerListener(ContainerListener event) {
//		this.eventList.add(event);
//		super.addContainerListener(event);
//	}
//	public void addFocusListener(FocusListener event) {
//		this.eventList.add(event);
//		super.addFocusListener(event);
//	}
//	public void addHierarchyBoundsListener(HierarchyBoundsListener event) {
//		this.eventList.add(event);
//		super.addHierarchyBoundsListener(event);
//	}
//	public void addHierarchyListener(HierarchyListener event) {
//		this.eventList.add(event);
//		super.addHierarchyListener(event);
//	}
//	public void addInputMethodListener(InputMethodListener event) {
//		this.eventList.add(event);
//		super.addInputMethodListener(event);
//	}
//	public void addKeyListener(KeyListener event) {
//		this.eventList.add(event);
//		super.addKeyListener(event);
//	}
//	public void addMouseListener(MouseListener event) {
//		this.eventList.add(event);
//		super.addMouseListener(event);
//	}
//	public void addMouseMotionListener(MouseMotionListener event) {
//		this.eventList.add(event);
//		super.addMouseMotionListener(event);
//	}
//	public void addMouseWheelListener(MouseWheelListener event) {
//		this.eventList.add(event);
//		super.addMouseWheelListener(event);
//	}
//	public void addPropertyChangeListener(PropertyChangeListener event) {
//		this.eventList.add(event);
//		super.addPropertyChangeListener(event);
//	}
//	public void addPropertyChangeListener(String propertyName, PropertyChangeListener event) {
//		this.eventList.add(event);
//		super.addPropertyChangeListener(propertyName, event);
//	}
//	public void addWindowFocusListener(WindowFocusListener event) {
//		this.eventList.add(event);
//		super.addWindowFocusListener(event);
//	}
//	public void addWindowStateListener(WindowStateListener event) {
//		this.eventList.add(event);
//		super.addWindowStateListener(event);
//	}
}
