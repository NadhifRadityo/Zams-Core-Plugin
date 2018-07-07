package io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class Button extends JButton{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6218119154712423816L;
	
	private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    private Color originalColor;
    private boolean init = false;

    public Button() {
        this(null);
    }

    public Button(String text) {
        super(text);
        init = true;
        super.setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
        	if(pressedBackgroundColor != null) {
	            g.setColor(pressedBackgroundColor);
	            this.setBackground(pressedBackgroundColor);
        	}
        } else if (getModel().isRollover()) {
        	if(hoverBackgroundColor != null) {
	            g.setColor(hoverBackgroundColor);
	            this.setBackground(hoverBackgroundColor);
        	}
        } else {
            g.setColor(originalColor);
            this.setBackground(originalColor);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

//    @Override
//    public void setContentAreaFilled(boolean b) {
//    }
    
    public void setBackground(Color bg) {
    	if(originalColor == null && init == true) {
    		originalColor = bg;
    	}
//    	new Exception().printStackTrace();
    	super.setBackground(bg);
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
