package fr.mkinengue.sudoku.ui.tutorial;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CalculatriceFenetre extends JFrame {
	private JTextField field1;
	private JTextField field2;

	public CalculatriceFenetre() {
		super();

		build();// On initialise notre fenêtre
	}

	private void build() {
		setTitle("Calculatrice"); // On donne un titre à l'application
		setSize(400, 200); // On donne une taille à notre fenêtre
		setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
		setResizable(true); // On interdit la redimensionnement de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // On dit à l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.white);

		field1 = new JTextField();
		field1.setColumns(10);

		panel.add(field1);

		field2 = new JTextField();
		field2.setColumns(10);

		panel.add(field2);

		final JButton bouton = new JButton(new CalculAction(this, "Calculer"));
		panel.add(bouton);

		final JLabel label = new JLabel("Résultat : Pas encore calculé");
		panel.add(label);
		return panel;
	}

	public JTextField getField1() {
		return field1;
	}

	public JTextField getField2() {
		return field2;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// On crée une nouvelle instance de notre JDialog
				final CalculatriceFenetre calculatrice = new CalculatriceFenetre();
				calculatrice.setVisible(true);
			}
		});
	}
}
