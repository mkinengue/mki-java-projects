package fr.perso.sudoku.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.perso.sudoku.exception.NotValidException;

/**
 * Implémentation d'une région de la grille de SUdoku. Une région est un mini carré constitué d'autant de cases qu'une
 * ligne de la grille. De plus, les index de ligne et colonne des cases de la région sont liés par une formule
 */
public class Region {

	private final Case[] region;

	private final int size;

	private boolean completed;

	private boolean validated;

	private final Case firstCase;

	private Map<Integer, List<Case>> caseByRow;

	private Map<Integer, List<Case>> caseByColumn;

	/**
	 * Constraucteur
	 * 
	 * @param size
	 * @param firstCase
	 */
	public Region(final int size, final Case firstCase) {
		this.size = size;
		region = new Case[size];
		completed = false;
		validated = false;
		caseByRow = null;
		caseByColumn = null;
		this.firstCase = firstCase;
	}

	/**
	 * @return
	 */
	public final int getSize() {
		return size;
	}

	/**
	 * 
	 * @return
	 */
	public final Case[] getCases() {
		return region;
	}

	/**
	 * Ajoute la case à la région
	 * 
	 * @param c
	 * @return
	 */
	public boolean addCase(final Case c) {
		for (int i = 0; i < size; i++) {
			if (region[i] == null) {
				region[i] = c;
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si la région est complète ou non
	 * 
	 * @return
	 */
	public boolean isComplete() {
		if (!completed) {
			boolean tmp = true;
			for (int i = 0; i < size; i++) {
				tmp = tmp && (region[i] != null);
			}
			completed = tmp;
		}
		return completed;
	}

	/**
	 * Vérifie si la région est valide ou non<br />
	 * Lève une exception quand ce n'est pas le cas
	 * 
	 * @return
	 */
	public boolean isValid() {
		if (isComplete() && !validated) {
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					final int rowI = region[i].getRow();
					final int colI = region[i].getColumn();
					final int rowJ = region[j].getRow();
					final int colJ = region[j].getColumn();
					if (Math.pow(rowI - rowJ, 2) >= size) {
						throw new NotValidException("La différence des lignes des cases " + region[i] + " et "
								+ region[j] + " est supérieure à " + size);
					}
					if (Math.abs(colI - colJ) >= size) {
						throw new NotValidException("La différence des lignes des cases " + region[i] + " et "
								+ region[j] + " est supérieure à " + size);
					}
				}
			}
			validated = true;
		}
		return validated;
	}

	private void initCasesByRow() {
		if (caseByRow == null) {
			// if (!completed) {
			// throw new NotCompletedException("La région courante n'est pas complète");
			// } else if (!validated) {
			// throw new NotValidException("La région courante n'est pas valide");
			// } else {
			caseByRow = new HashMap<Integer, List<Case>>();
			for (int i = 0; i < size; i++) {
				if (caseByRow.get(region[i].getRow()) == null) {
					caseByRow.put(region[i].getRow(), new ArrayList<Case>());
				}
				caseByRow.get(region[i].getRow()).add(region[i]);
			}
			// }
		}
	}

	private void initCasesByColumn() {
		if (caseByColumn == null) {
			// if (!completed) {
			// throw new NotCompletedException("La région courante n'est pas complète");
			// } else if (!validated) {
			// throw new NotValidException("La région courante n'est pas valide");
			// } else {
			caseByColumn = new HashMap<Integer, List<Case>>();
			for (int i = 0; i < size; i++) {
				System.out.println(region[i]);
				if (caseByColumn.get(region[i].getColumn()) == null) {
					caseByColumn.put(region[i].getColumn(), new ArrayList<Case>());
				}
				caseByColumn.get(region[i].getColumn()).add(region[i]);
			}
			// }
		}
	}

	public List<Case> getCasesByRow(final int row) {
		if (caseByRow == null) {
			initCasesByRow();
		}
		return caseByRow.get(row);
	}

	public List<Case> getCasesByColumn(final int column) {
		if (caseByColumn == null) {
			initCasesByColumn();
		}
		return caseByColumn.get(column);
	}

	public boolean contains(final Case c) {
		final List<Case> listCases = getCasesByRow(c.getRow());
		return listCases.contains(c);
	}

	public List<Integer> getRows() {
		initCasesByRow();
		return new ArrayList<Integer>(caseByRow.keySet());
	}

	public List<Integer> getColumns() {
		initCasesByColumn();
		return new ArrayList<Integer>(caseByColumn.keySet());
	}

	public Case getFirstCase() {
		return firstCase;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder toPrint = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (toPrint.length() != 0) {
				toPrint.append(" - ");
			}
			toPrint.append(region[i]);
		}
		return toPrint.toString();
	}
}
