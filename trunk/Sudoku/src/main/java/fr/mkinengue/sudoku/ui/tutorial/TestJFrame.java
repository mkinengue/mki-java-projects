package fr.mkinengue.sudoku.ui.tutorial;

import javax.swing.SwingUtilities;

public class TestJFrame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// On crée une nouvelle instance de notre JDialog
				final SimpleFenetre fenetre = new SimpleFenetre();
				fenetre.setVisible(true);
			}
		});
	}
}
