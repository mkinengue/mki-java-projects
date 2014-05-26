package fr.mkinengue.sudoku.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation d'une case d'une grille de Sudoku contenant les index de ligne et colonne de la case dans la grille,
 * le nombre maximum que peut contenir une case, la valeur de la case et la liste des candidats -valeus possibles de la
 * case lorsque celle-ci est encore vide
 */
public class Case implements Comparable {

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

	/**
	 * Retourne le numéro de la ligne à laquelle appartient la case
	 * 
	 * @return int
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Retourne le numéro de la colonne à laquelle appartient la case
	 * 
	 * @return int
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Retourne le nombre maximum que peut contenir la case du Sudoku courant
	 * 
	 * @return int
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Retourne le nombre contenu dans la case. Retourne null si la case est vide
	 * 
	 * @return Integer ou Null
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * Valorise la case avec le nombre "value" en paramètre si celui-ci est un nombre dans la plage attendue.<br />
	 * En cas de valorisation, la liste des candidats possibles pour la case courante est vidée
	 * 
	 * @param value
	 */
	public void setValue(final int value) {
		if (value > 0 && value <= maxValue) {
			this.value = value;
			candidates.clear();
		}
	}

	public boolean isEmpty() {
		return value == null || value.intValue() == 0;
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
		return candidates.remove(Integer.valueOf(candidate));
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
		return co.row == row && co.column == column;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + row;
		hash = 29 * hash + column;
		return hash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("r:").append(row).append(",c:").append(column).append(",v:").append(value)
						.toString();
	}

	public static void main(final String[] args) {
		System.out.println(Math.round(1.0 / 3));
		System.out.println(Math.round(4.0 / 3));
		System.out.println(Math.floor(8.0 / 3));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("La case à comparer ne peut être nulle");
		} else if (!(o instanceof Case)) {
			throw new IllegalArgumentException("La case à comparer n'en est pas une");
		}

		final Case c = (Case) o;
		if (row < c.row) {
			return -1;
		} else if (row == c.row) {
			if (column < c.column) {
				return -1;
			} else if (column == c.column) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}
}
