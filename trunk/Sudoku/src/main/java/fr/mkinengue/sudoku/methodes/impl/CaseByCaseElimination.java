/**
 * 
 */
package fr.mkinengue.sudoku.methodes.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;

/**
 * @author mkinengue<br />
 *         Méthode d'élimination case par case
 */
public class CaseByCaseElimination extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(CaseByCaseElimination.class.getSimpleName());

	/**
	 * @param sudoku
	 */
	public CaseByCaseElimination(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		final long debut = System.currentTimeMillis();

		// Méthode de réduction case par case
		// reduceCaseByCase();

		// Méthode de réduction par ligne, colonne, région
		reduceByRowColumnRegion();

		LOG.log(Level.INFO, "Méthode CaseByCaseElimination exécutée en {0} ms", System.currentTimeMillis() - debut);
	}

	/**
	 * Parcourt la grille de Sudoku case par case et, pour chaque case, procède à la réduction du nombre de candidats
	 * pour chacune des cases parcourues par région, par ligne et par colonne
	 */
	private void reduceByRowColumnRegion() {
		for (final Case[] rows : getSudoku().getGrille()) {
			for (final Case c : rows) {
				if (!c.isEmpty()) {
					continue;
				}

				// Flag servant à vérifier s'il y a eu un changement pour cette case
				boolean caseChanged = false;

				// Elimination sur la région
				final Region regionByCase = getSudoku().getRegionByCase(c);
				for (final Case cc : regionByCase.getCases()) {
					if (cc.isEmpty() || cc.equals(c)) {
						continue;
					}
					caseChanged = c.removeCandidate(cc.getValue()) || caseChanged;
				}

				// Elimination sur la ligne
				for (final Case cc : getSudoku().getCasesByRow(c.getRow())) {
					if (cc.isEmpty() || cc.equals(c)) {
						continue;
					}
					caseChanged = c.removeCandidate(cc.getValue()) || caseChanged;
				}

				// Elimination sur la colonne
				for (final Case cc : getSudoku().getCasesByColumn(c.getColumn())) {
					if (cc.isEmpty() || cc.equals(c)) {
						continue;
					}
					caseChanged = c.removeCandidate(cc.getValue()) || caseChanged;
				}

				// Mise à jour du Sudoku si la case a changé
				updateSudoku(caseChanged, c);
			}
		}
	}

	/**
	 * Mise à jour de la grille du Sudoku en mettant à jour la liste des priorités et la map des occurrences des nombres
	 * 
	 * @param boolean caseUpdated flag de mise à jour
	 * @param Case c Case potentiellement mise à jour
	 */
	private void updateSudoku(final boolean caseUpdated, final Case c) {
		if (caseUpdated) {
			getSudoku().updatePrioritiesByCase(c);

			if (c.getValue() != null) {
				getSudoku().updateMapOccurrencesByNumber(c.getValue());
			}
		}
	}
}
