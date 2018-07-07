package Test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HideMode extends JPanel {

    private class RadioActionListener implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            final boolean showA = aRadio.isSelected();
            field.setVisible(showA);
            combo.setVisible(!showA);
        }
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    final JDialog dialog = new JDialog();

                    final Container contentPane = dialog.getContentPane();
                    contentPane.setLayout(new BorderLayout());
                    contentPane.add(new HideMode(), BorderLayout.CENTER);

                    dialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(final WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final JTextField field;
    private final JRadioButton aRadio;
    private final JRadioButton bRadio;

    private final JComboBox<String> combo;

    public HideMode() {
        setLayout(new MigLayout("", "[]", "[][]"));

        final RadioActionListener radioListener = new RadioActionListener();

        aRadio = new JRadioButton("A");
        aRadio.addActionListener(radioListener);
        add(aRadio, "flowx,cell 0 0");

        bRadio = new JRadioButton("B");
        bRadio.addActionListener(radioListener);
        add(bRadio, "cell 0 0");

        field = new JTextField();
        add(field, "cell 0 1,hidemode 3");
        field.setColumns(20);

        combo = new JComboBox<String>();
        combo.setModel(new DefaultComboBoxModel<String>(new String[] {
            "hello",
            "world" }));
        add(combo, "cell 0 1,hidemode 3");

        final ButtonGroup bg = new ButtonGroup();
        bg.add(aRadio);
        bg.add(bRadio);
        aRadio.doClick();
    }
}
