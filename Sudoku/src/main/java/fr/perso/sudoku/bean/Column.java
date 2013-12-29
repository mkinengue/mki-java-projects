/**
 * 
 */
package fr.perso.sudoku.bean;

/**
 * @author mkinengue<br />
 *         Implémentation d'une colonne
 */
public class Column extends RowColumnAbstract {

	private final boolean isColumn;

	/**
	 * {@inheritDoc}
	 */
	public Column(final Case[] columnParam, final int indexParam) {
		super(columnParam, indexParam);
		isColumn = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public Column(final int size, final int indexParam) {
		super(size, indexParam);
		isColumn = true;
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
		return isColumn;
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
