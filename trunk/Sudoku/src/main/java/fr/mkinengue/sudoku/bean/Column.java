package fr.mkinengue.sudoku.bean;

/**
 * @author mkinengue<br />
 *         Implémentation d'une colonne
 */
public class Column extends RowColumnAbstract {

	/**
	 * {@inheritDoc}
	 */
	public Column(final Case[] columnParam, final int indexParam) {
		super(columnParam, indexParam);
	}

	/**
	 * {@inheritDoc}
	 */
	public Column(final int size, final int indexParam) {
		super(size, indexParam);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isRowType() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isColumnType() {
		return true;
	}

	/**
	 * Retourne la colonne sous forme de tableau
	 * 
	 * @return Case[] tableau des cases
	 */
	public Case[] getColumn() {
		return getRowColumn();
	}
}
