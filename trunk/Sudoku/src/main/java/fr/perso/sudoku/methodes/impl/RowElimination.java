package fr.perso.sudoku.methodes.impl;

import java.util.logging.Logger;

import fr.perso.sudoku.core.Sudoku;
import fr.perso.sudoku.methodes.Methode;
import fr.perso.sudoku.methodes.RowColumnElimination;

/**
 * Méthode consistant à réduire les candidats de chacune des lignes de la grille de Sudoku
 */
public class RowElimination extends RowColumnElimination implements Methode {

	private static Logger LOG = Logger.getLogger(RowElimination.class.getSimpleName());

	public RowElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getType() {
		return RowColumnElimination.Type.ROW;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Logger getLog() {
		return LOG;
	}

}
