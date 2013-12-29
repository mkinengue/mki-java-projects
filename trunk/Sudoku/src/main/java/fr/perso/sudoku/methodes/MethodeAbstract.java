package fr.perso.sudoku.methodes;

import fr.perso.sudoku.core.Sudoku;

/**
 * Classe abstraite pour les méthodes de résolution de la grille de Sudoku
 */
public abstract class MethodeAbstract {

	private final Sudoku sudoku;

	/**
	 * Constructeur
	 * 
	 * @param sudoku
	 */
	public MethodeAbstract(final Sudoku sudoku) {
		this.sudoku = sudoku;
	}

	/**
	 * @return
	 */
	protected Sudoku getSudoku() {
		return sudoku;
	}
}
