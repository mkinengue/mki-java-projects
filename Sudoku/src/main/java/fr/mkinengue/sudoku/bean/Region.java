package fr.mkinengue.sudoku.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mkinengue.sudoku.exception.NotValidException;
import fr.mkinengue.sudoku.exception.SudokuException;

/**
 * Implémentation d'une région de la grille de Sudoku. Une région est un mini carré constitué d'autant de cases qu'une
 * ligne de la grille. De plus, les index de ligne et colonne des cases de la région sont liés par une formule
 */
public class Region {

	private final List<Case> region;

	private final int size;

	private boolean completed;

	private boolean validated;

	private final Case firstCase;

	private Map<Integer, List<Case>> caseByRow;

	private Map<Integer, List<Case>> caseByColumn;

	/**
	 * Constructeur initialisant une région avec la taille donnée par le premier paramètre et comme première case de la
	 * région celle donnée par le second paramètre
	 * 
	 * @param size
	 * @param firstCase
	 */
	public Region(final int size, final Case firstCase) {
		this.size = size;
		region = new ArrayList<Case>(size);
		completed = false;
		validated = false;
		caseByRow = null;
		caseByColumn = null;
		this.firstCase = firstCase;
	}

	/**
	 * Retourne la taille de la région, le nombre de cases contenus dans la région
	 * 
	 * @return int
	 */
	public final int getSize() {
		return size;
	}

	/**
	 * Retourne la liste des cases formant la région
	 * 
	 * @return Case[]
	 */
	public final Case[] getCases() {
		return (Case[]) region.toArray();
	}

	/**
	 * Ajoute la case
	 * 
	 * @param c
	 */
	public void addCase(Case c) {
		if (!contains(c)) {
			region.add(c);
		}
	}

	/**
	 * Vérifie si la région est complète ou non
	 * 
	 * @return true / false
	 */
	public boolean isComplete() {
		if (!completed) {
			completed = region.size() == size;
		}
		return completed;
	}

	/**
	 * Vérifie si la région est valide ou non en faisant la différence ligne à ligne et colonne à colonne de toutes les
	 * cases deux à deux en s'assurant que le carré du résultat est toujours strictement inférieure au nombre de cases
	 * contenues dans la région<br />
	 * Lève une exception quand ce n'est pas le cas
	 * 
	 * @return true / false
	 */
	public boolean isValid() {
		if (isComplete() && !validated) {
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					final int rowI = region.get(i).getRow();
					final int colI = region.get(i).getColumn();
					final int rowJ = region.get(j).getRow();
					final int colJ = region.get(j).getColumn();
					if (Math.pow(rowI - rowJ, 2) >= size) {
						throw new NotValidException("Le carré de la différence des lignes des cases " + region.get(i)
										+ " et " + region.get(j) + " est supérieure à " + size);
					}
					if (Math.pow(colI - colJ, 2) >= size) {
						throw new NotValidException("Le carré de la différence des lignes des cases " + region.get(i)
										+ " et " + region.get(j) + " est supérieure à " + size);
					}
				}
			}
			validated = true;
		}
		return validated;
	}

	/**
	 * Met à jour la Map des cases de la région par ligne
	 */
	private void updateCaseByRow() {
		if (caseByRow == null) {
			caseByRow = new HashMap<Integer, List<Case>>();
		}
		for (final Case c : region) {
			if (caseByRow.get(c.getRow()) == null) {
				caseByRow.put(c.getRow(), new ArrayList<Case>());
			}
			if (!caseByRow.get(c.getRow()).contains(c)) {
				caseByRow.get(c.getRow()).add(c);
			}
		}
	}

	/**
	 * Met à jour la Map des cases de la région par colonne
	 */
	private void updateCasesByColumn() {
		if (caseByColumn == null) {
			caseByColumn = new HashMap<Integer, List<Case>>();
		}
		for (final Case c : region) {
			if (caseByColumn.get(c.getColumn()) == null) {
				caseByColumn.put(c.getColumn(), new ArrayList<Case>());
			}
			if (!caseByColumn.get(c.getColumn()).contains(c)) {
				caseByColumn.get(c.getColumn()).add(c);
			}
		}
	}

	/**
	 * Retourne la liste des cases appartenant à la ligne row de la région courante<br />
	 * Retourne null si la ligne n'existe pas dans la région courante
	 * 
	 * @param row
	 * @return List&lt;Case&gt;
	 */
	public List<Case> getCasesByRow(final int row) {
		updateCaseByRow();
		return caseByRow.get(row);
	}

	/**
	 * Retourne la liste des cases appartenant à la colonne column de la région courante<br />
	 * Retourne null si la colonne n'existe pas dans la région courante
	 * 
	 * @param row
	 * @return List&lt;Case&gt;
	 */
	public List<Case> getCasesByColumn(final int column) {
		updateCasesByColumn();
		return caseByColumn.get(column);
	}

	/**
	 * Retourne true si la région courante contient la case en paramètre
	 * 
	 * @param c
	 * @return true / false
	 */
	public boolean contains(final Case c) {
		if (c == null) {
			throw new SudokuException("La case fournie en paramètre est nulle");
		}
		final List<Case> listCases = getCasesByRow(c.getRow());
		return listCases != null && listCases.contains(c);
	}

	/**
	 * Retourne la liste des index de ligne contenant la région
	 * 
	 * @return List&lt;Integer&gt;
	 */
	public List<Integer> getRows() {
		updateCaseByRow();
		return new ArrayList<Integer>(caseByRow.keySet());
	}

	/**
	 * Retourne la liste des index de colonne contenant la région
	 * 
	 * @return List&lt;Integer&gt;
	 */
	public List<Integer> getColumns() {
		updateCasesByColumn();
		return new ArrayList<Integer>(caseByColumn.keySet());
	}

	/**
	 * Retourne la première case de la région
	 * 
	 * @return Case
	 */
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
			toPrint.append(region.get(i));
		}
		return toPrint.toString();
	}
}
