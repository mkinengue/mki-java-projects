package fr.mkinengue.sudoku.ui.tutorial;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FenetreTexte extends JFrame {

	public FenetreTexte() {
		super();

		build();// On initialise notre fenêtre
	}

	private void build() {
		setTitle("Fenêtre qui affiche du texte"); // On donne un titre à l'application
		setSize(320, 240); // On donne une taille à notre fenêtre
		setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
		setResizable(true); // On permet le redimensionnement
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // On dit à l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		final JLabel label = new JLabel("Bienvenue dans ma modeste application");
		panel.add(label);

		final JButton bouton = new JButton("Cliquez ici !");
		panel.add(bouton);

		final JButton bouton2 = new JButton("Ou là !");
		panel.add(bouton2);

		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// On crée une nouvelle instance de notre JDialog
				final FenetreTexte fenetre = new FenetreTexte();
				fenetre.setVisible(true);
			}
		});
	}
}