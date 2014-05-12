/**
 * 
 */
package fr.mkinengue.sudoku.bean;

/**
 * @author mkinengue<br />
 *         Implémentation d'une ligne
 */
public class Row extends RowColumnAbstract {

	/**
	 * {@inheritDoc}
	 */
	public Row(final Case[] rowColumnParam, final int indexParam) {
		super(rowColumnParam, indexParam);
	}

	/**
	 * {@inheritDoc}
	 */
	public Row(final int size, final int indexParam) {
		super(size, indexParam);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isRowType() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isColumnType() {
		return false;
	}

	/**
	 * Retourne la ligne sous forme de tableau
	 * 
	 * @return Case[] tableau des cases
	 */
	public Case[] getRow() {
		return getRowColumn();
	}
}
