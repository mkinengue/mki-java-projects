package fr.mkinengue.sudoku.bean;

import fr.mkinengue.sudoku.exception.SudokuException;

/**
 * @author mkinengue Classe abstraite de définition des lignes et colonnes
 */
public abstract class RowColumnAbstract {

	private Case[] rowColumn;

	private final int index;

	/**
	 * Constructeur par défaut
	 * 
	 * @param int indexParam index de la ligne ou de la colonne
	 */
	public RowColumnAbstract(final int indexParam) {
		index = indexParam;
	}

	/**
	 * Constructeur initialisant ligne et colonne
	 * 
	 * @param Case [] rowColumnParam : tableau de cases
	 * @param indexParam index de la ligne ou de la colonne dans le tableau
	 */
	public RowColumnAbstract(final Case[] rowColumnParam, final int indexParam) {
		rowColumn = rowColumnParam;
		index = indexParam;
	}

	/**
	 * Constructeur initialisant le tableau des cases avec la taille size
	 * 
	 * @param size taille du tableau de cases
	 * @param indexParam index de la ligne ou de la colonne dans le tableau
	 */
	public RowColumnAbstract(final int size, final int indexParam) {
		rowColumn = new Case[size];
		index = indexParam;
	}

	/**
	 * Retourne l'index de la ligne ou de la colonne
	 * 
	 * @return int index de la ligne ou de la colonne
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Ajoute une case à la ligne ou la colonne en cours
	 * 
	 * @param Case c
	 */
	public void addCase(final Case c) {
		if (c == null) {
			return;
		}

		if (isRowType()) {
			rowColumn[c.getColumn()] = c;
		} else if (isColumnType()) {
			rowColumn[c.getRow()] = c;
		} else {
			throw new SudokuException("Type inconnu. Devrait être de type ligne ou colonne");
		}
	}

	// /**
	// * Remplit le tableau avec la case donnée en paramètre en position d'index de colonne ou de ligne selon qu'il
	// * s'agisse d'une ligne ou d'une colonne
	// *
	// * @param Case c
	// */
	// public void fill(final Case c) {
	// if (c == null) {
	// return;
	// }
	// fill(c.getRow(), c.getColumn(), c.getValue());
	// }
	//
	// /**
	// * Remplit la case du tableau en index row pour une ligne et column pour une colonne avec la valeur value
	// *
	// * @param row index de ligne
	// * @param column index de colonne
	// * @param value valeur
	// */
	// public void fill(final int row, final int column, final Integer value) {
	// if (isRowType()) {
	// if (row != index) {
	// throw new SudokuException("L'index de ligne " + row + " ne correspond pas à l'index de cette ligne : "
	// + index);
	// }
	// if (rowColumn[row] == null) {
	// rowColumn[row] = new Case(row, column, value, rowColumn.length);
	// } else {
	// rowColumn[row].setValue(value);
	// }
	// } else if (isColumnType()) {
	// if (column != index) {
	// throw new SudokuException("L'index de colonne " + row
	// + " ne correspond pas à l'index de cette colonne : " + index);
	// }
	// if (rowColumn[column] == null) {
	// rowColumn[column] = new Case(row, column, value, rowColumn.length);
	// } else {
	// rowColumn[column].setValue(value);
	// }
	// } else {
	// throw new SudokuException("Type inconnu. Devrait être de type ligne ou colonne");
	// }
	// }

	/**
	 * Retourne le tableau des cases des
	 * 
	 * @return Case[]
	 */
	protected Case[] getRowColumn() {
		return rowColumn;
	}

	/**
	 * Retourne true s'il s'agit d'une ligne
	 * 
	 * @return boolean
	 */
	protected abstract boolean isRowType();

	/**
	 * Retourne true s'il s'agit d'une colonne
	 * 
	 * @return boolean
	 */
	protected abstract boolean isColumnType();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof Case[])) {
			return false;
		}
		final Case[] oo = (Case[]) o;
		return this.hashCode() == oo.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		if (rowColumn == null) {
			return 0;
		}
		int hash = 0;
		for (final Case c : rowColumn) {
			if (c != null) {
				hash += 31 * c.getRow() + 37 * c.getColumn();
				if (c.getValue() != null) {
					hash += 29 * c.getValue();
				}
			}
		}
		return hash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder print = new StringBuilder();
		print.append("[");
		for (final Case c : getRowColumn()) {
			print.append("(").append(c).append(")");
		}
		print.append("]");
		return print.toString();
	}
}
