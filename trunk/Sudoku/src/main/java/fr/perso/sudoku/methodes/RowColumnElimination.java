package fr.perso.sudoku.methodes;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.perso.sudoku.bean.Case;
import fr.perso.sudoku.bean.Region;
import fr.perso.sudoku.core.Sudoku;
import fr.perso.sudoku.exception.SudokuException;

/**
 * Classe abstraite de la méthode d'élimination par ligne ou par colonne
 */
public abstract class RowColumnElimination extends MethodeAbstract implements Methode {

	/**
	 * Type d'élimination : LIGNE ou COLONNE
	 */
	public interface Type {
		static final String ROW = "RowElimination";

		static final String COLUMN = "ColumnElimination";
	}

	/**
	 * Constructeur par d�faut
	 * 
	 * @param sudoku sudoku sur lequel appliquer la méthode de résolution
	 */
	public RowColumnElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	protected abstract Logger getLog();

	protected abstract String getType();

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

			boolean caseWasFilled = false;
			for (int j = 0; j < size; j++) {
				// 3.élimination par ligne/colonne
				final boolean eliminatedByRowColumn = eliminateByRowColumn(current[j]);

				// 4.élimination par région
				final boolean eliminatedByRegion = eliminateByRegion(current[j]);

				if (!caseWasFilled) {
					caseWasFilled = caseWasFilled || eliminatedByRowColumn || eliminatedByRegion;
				}
			}

			if (caseWasFilled) {
				// Une case de la ligne ou de la colonne a été mise à jour, on met la ligne ou colonne en tête de liste
				priorityList.remove(i);
				priorityList.addFirst(i);
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
	 * @return
	 */
	private boolean eliminateByRowColumn(final Case currCase) {
		Case[] otherCurrRowCol;

		// 3.on extrait la colonne ou la ligne correspondant à la case courante
		// de la façon suivante :
		// si currCase est un extrait de ligne, on extrait la colonne à laquelle appartient la case;
		// sinon, on extrait la ligne à laquelle appartient la case
		if (isRowType()) {
			otherCurrRowCol = getSudoku().getCasesByColumn(currCase.getColumn());
		} else {
			otherCurrRowCol = getSudoku().getCasesByRow(currCase.getRow());
		}

		// 4.si la case est vide, on vérifie s'il ne reste plus qu'un candidat
		boolean caseIsFilled = false;
		if (currCase.isEmpty()) {
			if (currCase.getCandidates().size() == 0) {
				throw new SudokuException("La case " + currCase
						+ " est vide mais, sa liste de candidats est également vide alors "
						+ "qu'elle devrait contenir des candidats");
			} else if (currCase.getCandidates().size() > 1) {
				// 4.2.on essaie de réduire le nombre de candidats de la case courante en fonction du contenu de l'autre
				// ligne ou colonne extraite
				reduceCandidate(currCase, otherCurrRowCol);
			}

			if (currCase.getCandidates().size() == 1) {
				// 4.1.on set la valeur de la case à la valeur du seul candidat
				currCase.setValue(currCase.getCandidates().get(0));

				// On supprime tous les candidats de la case
				currCase.getCandidates().clear();

				// On remplit la map des occurrences des nombres
				getSudoku().updateMapOccurrencesByNumber(currCase.getValue());

				// On supprime la case de la liste des cases vides
				getSudoku().getEmptyCases().remove(currCase);
				caseIsFilled = true;
			}
		}
		return caseIsFilled;
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
				// Le candidat est contenu dans la case, on le supprime
				c.removeCandidate(currVal);
			}
		}
	}

	/**
	 * Elimination de candidats par région.<br />
	 * Les candidats de la case currCase sont réduits en fonction des valeurs contenues dans la région le contenant.<br />
	 * Si la case n'a plus de candidats, une exception levée. Si la case n'a plus qu'un candidat, celui-ci est valorisé
	 * dans la case et la map des occurrences des nombres de la grille est mise à jour en même temps que la liste des
	 * index de priorité de la région
	 * 
	 * @param Case currCase
	 * @return boolean : true / false
	 */
	private boolean eliminateByRegion(final Case currCase) {
		final Region region = getSudoku().getRegionByCase(currCase);
		boolean caseIsFilled = false;
		if (currCase.isEmpty()) {
			if (currCase.getCandidates().size() == 0) {
				throw new SudokuException(getClass().getSimpleName() + ".eliminateByRegion:la case " + currCase
						+ " est vide mais n'a plus de candidats");
			} else if (currCase.getCandidates().size() > 1) {
				for (final Case c : region.getCases()) {
					if (c.isEmpty() || c.equals(currCase)) {
						continue;
					} else if (currCase.getCandidates().contains(c.getValue())) {
						currCase.removeCandidate(c.getValue());
					}
				}
			}

			if (currCase.getCandidates().size() == 1) {
				currCase.setValue(currCase.getCandidates().get(0).intValue());

				// On vide la liste des candidats de la case juste remplie
				currCase.getCandidates().clear();

				// On remplit la map des occurrences des nombres
				getSudoku().updateMapOccurrencesByNumber(currCase.getValue());

				// On supprime la case de la liste des cases vides
				getSudoku().getEmptyCases().remove(currCase);

				caseIsFilled = true;
			}
		}

		return caseIsFilled;

		// TODO Elimination normalement effectuée par une autre méthode (SingletonNu)
		// if (!currCase.isEmpty()) {
		// // Si la case n'est pas vide, on supprime de la liste des candidats des autres cases de la région sa valeur
		// for (final Case c : region.getCases()) {
		// if (c.equals(currCase)) {
		// continue;
		// } else if (c.isEmpty() && c.getCandidates().contains(currCase.getValue())) {
		// c.removeCandidate(currCase.getValue());
		// }
		// }
		// }
	}
}
