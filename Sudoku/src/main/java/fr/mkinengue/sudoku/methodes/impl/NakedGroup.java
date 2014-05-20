package fr.mkinengue.sudoku.methodes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.logger.LogUtils;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;

/**
 * Méthode des groupes nus. La technique consiste à rechercher soit des paires, des triplés ou des quadruplés d'une même
 * zone (ligne, région ou colonne) apparaissant dans une paire, un triplé ou un quadruplé de cellules et uniquement ces
 * candidats puis, on élimine ces candidats s'ils apparaissent dans d'autres cellules de la zone
 */
public class NakedGroup extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(NakedGroup.class.getSimpleName());

	public NakedGroup(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "execute");
		final long debut = System.currentTimeMillis();
		executePairs();
		// executeTriplets();
		// executeQuadruplets();

		LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(), "execute", debut);
	}

	/**
	 * Exécution de la recherche des paires et élimination dans les autres cellules pour les paires trouvées<br />
	 * On recherche toutes les paires possibles
	 */
	private void executePairs() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executePairs");
		final int size = getSudoku().getGrille().length;
		for (int candidat1 = 1; candidat1 <= size - 1; candidat1++) {
			for (int candidat2 = candidat1 + 1; candidat2 <= size; candidat2++) {
				executePairForRowsColumns(candidat1, candidat2);
				executePairForRegions(candidat1, candidat2);
			}
		}
	}

	/**
	 * Exécution de la recherche des triplets et élimination dans les autres cellules pour les triplés trouvés<br />
	 * On recherche tous les triplés possibles
	 */
	private void executeTriplets() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeTriplets");
		final int size = getSudoku().getGrille().length;
		for (int candidat1 = 1; candidat1 <= size - 2; candidat1++) {
			for (int candidat2 = candidat1 + 1; candidat2 <= size - 1; candidat2++) {
				for (int candidat3 = candidat2 + 1; candidat3 <= size; candidat3++) {
					executeTripletForRowsColumns(candidat1, candidat2, candidat3);
					executeTripletForRegions(candidat1, candidat2, candidat3);
				}
			}
		}
	}

	/**
	 * Exécution de la recherche des quadruplets etélimination dans les autres cellules pour les quadruplets trouvés<br />
	 * On recherche tous les quadruplets possibles
	 */
	private void executeQuadruplets() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeQuadruplets");
		final int size = getSudoku().getGrille().length;
		for (int candidat1 = 1; candidat1 <= size - 3; candidat1++) {
			for (int candidat2 = candidat1 + 1; candidat2 <= size - 2; candidat2++) {
				for (int candidat3 = candidat2 + 1; candidat3 <= size - 1; candidat3++) {
					for (int candidat4 = candidat3 + 1; candidat4 <= size; candidat4++) {
						executeQuadrupletForRowsColumns(candidat1, candidat2, candidat3, candidat4);
						executeQuadrupletForRegions(candidat1, candidat2, candidat3, candidat4);
					}
				}
			}
		}
	}

	/**
	 * Exécution de la recherche des paires sur les lignes et les colonnes
	 * 
	 * @param candidat1 candidat 1 de la paire
	 * @param candidat2 candidat 2 de la paire
	 */
	private void executePairForRowsColumns(final int candidat1, final int candidat2) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executePairForRowsColumns");
		for (int rowCol = 0; rowCol < getSudoku().getGrille().length; rowCol++) {
			// Exécution par ligne
			final List<Case> casesPairRow = lookForPairCandidatesRow(candidat1, candidat2, rowCol);
			if (!casesPairRow.isEmpty()) {
				for (final Case cRow : getSudoku().getCasesByRow(rowCol)) {
					if (!casesPairRow.contains(cRow)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cRow.removeCandidate(candidat1);
						cRow.removeCandidate(candidat2);
					}
				}
			}

			// Exécution par colonne
			final List<Case> casesPaireColumn = lookForPaireCandidatesColumn(candidat1, candidat2, rowCol);
			if (!casesPaireColumn.isEmpty()) {
				for (final Case cCol : getSudoku().getCasesByColumn(rowCol)) {
					if (!casesPaireColumn.contains(cCol)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cCol.removeCandidate(candidat1);
						cCol.removeCandidate(candidat2);
					}
				}
			}
		}
	}

	/**
	 * Exécution de la recherche des triplets sur les lignes et les colonnes
	 * 
	 * @param candidat1 candidat 1 du triplé
	 * @param candidat2 candidat 2 du triplé
	 * @param candidat3 candidat 3 du triplé
	 */
	private void executeTripletForRowsColumns(final int candidat1, final int candidat2, final int candidat3) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeTripletForRowsColumns");
		for (int rowCol = 0; rowCol < getSudoku().getGrille().length; rowCol++) {
			// Exécution par ligne
			final List<Case> casesTripletRow = lookForTripletCandidatesRow(candidat1, candidat2, candidat3, rowCol);
			if (!casesTripletRow.isEmpty()) {
				for (final Case cRow : getSudoku().getCasesByRow(rowCol)) {
					if (!casesTripletRow.contains(cRow)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cRow.removeCandidate(candidat1);
						cRow.removeCandidate(candidat2);
						cRow.removeCandidate(candidat3);
					}
				}
			}

			// Exécution par colonne
			final List<Case> casesTripletColumn = lookForTripletCandidatesColumn(candidat1, candidat2, candidat3,
							rowCol);
			if (!casesTripletColumn.isEmpty()) {
				for (final Case cCol : getSudoku().getCasesByColumn(rowCol)) {
					if (!casesTripletColumn.contains(cCol)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cCol.removeCandidate(candidat1);
						cCol.removeCandidate(candidat2);
						cCol.removeCandidate(candidat3);
					}
				}
			}
		}
	}

	/**
	 * Ex�cution de la recherche des quadruplets sur les lignes et les colonnes
	 * 
	 * @param candidat1 candidat 1 du quadruplet
	 * @param candidat2 candidat 2 du quadruplet
	 * @param candidat3 candidat 3 du quadruplet
	 * @param candidat4 candidat 4 du quadruplet
	 */
	private void executeQuadrupletForRowsColumns(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeQuadrupletForRowsColumns");
		for (int rowCol = 0; rowCol < getSudoku().getGrille().length; rowCol++) {
			// Ex�cution par ligne
			final List<Case> casesQuadrupletRow = lookForQuadrupletCandidatesRow(candidat1, candidat2, candidat3,
							candidat4, rowCol);
			if (!casesQuadrupletRow.isEmpty()) {
				for (final Case cRow : getSudoku().getCasesByRow(rowCol)) {
					if (!casesQuadrupletRow.contains(cRow)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cRow.removeCandidate(candidat1);
						getSudoku().updateCaseWithOneCandidate(cRow);
						cRow.removeCandidate(candidat2);
						getSudoku().updateCaseWithOneCandidate(cRow);
						cRow.removeCandidate(candidat3);
						getSudoku().updateCaseWithOneCandidate(cRow);
						cRow.removeCandidate(candidat4);
						getSudoku().updateCaseWithOneCandidate(cRow);
					}
				}
			}

			// Exécution par colonne
			final List<Case> casesQuadrupletColumn = lookForQuadrupletCandidatesColumn(candidat1, candidat2, candidat3,
							candidat4, rowCol);
			if (!casesQuadrupletColumn.isEmpty()) {
				for (final Case cCol : getSudoku().getCasesByColumn(rowCol)) {
					if (!casesQuadrupletColumn.contains(cCol)) {
						// La case courante de la ligne n'est pas l'une des case contenant la paire candidat1/candidat2
						// On retire d'une telle case, tous les candidats de la paire
						cCol.removeCandidate(candidat1);
						getSudoku().updateCaseWithOneCandidate(cCol);
						cCol.removeCandidate(candidat2);
						getSudoku().updateCaseWithOneCandidate(cCol);
						cCol.removeCandidate(candidat3);
						getSudoku().updateCaseWithOneCandidate(cCol);
						cCol.removeCandidate(candidat4);
						getSudoku().updateCaseWithOneCandidate(cCol);
					}
				}
			}
		}
	}

	/**
	 * Ex�cution de la recherche des paires sur les r�gions
	 * 
	 * @param candidat1 candidat 1 de la paire
	 * @param candidat2 candidat 2 de la paire
	 */
	private void executePairForRegions(final int candidat1, final int candidat2) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executePairForRegions");
		for (final Region region : getSudoku().getRegionsByFirstCase().values()) {
			final List<Case> casesRegion = lookForPaireCandidatesRegion(candidat1, candidat2, region);
			if (!casesRegion.isEmpty()) {
				for (final Case cRegion : region.getCases()) {
					if (!casesRegion.contains(cRegion)) {
						// La case courante de la région n'est pas l'une des cases contenant les candidats cherchés
						// On supprime les candidats de la liste des candidats de cette case
						cRegion.removeCandidate(candidat1);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat2);
						getSudoku().updateCaseWithOneCandidate(cRegion);
					}
				}
			}
		}
	}

	/**
	 * Ex�cution de la recherche des triplets sur les r�gions
	 * 
	 * @param candidat1 candidat 1 du triplet
	 * @param candidat2 candidat 2 du triplet
	 * @param candidat3 candidat 3 du triplet
	 */
	private void executeTripletForRegions(final int candidat1, final int candidat2, final int candidat3) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeTripletForRegions");
		for (final Region region : getSudoku().getRegionsByFirstCase().values()) {
			final List<Case> casesRegion = lookForTripletCandidatesRegion(candidat1, candidat2, candidat3, region);
			if (!casesRegion.isEmpty()) {
				for (final Case cRegion : region.getCases()) {
					if (!casesRegion.contains(cRegion)) {
						// La case courante de la r�gion n'est pas l'une des cases contenant les candidats cherch�s
						// On supprime les candidats de la liste des candidats de cette case
						cRegion.removeCandidate(candidat1);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat2);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat3);
						getSudoku().updateCaseWithOneCandidate(cRegion);
					}
				}
			}
		}
	}

	/**
	 * Ex�cution de la recherche des quadruplets sur les r�gions
	 * 
	 * @param candidat1 candidat 1 du quadruplet
	 * @param candidat2 candidat 2 du quadruplet
	 * @param candidat3 candidat 3 du quadruplet
	 * @param candidat4 candidat 4 du quadruplet
	 */
	private void executeQuadrupletForRegions(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "executeQuadrupletForRegions");
		for (final Region region : getSudoku().getRegionsByFirstCase().values()) {
			final List<Case> casesRegion = lookForQuadrupletCandidatesRegion(candidat1, candidat2, candidat3,
							candidat4, region);
			if (!casesRegion.isEmpty()) {
				for (final Case cRegion : region.getCases()) {
					if (!casesRegion.contains(cRegion)) {
						// La case courante de la r�gion n'est pas l'une des cases contenant les candidats cherch�s
						// On supprime les candidats de la liste des candidats de cette case
						cRegion.removeCandidate(candidat1);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat2);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat3);
						getSudoku().updateCaseWithOneCandidate(cRegion);
						cRegion.removeCandidate(candidat4);
						getSudoku().updateCaseWithOneCandidate(cRegion);
					}
				}
			}
		}
	}

	/**
	 * Recherche de la paire candidat1-candidat2 dans la ligne row comme candidats seuls d'une paire de cellules
	 * 
	 * @param candidat1 candidat 1
	 * @param candidat2 candidat 2
	 * @param row numéro de ligne où la recherche de la paire s'effectue
	 * @return liste de cases
	 */
	private List<Case> lookForPairCandidatesRow(final int candidat1, final int candidat2, final int row) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForPairCandidatesRow");
		return lookForPairCandidates(candidat1, candidat2, getSudoku().getCasesByRow(row));
	}

	private List<Case> lookForTripletCandidatesRow(final int candidat1, final int candidat2, final int candidat3,
					final int row) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForTripletCandidatesRow");
		return lookForTripletsCandidates(candidat1, candidat2, candidat3, getSudoku().getCasesByRow(row));
	}

	private List<Case> lookForQuadrupletCandidatesRow(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4, final int row) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForQuadrupletCandidatesRow");
		return lookForQuadrupletsCandidates(candidat1, candidat2, candidat3, candidat4, getSudoku().getCasesByRow(row));
	}

	private List<Case> lookForPaireCandidatesColumn(final int candidat1, final int candidat2, final int column) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForPaireCandidatesColumn");
		return lookForPairCandidates(candidat1, candidat2, getSudoku().getCasesByColumn(column));
	}

	private List<Case> lookForTripletCandidatesColumn(final int candidat1, final int candidat2, final int candidat3,
					final int column) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForTripletCandidatesColumn");
		return lookForTripletsCandidates(candidat1, candidat2, candidat3, getSudoku().getCasesByColumn(column));
	}

	private List<Case> lookForQuadrupletCandidatesColumn(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4, final int column) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForQuadrupletCandidatesColumn");
		return lookForQuadrupletsCandidates(candidat1, candidat2, candidat3, candidat4,
						getSudoku().getCasesByColumn(column));
	}

	private List<Case> lookForPaireCandidatesRegion(final int candidat1, final int candidat2, final Region region) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForPaireCandidatesRegion");
		return lookForPairCandidates(candidat1, candidat2, region.getCases());
	}

	private List<Case> lookForTripletCandidatesRegion(final int candidat1, final int candidat2, final int candidat3,
					final Region region) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForTripletCandidatesRegion");
		return lookForTripletsCandidates(candidat1, candidat2, candidat3, region.getCases());
	}

	private List<Case> lookForQuadrupletCandidatesRegion(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4, final Region region) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForQuadrupletCandidatesRegion");
		return lookForQuadrupletsCandidates(candidat1, candidat2, candidat3, candidat4, region.getCases());
	}

	/**
	 * Recherche des cases ayant comme uniques candidats candidat1 et candidat2<br />
	 * Retourne une liste de cases vides contenant comme seuls candidats la paire cherchée<br />
	 * Retourne une liste vide si une telle paire n'est pas trouvée
	 * 
	 * @param candidat1 candidat 1 de la paire
	 * @param candidat2 candidat 2 de la paire
	 * @param cases liste de cases dans lesquelles effectuées la recherche
	 * @return liste cases
	 */
	private List<Case> lookForPairCandidates(final int candidat1, final int candidat2, final Case[] cases) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForPairCandidates");
		final List<Integer> candidates = new ArrayList<Integer>();
		candidates.add(candidat1);
		candidates.add(candidat2);
		return lookForUniqueCandidates(candidates, cases);
	}

	/**
	 * Recherche des cases ayant comme uniques candidats candidat1, candidat2 et candidat3<br />
	 * Retourne une liste de cases vides contenant comme seuls candidats le triplet cherchée<br />
	 * Retourne une liste vide si un tel triplet n'est pas trouvée
	 * 
	 * @param candidat1 candidat 1 du triplet
	 * @param candidat2 candidat 2 du triplet
	 * @param candidat3 candidat 3 du triplet
	 * @param cases liste de cases dans lesquelles effectuées la recherche
	 * @return liste cases
	 */
	private List<Case> lookForTripletsCandidates(final int candidat1, final int candidat2, final int candidat3,
					final Case[] cases) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForTripletsCandidates");
		final List<Integer> candidates = new ArrayList<Integer>();
		candidates.add(candidat1);
		candidates.add(candidat2);
		candidates.add(candidat3);
		return lookForUniqueCandidates(candidates, cases);
	}

	/**
	 * Recherche des cases ayant comme uniques candidats candidat1, candidat2, candidat3 et candidat4<br />
	 * Retourne une liste de cases vides contenant comme seuls candidats le quadruplet cherch�e<br />
	 * Retourne une liste vide si un tel quadruplet n'est pas trouv�e
	 * 
	 * @param candidat1 candidat 1 du quadruplet
	 * @param candidat2 candidat 2 du quadruplet
	 * @param candidat3 candidat 3 du quadruplet
	 * @param candidat4 candidat 4 du quadruplet
	 * @param cases liste de cases dans lesquelles effectu�es la recherche
	 * @return liste cases
	 */
	private List<Case> lookForQuadrupletsCandidates(final int candidat1, final int candidat2, final int candidat3,
					final int candidat4, final Case[] cases) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForQuadrupletsCandidates");
		final List<Integer> candidates = new ArrayList<Integer>();
		candidates.add(candidat1);
		candidates.add(candidat2);
		candidates.add(candidat3);
		candidates.add(candidat4);
		return lookForUniqueCandidates(candidates, cases);
	}

	/**
	 * Recherche des cases ayant comme uniques candidats candidat1 et candidat2<br />
	 * Retourne une liste de cases vides contenant comme seuls candidats la paire cherchée<br />
	 * Retourne une liste vide si une telle paire n'est pas trouvée
	 * 
	 * @param candidatCherches liste de candidats cherchés comme unique possibilité
	 * @param cases liste de cases dans lesquelles effectuées la recherche
	 * @return liste cases
	 */
	private List<Case> lookForUniqueCandidates(final List<Integer> candidatCherches, final Case[] cases) {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForPairCandidates");
		final List<Case> liste = new ArrayList<Case>();
		for (final Case c : cases) {
			if (!c.isEmpty()) {
				// La case n'est pas vide, on passe à la case suivante
				c.getCandidates().clear();
				continue;
			}

			final List<Integer> candidates = c.getCandidates();
			final List<Integer> cloneCandidates = new ArrayList<Integer>(candidates);
			cloneCandidates.removeAll(candidatCherches);
			if (cloneCandidates.isEmpty()) {
				// La case courante contient comme candidats au moins une partie de ceux cherchés dans candidatCherches
				liste.add(c);
			}
		}

		if (liste.size() >= candidatCherches.size()) {
			// On retourne la liste obtenue que si elle est au moins aussi grande que celle des candidats cherchés
			return liste;
		} else {
			return new ArrayList<Case>();
		}
	}
}
