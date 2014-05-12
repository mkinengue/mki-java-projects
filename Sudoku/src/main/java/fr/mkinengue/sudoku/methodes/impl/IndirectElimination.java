package fr.mkinengue.sudoku.methodes.impl;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;

/**
 * Si, sur une ligne L coupant une région B, un nombre n'apparaît pas comme candidat à l'extérieur de B, supprimer ce
 * nombre comme candidat partout dans B sauf sur L.<br />
 * Si, dans une région B coupée par une ligne L, un nombre n'apparaît pas comme candidat ailleurs que sur L, supprimer
 * ce nombre comme candidat partout sur L à l'extérieur de B.<br />
 * ... et symétriquement pour les colonnes.
 */
public class IndirectElimination extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(IndirectElimination.class.getSimpleName());

	/**
	 * Constructeur public de la classe
	 * 
	 * @param sudoku
	 */
	public IndirectElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		final long debut = System.currentTimeMillis();
		executeRowColumnCuttingRegionElimination();
		executeRegionCuttingRowColumnElimination();

		LOG.log(Level.INFO, "Méthode IndirectElimination exécutée en {0} ms", System.currentTimeMillis() - debut);
	}

	/**
	 * Méthode d'exécution de l'élimination indirecte ligne/colonne coupant une région
	 */
	private void executeRowColumnCuttingRegionElimination() {
		final int size = getSudoku().getGrille().length;
		final Collection<Region> regions = getSudoku().getRegionsByFirstCase().values();
		for (Integer candidat = 1; candidat <= size; candidat++) {
			for (final Region region : regions) {
				final List<Integer> rowsIdx = region.getRows();
				for (final Integer rowIdx : rowsIdx) {
					eliminationRowCuttingRegion(candidat, rowIdx.intValue(), region);
				}

				final List<Integer> columnsIdx = region.getColumns();
				for (final Integer columnIdx : columnsIdx) {
					eliminationColumnCuttingRegion(Integer.valueOf(candidat), columnIdx.intValue(), region);
				}
			}
		}
	}

	private void eliminationRowCuttingRegion(final Integer candidat, final int row, final Region region) {
		final Case[] casesByRow = getSudoku().getCasesByRow(row);
		eliminationRowColumnCuttingRegion(true, candidat, casesByRow, region);
	}

	private void eliminationColumnCuttingRegion(final Integer candidat, final int column, final Region region) {
		final Case[] casesByColumn = getSudoku().getCasesByColumn(column);
		eliminationRowColumnCuttingRegion(false, candidat, casesByColumn, region);
	}

	/**
	 * Elimination indirecte ligne ou colonne - région. Selon que c'est une ligne ou une colonne coupant une région et
	 * ayant un candidat pour la ligne/colonne uniquement dans les cases appartenant à la région et, pour la région,
	 * éventuellement dans d'autres cases, supprime les candidats des cases de la région, à l'exception des cases
	 * appartenant aussi à la ligne/colonne
	 * 
	 * @param isRow true si ligne, false si colonne
	 * @param candidat candidat à chercher
	 * @param cases liste des cases, ligne ou colonne
	 * @param region région
	 */
	private void eliminationRowColumnCuttingRegion(final boolean isRow, final Integer candidat, final Case[] cases,
			final Region region) {
		List<Case> caseRegionRowCol = null;
		if (isRow) {
			caseRegionRowCol = region.getCasesByRow(cases[0].getRow());
		} else {
			caseRegionRowCol = region.getCasesByColumn(cases[0].getColumn());
		}

		if (caseRegionRowCol == null) {
			return;
		}

		// On vérifie que le candidat est contenu dans l'une des cases intersection entre la ligne/colonne et la région
		// Si ce n'est pas le cas, on arrête tout car alors, le candidat est forcément sur le reste de la ligne/colonne
		boolean containsCandidate = false;
		for (final Case c : caseRegionRowCol) {
			if (c.getCandidates().contains(candidat)) {
				containsCandidate = true;
				break;
			}
		}
		if (!containsCandidate) {
			// Le candidat n'est contenu dans aucune case intersection entre la ligne/colonne et la région
			return;
		}

		// On vérifie que le candidat n'appartient pas aux cases de la ligne/colonne n'appartenant pas à la région
		for (final Case c : cases) {
			if (!caseRegionRowCol.contains(c) && c.getCandidates().contains(candidat)) {
				// Si la case de la ligne/colonne n'appartenant pas à la région contient le candidat candidat, on arrête
				// le processus
				return;
			}
		}

		// A ce stade, le candidat est contenu dans au moins l'une des cases intersection ligne/colonne et région et, le
		// candidat n'est pas contenu dans les autres cases de la ligne/colonne. On supprime le candidat des autres
		// cases de la région
		for (final Case c : region.getCases()) {
			if (caseRegionRowCol.contains(c)) {
				// Il s'agit de l'une des cases intersection ligne/colonne avec la région, on l'ignore
				continue;
			}

			// S'il s'agit de l'une des cases intersection ligne/colonne avec la région, on l'ignore
			// if (isRow) {
			// if (c.getRow() == cases[0].getRow()) {
			// continue;
			// }
			// } else {
			// if (c.getColumn() == cases[0].getColumn()) {
			// continue;
			// }
			// }

			if (c.getCandidates().contains(candidat)) {
				c.removeCandidate(candidat);
			}
		}
	}

	/**
	 * Méthode d'exécution de l'élimination indirecte ligne/colonne coupant une région
	 */
	private void executeRegionCuttingRowColumnElimination() {
		final int size = getSudoku().getGrille().length;
		final Collection<Region> regions = getSudoku().getRegionsByFirstCase().values();
		for (int candidat = 1; candidat <= size; candidat++) {
			for (final Region region : regions) {
				final List<Integer> rows = region.getRows();
				for (final Integer row : rows) {
					eliminationRegionCuttingRow(Integer.valueOf(candidat), row, region);
				}

				final List<Integer> columns = region.getColumns();
				for (final Integer column : columns) {
					eliminationRegionCuttingColumn(Integer.valueOf(candidat), column, region);
				}
			}
		}
	}

	private void eliminationRegionCuttingRow(final Integer candidat, final int row, final Region region) {
		final Case[] casesByRow = getSudoku().getCasesByRow(row);
		eliminationRegionCuttingRowColumn(true, candidat, casesByRow, region);
	}

	private void eliminationRegionCuttingColumn(final Integer candidat, final int column, final Region region) {
		final Case[] casesByColumn = getSudoku().getCasesByColumn(column);
		eliminationRegionCuttingRowColumn(false, candidat, casesByColumn, region);
	}

	/**
	 * Elimination indirecte région - ligne ou colonne. Selon que c'est une région coupant une ligne ou une colonne et
	 * ayant un candidat pour la région uniquement dans les cases appartenant à la région et, pour la ligne/colonne,
	 * éventuellement dans d'autres cases, supprime les candidats des cases de la ligne/colonne, à l'exception des cases
	 * appartenant aussi à la région
	 * 
	 * @param isRow true si ligne, false si colonne
	 * @param candidat candidat à chercher
	 * @param cases liste des cases, ligne ou colonne
	 * @param region région
	 */
	private void eliminationRegionCuttingRowColumn(final boolean isRow, final Integer candidat, final Case[] cases,
			final Region region) {
		List<Case> caseRegionRowCol = null;
		if (isRow) {
			caseRegionRowCol = region.getCasesByRow(cases[0].getRow());
		} else {
			caseRegionRowCol = region.getCasesByColumn(cases[0].getColumn());
		}

		if (caseRegionRowCol == null) {
			return;
		}

		// On vérifie que le candidat est contenu dans l'une des cases intersection entre la ligne/colonne et la région
		// Si ce n'est pas le cas, on arrête tout
		boolean containsCandidate = false;
		for (final Case c : caseRegionRowCol) {
			if (c.getCandidates().contains(candidat)) {
				containsCandidate = true;
				break;
			}
		}
		if (!containsCandidate) {
			// Le candidat n'est contenu dans aucune case
			return;
		}

		// On vérifie que le candidat n'appartient pas aux cases de la région n'appartenant pas à la ligne/colonne
		for (final Case c : region.getCases()) {
			if (!caseRegionRowCol.contains(c) && c.getCandidates().contains(candidat)) {
				// Si la case de la région n'appartenant pas à la ligne/colonne contient le candidat candidat, on arrête
				// le processus
				return;
			}
		}

		// A ce stade, le candidat est contenu dans au moins l'une des cases intersection ligne/colonne et région et, le
		// candidat n'est pas contenu dans les autres cases de la région. On supprime le candidat des autres cases de la
		// ligne/colonne coupant la région
		for (final Case c : cases) {
			if (caseRegionRowCol.contains(c)) {
				// Il s'agit de l'une des cases intersection ligne/colonne avec la région, on l'ignore
				continue;
			}

			if (c.getCandidates().contains(candidat)) {
				c.removeCandidate(candidat);
			}
		}
	}
}
