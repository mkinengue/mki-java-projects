package fr.mkinengue.sudoku.methodes.impl;

import java.util.logging.Logger;

import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.RowColumnElimination;

/**
 * M�thode consistant � r�duire les candidats de chacune des colonnes de la grille de Sudoku
 */
public class ColumnElimination extends RowColumnElimination implements Methode {

	private static Logger LOG = Logger.getLogger(ColumnElimination.class.getSimpleName());

	public ColumnElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getType() {
		return RowColumnElimination.Type.COLUMN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Logger getLog() {
		return LOG;
	}

}