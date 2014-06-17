package fr.mkinengue.sudoku.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Panel utilisé pour dessiner la grille de Sudoku
 */
public class SudokuPanel {

	private final JPanel panel;

	public SudokuPanel() {
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(2, 0, 50, 50));
	}
}
