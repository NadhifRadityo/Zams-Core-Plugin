package Test;

import java.awt.Color;
import java.awt.EventQueue;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ZIndex extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4365096191161443802L;
	ArrayList<JLabel> cards = new ArrayList<JLabel>();

    public ZIndex() {
        setLayout(null);
        cards.add(createCard("1", Color.RED));
        cards.add(createCard("2", Color.GREEN));
        cards.add(createCard("3", Color.BLUE));
        cards.add(createCard("4", Color.YELLOW));

        // This would be your first natural attempt (but it doesn't work)
//        for (int i = 0; i < cards.size(); i++) {
//            add(cards.get(i));
//        }

        // This affects the Z-Order to do what you want
      for (int i = cards.size() - 1; i >= 0; i--) {
          add(cards.get(i), getComponentCount() - 1);
          System.out.println(cards.get(i).getText());
          System.out.println(getComponentCount() - 1);
//          System.out.println(i);
      }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("SSCCE");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ZIndex());
        frame.setLocationByPlatform(true);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }

    public JLabel createCard(String text, Color background) {
        JLabel label = new JLabel(text);

        label.setOpaque(true);
        label.setBackground(background);
        label.setSize(30, 70);
        label.setLocation((cards.size() + 1) * 20, 20);

        return label;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
                                   public void run() {
                                       createAndShowGUI();
                                   }
                               });
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
