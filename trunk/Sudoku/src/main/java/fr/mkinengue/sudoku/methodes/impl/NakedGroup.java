package fr.mkinengue.sudoku.methodes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.bean.RowColumnAbstract;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.logger.LogUtils;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;
import fr.mkinengue.sudoku.utils.SudokuUtils;

/**
 * Méthode des "groupes nus". La technique consiste à rechercher parmi les listes des candidats soit des paires, des
 * triplés ou des quadruplés d'une même zone (ligne, région ou colonne) apparaissant dans une paire, un triplé ou un
 * quadruplé de cellules et uniquement ces candidats puis, on élimine ces candidats s'ils apparaissent dans d'autres
 * cellules de la zone
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
		lookForCaseWithSameCandidates();

		LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(), "execute", debut);
	}

	/**
	 * Exécution de la recherche des paires et élimination dans les autres cellules pour les paires trouvées<br />
	 * On recherche parmi les candidats d'une même zone
	 */
	private void lookForCaseWithSameCandidates() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForCaseWithSameCandidates");
		lookForCaseWithSameCandidatesByRowColumn(true);
		lookForCaseWithSameCandidatesByRowColumn(false);
		lookForCaseWithSameCandidatesByRegion();
	}

	/**
	 * Cherche pour chaque ligne/colonne, les cases vides ayant exactement les mêmes candidats. Si le nombre de
	 * candidats de ces cases correspond exactement au nombre de cases, bingo ! On supprime alors des autres cases vides
	 * les candidats précédemment trouvés qu'elles contiennent
	 */
	private void lookForCaseWithSameCandidatesByRowColumn(boolean isRow) {
		final long debut = System.currentTimeMillis();
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(),
						"lookForCaseWithSameCandidatesByRowColumn");
		final Map<Integer, List<Case>> mapCasesWithSameCandidatesByRowColIdx = new HashMap<Integer, List<Case>>();

		List<RowColumnAbstract> rowCols = isRow ? SudokuUtils.listRow2ListRowColAbstract(getSudoku().getRowsList())
						: SudokuUtils.listColumn2ListRowColAbstract(getSudoku().getColumnsList());
		for (final RowColumnAbstract rowCol : rowCols) {
			final List<Case> rowColEmptyCases = rowCol.getEmptyCases();
			if (!rowColEmptyCases.isEmpty()) {
				final int sizeEmptyCases = rowColEmptyCases.size();
				for (int i = 0; i < sizeEmptyCases - 1; i++) {
					for (int j = i + 1; j < sizeEmptyCases; j++) {
						final List<Integer> cand1 = new ArrayList<Integer>(rowColEmptyCases.get(i).getCandidates());
						cand1.removeAll(rowColEmptyCases.get(j).getCandidates());
						if (cand1.isEmpty()
										&& rowColEmptyCases.get(i).getCandidates().size() == rowColEmptyCases.get(j)
														.getCandidates().size()) {
							// Les deux cases d'index i et j dans la liste ont exactement les mêmes candidats, on les
							// ajoute dans la map de travail
							final Integer idxRowCol = isRow ? rowColEmptyCases.get(i).getRow() : rowColEmptyCases
											.get(i).getColumn();

							if (mapCasesWithSameCandidatesByRowColIdx.get(idxRowCol) == null) {
								mapCasesWithSameCandidatesByRowColIdx.put(idxRowCol, new ArrayList<Case>());
								mapCasesWithSameCandidatesByRowColIdx.get(idxRowCol).add(rowColEmptyCases.get(i));
							}
							mapCasesWithSameCandidatesByRowColIdx.get(idxRowCol).add(rowColEmptyCases.get(j));
						}
					}
				}
			}
		}

		// On vérifie si la map de travail est remplie ou non. Si elle l'est, on traite ligne/colonne par ligne/colonne
		if (mapCasesWithSameCandidatesByRowColIdx.isEmpty()) {
			// Aucun groupe de cases pour chacune des lignes/colonnes ne contiennent exactement le même nombre de
			// candidats
			LOG.log(Level.INFO,
							"Aucun groupe de cases pour chacune des lignes/colonnes n'a exactement les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRowColumn", debut);
			return;
		}

		LOG.log(Level.INFO, "Quelques cases pour certaines lignes/colonnes ont exactement les mêmes candidats");
		final List<Integer> rowCols2Keep = new ArrayList<Integer>();
		for (final Integer rowColIdx : mapCasesWithSameCandidatesByRowColIdx.keySet()) {
			final List<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRowColIdx.get(rowColIdx);
			if (casesWithSameCandidates.size() != casesWithSameCandidates.get(0).getCandidates().size()) {
				// Le nombre de candidats et le nombre d'éléments de la liste des cases ayant les mêmes candidats
				// diffèrent. On ne peut aller plus loin
				LOG.log(Level.INFO,
								"Ligne/Colonne {0} non exploitable car le nombre de candidats diffère de celui du nombre de cases ayant les mêmes candidats. Nombre de candidats : {1} - Nombre de cases : {2}",
								new Object[] { rowColIdx, casesWithSameCandidates.get(0).getCandidates().size(),
												casesWithSameCandidates.size() });
				continue;
			}
			rowCols2Keep.add(rowColIdx);
		}

		if (rowCols2Keep.isEmpty()) {
			LOG.log(Level.INFO,
							"Aucune ligne/colonne exploitable malgré le fait que des cases avaient les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRowColumn", debut);
			return;
		}

		rowCols = isRow ? SudokuUtils.listRow2ListRowColAbstract(getSudoku().getRowsList()) : SudokuUtils
						.listColumn2ListRowColAbstract(getSudoku().getColumnsList());
		for (final RowColumnAbstract rowCol : rowCols) {
			if (!rowCols2Keep.contains(Integer.valueOf(rowCol.getIndex()))) {
				// Ce n'est pas une ligne/colonne à exploiter, on passe à la suite
				continue;
			}
			final List<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRowColIdx.get(Integer.valueOf(rowCol
							.getIndex()));
			final List<Integer> candidates = casesWithSameCandidates.get(0).getCandidates();
			final List<Case> rowEmptyCases = rowCol.getEmptyCases();
			for (final Case emptyCase : rowEmptyCases) {
				if (casesWithSameCandidates.contains(emptyCase)) {
					// Il s'agit d'une des cases du groupe contenant exactement les mêmes candidats que certaines
					// autres cases vides : elle est à garder
					continue;
				}

				// On supprime de la case vide, les candidats trouvés précédemment
				emptyCase.getCandidates().removeAll(candidates);
			}
			LOG.log(Level.INFO,
							"Fin application de la NakedGroup méthode pour la ligne/colonne {0} avec les candidats {1}. isRow = {2}",
							new Object[] { rowCol.getIndex(), candidates, isRow });
		}
	}

	/**
	 * Cherche pour chaque région, les cases vides ayant exactement les mêmes candidats. Si le nombre de candidats de
	 * ces cases correspond exactement au nombre de cases, bingo ! On supprime alors des autres cases vides les
	 * candidats précédemment trouvés qu'elles contiennent
	 */
	private void lookForCaseWithSameCandidatesByRegion() {
		final long debut = System.currentTimeMillis();
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForCaseWithSameCandidatesByRegion");
		final Map<Case, TreeSet<Case>> mapCasesWithSameCandidatesByRegion = new HashMap<Case, TreeSet<Case>>();
		for (final Region region : getSudoku().getRegionsByFirstCase().values()) {
			final List<Case> regEmptyCases = region.getEmptyCases();
			if (!regEmptyCases.isEmpty()) {
				final int sizeEmptyCases = regEmptyCases.size();
				for (int i = 0; i < sizeEmptyCases - 1; i++) {
					for (int j = i + 1; j < sizeEmptyCases; j++) {
						final List<Integer> cand1 = new ArrayList<Integer>(regEmptyCases.get(i).getCandidates());
						cand1.removeAll(regEmptyCases.get(j).getCandidates());
						if (cand1.isEmpty()
										&& regEmptyCases.get(i).getCandidates().size() == regEmptyCases.get(j)
														.getCandidates().size()) {
							// Les deux cases d'index i et j dans la liste ont exactement les mêmes candidats, on les
							// ajoute dans la map de travail
							if (mapCasesWithSameCandidatesByRegion.get(region.getFirstCase()) == null) {
								mapCasesWithSameCandidatesByRegion.put(region.getFirstCase(), new TreeSet<Case>());
								mapCasesWithSameCandidatesByRegion.get(region.getFirstCase()).add(regEmptyCases.get(i));
							}
							mapCasesWithSameCandidatesByRegion.get(region.getFirstCase()).add(regEmptyCases.get(j));
						}
					}
				}
			}
		}

		// On vérifie si la map de travail est remplie ou non. Si elle l'est, on traite région par région
		if (mapCasesWithSameCandidatesByRegion.isEmpty()) {
			// Aucun groupe de cases pour chacune des régions ne contiennent exactement le même nombre de candidats
			LOG.log(Level.INFO, "Aucun groupe de cases pour chacune des régions n'a exactement les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRegion", debut);
			return;
		}

		LOG.log(Level.INFO, "Quelques cases pour certaines régions ont exactement les mêmes candidats");
		final List<Case> regions2KeepByFirstCase = new ArrayList<Case>();
		for (final Case firstCaseOfReg : mapCasesWithSameCandidatesByRegion.keySet()) {
			final TreeSet<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRegion.get(firstCaseOfReg);
			if (casesWithSameCandidates.size() != casesWithSameCandidates.first().getCandidates().size()) {
				// Le nombre de candidats et le nombre d'éléments de la liste des cases ayant les mêmes candidats
				// diffèrent. On ne peut aller plus loin
				LOG.log(Level.INFO,
								"Région {0} non exploitable car le nombre de candidats diffère de celui du nombre de cases ayant les mêmes candidats. Nombre de candidats : {1} - Nombre de cases : {2}",
								new Object[] { firstCaseOfReg, casesWithSameCandidates.first().getCandidates().size(),
												casesWithSameCandidates.size() });
				continue;
			}
			regions2KeepByFirstCase.add(firstCaseOfReg);
		}

		if (regions2KeepByFirstCase.isEmpty()) {
			LOG.log(Level.INFO,
							"Aucune région exploitable malgré le fait que certaines cases avaient les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRegion", debut);
			return;
		}

		for (final Case firstCaseOfReg : getSudoku().getRegionsByFirstCase().keySet()) {
			if (!regions2KeepByFirstCase.contains(firstCaseOfReg)) {
				// Ce n'est pas une région à exploiter, on passe à la suite
				continue;
			}
			final TreeSet<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRegion.get(firstCaseOfReg);
			final List<Integer> candidates = casesWithSameCandidates.first().getCandidates();
			final List<Case> regEmptyCases = getSudoku().getRegionsByFirstCase().get(firstCaseOfReg).getEmptyCases();
			for (final Case emptyCase : regEmptyCases) {
				if (casesWithSameCandidates.contains(emptyCase)) {
					// Il s'agit d'une des cases du groupe contenant exactement les mêmes candidats que certaines
					// autres cases vides : elle est à garder
					continue;
				}

				// On supprime de la case vide, les candidats trouvés précédemment
				emptyCase.getCandidates().removeAll(candidates);
			}
			LOG.log(Level.INFO, "Fin application de la NakedGroup méthode pour la région {0} avec les candidats {1}",
							new Object[] { firstCaseOfReg, candidates });
		}
	}
}
