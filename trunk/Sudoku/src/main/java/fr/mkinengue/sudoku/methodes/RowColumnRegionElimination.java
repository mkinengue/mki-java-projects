package fr.mkinengue.sudoku.methodes;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.exception.SudokuException;

/**
 * Classe abstraite de la méthode d'élimination par ligne ou par colonne
 */
public abstract class RowColumnRegionElimination extends MethodeAbstract implements Methode {

	/**
	 * Type d'élimination : LIGNE ou COLONNE
	 */
	public interface Type {
		static final String ROW = "RowElimination";

		static final String COLUMN = "ColumnElimination";
	}

	/**
	 * Constructeur par défaut
	 * 
	 * @param sudoku sudoku sur lequel appliquer la méthode de résolution
	 */
	public RowColumnRegionElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	protected abstract Logger getLog();

	protected abstract String getType();

	/**
	 * Retourne true si la méthode d'élimination est de type ligne et false si de type colonne
	 * 
	 * @return true / false
	 */
	protected boolean isRowType() {
		if (Type.ROW.equals(getType())) {
			return true;
		} else if (Type.COLUMN.equals(getType())) {
			return false;
		} else {
			throw new SudokuException("Le type d'élimination par ligne/colonne " + getType() + " est inconnu");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		final long debut = System.currentTimeMillis();
		final Case[][] grille = getSudoku().getGrille();
		final int size = grille[0].length;
		Case[] current;

		// Recherche de l'ordre de recherche de ligne ou colonne
		LinkedList<Integer> priorityList = null;
		LinkedList<Integer> searchIdxList = null;

		if (isRowType()) {
			priorityList = getSudoku().getPriorityRows();
		} else {
			priorityList = getSudoku().getPriorityColumns();
		}

		// Copy the priority list
		searchIdxList = (LinkedList<Integer>) priorityList.clone();

		for (final Integer i : searchIdxList) {
			if (isRowType()) {
				current = getSudoku().getCasesByRow(i);
			} else {
				current = getSudoku().getCasesByColumn(i);
			}

			for (int j = 0; j < size; j++) {
				// 3.élimination par ligne/colonne
				eliminateByRowColumn(current[j]);

				// 4.élimination par région
				eliminateByRegion(current[j]);
			}
		}

		getLog().log(Level.INFO, "Méthode {0} exécutée en {1} ms",
						new Object[] { getType(), String.valueOf(System.currentTimeMillis() - debut) });
	}

	/**
	 * Elimination de candidats par ligne ou colonne.<br />
	 * Les candidats de la case currCase sont réduits en fonction des valeurs contenues dans la ligne ou colonne le
	 * contenant.<br />
	 * Si la case n'a plus de candidats, une exception levée. Si la case n'a plus qu'un candidat, celui-ci est valorisé
	 * dans la case et la map des occurrences des nombres de la grille est mise à jour en même temps que la liste des
	 * index de priorité de la ligne ou de la colonne
	 * 
	 * @param currCase
	 */
	private void eliminateByRowColumn(final Case currCase) {
		Case[] otherCurrRowCol;

		// 3.on extrait la colonne ou la ligne correspondant à la case courante
		// de la façon suivante :
		// si on est en mode élimination par ligne, on extrait la colonne à laquelle appartient la case;
		// sinon (on est en mode élimination par colonne), on extrait la ligne à laquelle appartient la case
		if (isRowType()) {
			otherCurrRowCol = getSudoku().getCasesByColumn(currCase.getColumn());
		} else {
			otherCurrRowCol = getSudoku().getCasesByRow(currCase.getRow());
		}

		// 4.si la case est vide, on vérifie s'il ne reste plus qu'un candidat
		if (currCase.isEmpty()) {
			if (currCase.getCandidates().size() == 0) {
				throw new SudokuException("La case " + currCase
								+ " est vide et, sa liste de candidats est également vide alors "
								+ "qu'elle devrait contenir des candidats");
			} else if (currCase.getCandidates().size() > 1) {
				// 4.2.on essaie de réduire la liste des candidats de la case courante en fonction du contenu de l'autre
				// ligne ou colonne extraite
				reduceCandidate(currCase, otherCurrRowCol);
			}

			getSudoku().updateCaseWithOneCandidate(currCase);
		}
	}

	/**
	 * Réduit la liste des candidats de la case c grâce à la liste des case other
	 * 
	 * @param c
	 * @param other
	 */
	private void reduceCandidate(final Case c, final Case[] other) {
		final int size = other.length;
		for (int i = 0; i < size; i++) {
			if (other[i].isEmpty() || other[i].equals(c)) {
				// Si la case c est égale à la case courante ou si la case courante est vide, on passe à la suivante
				continue;
			}
			final Integer currVal = other[i].getValue();
			if (c.getCandidates().contains(currVal)) {
				// La valeur de la case en cours d'analyse est un candidat de la case c, on le supprime
				c.removeCandidate(currVal);
			}
		}
	}

	/**
	 * Elimination de candidats par région.<br />
	 * Les candidats de la case currCase sont réduits en fonction des valeurs contenues dans la région la contenant.<br />
	 * Si la case n'a plus de candidats, une exception levée. Si la case n'a plus qu'un candidat, celui-ci est valorisé
	 * dans la case et la map des occurrences des nombres de la grille est mise à jour en même temps que la liste des
	 * index de priorité de la région
	 * 
	 * @param Case currCase
	 */
	private void eliminateByRegion(final Case currCase) {
		final Region region = getSudoku().getRegionByCase(currCase);
		if (currCase.isEmpty()) {
			if (currCase.getCandidates().size() == 0) {
				throw new SudokuException(getClass().getSimpleName() + ".eliminateByRegion:la case " + currCase
								+ " est vide mais n'a plus de candidats possibles");
			}

			getSudoku().updateCaseWithOneCandidate(currCase);
			if (currCase.getCandidates().size() > 1) {
				for (final Case c : region.getCases()) {
					if (c.isEmpty() || c.equals(currCase)) {
						continue;
					} else if (currCase.getCandidates().contains(c.getValue())) {
						currCase.removeCandidate(c.getValue());
						getSudoku().updateCaseWithOneCandidate(currCase);
					}
				}
			}
		}
	}
}
