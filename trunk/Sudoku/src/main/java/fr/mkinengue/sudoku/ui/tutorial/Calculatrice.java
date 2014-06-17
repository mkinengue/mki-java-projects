package fr.mkinengue.sudoku.ui.tutorial;

import javax.swing.SwingUtilities;

public class Calculatrice {
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
