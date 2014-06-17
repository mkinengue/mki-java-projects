package fr.mkinengue.sudoku.ui.tutorial;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TestSwing extends JFrame {

	/** Serial UUID */
	private static final long serialVersionUID = -2680650387998196018L;

	public TestSwing() {

		final JPanel panel = new JPanel();

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panel.setLayout(new GridLayout(2, 2, 50, 50));

		final JLabel l1 = new JLabel("nothing");
		final JTextField jtF = new JTextField("nothinggg", 3);
		jtF.setBorder(BorderFactory.createTitledBorder("JText Field"));
		// jtF.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		final JButton b1 = new JButton("Start");
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				l1.setText("Button has been clicked");
			}

		});

		panel.add(b1);
		panel.add(jtF);
		panel.add(new JButton("Start"));

		add(panel);

		setTitle("Swing Example");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final TestSwing ex = new TestSwing();
				ex.setVisible(true);
			}
		});
	}

}