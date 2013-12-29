package fr.mkinengue.sudoku.methodes.impl;

import java.util.logging.Logger;

import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.RowColumnElimination;

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
