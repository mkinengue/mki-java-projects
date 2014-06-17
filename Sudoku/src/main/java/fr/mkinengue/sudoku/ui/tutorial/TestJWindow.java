package fr.mkinengue.sudoku.ui.tutorial;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class TestJWindow {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// On crée une nouvelle instance de notre JWindow
				final JWindow window = new JWindow();
				window.setSize(300, 200);// On lui donne une taille pour qu'on puisse la voir
				window.setVisible(true);// On la rend visible
			}
		});

		try {
			Thread.sleep(5000);
		} catch (final InterruptedException e) {
		}

		System.exit(0);
	}
}
