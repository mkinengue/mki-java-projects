package fr.perso.sudoku.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation d'une case d'une grille de Sudoku contenant les index de ligne et colonne de la case dans la grille,
 * le nombre maximum que peut contenir une case, la valeur de la case et la liste des candidats -valeus possibles de la
 * case lorsque celle-ci est encore vide
 */
public class Case {

	private final int row;

	private final int column;

	private Integer value;

	private final int maxValue;

	private final List<Integer> candidates;

	/**
	 * Constructeur
	 * 
	 * @param row
	 * @param column
	 * @param value
	 * @param maxValue
	 */
	public Case(final int row, final int column, final Integer value, final int maxValue) {
		this.row = row;
		this.column = column;
		this.value = value;
		this.maxValue = maxValue;
		candidates = new ArrayList<Integer>();
		if (value == null) {
			for (int i = 1; i <= maxValue; i++) {
				candidates.add(i);
			}
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = Integer.valueOf(value);
		if (value > 0 && value <= maxValue) {
			candidates.clear();
		}
	}

	public boolean isEmpty() {
		return (value == null || value.intValue() == 0);
	}

	public List<Integer> getCandidates() {
		return candidates;
	}

	/**
	 * Supprime le candidat candidate de la liste des possibles s'il s'y trouve.<br />
	 * Retourne true s'il y a eu une suppression et false autrement
	 * 
	 * @param candidate
	 * @return true / false
	 */
	public boolean removeCandidate(final Integer candidate) {
		return candidates.remove(candidate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Case)) {
			return false;
		}
		final Case co = (Case) o;
		return (co.row == this.row) && (co.column == this.column);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.row;
		hash = 31 * hash + this.column;
		return hash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (new StringBuilder()).append("r:").append(row).append(",c:").append(column).append(",v:").append(value)
				.toString();
	}

	public static void main(final String[] args) {
		System.out.println(Math.round(1.0 / 3));
		System.out.println(Math.round(4.0 / 3));
		System.out.println(Math.floor(8.0 / 3));
	}
}
