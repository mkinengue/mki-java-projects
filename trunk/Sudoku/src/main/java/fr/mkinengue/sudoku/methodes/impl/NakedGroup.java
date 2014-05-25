package fr.mkinengue.sudoku.methodes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Row;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.logger.LogUtils;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;

/**
 * Méthode des groupes nus. La technique consiste à rechercher parmi les listes des candidats soit des paires, des
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
		lookForCaseWithSameCandidatesByRow();
	}

	/**
	 * Cherche pour chaque ligne les cases vides ayant exactement les mêmes candidats. Si le nombre de candidats de ces
	 * cases correspond exactement au nombre de cases, bingo ! On supprime alors des autres cases vides les candidats
	 * précédemment trouvés qu'elles contiennent
	 */
	private void lookForCaseWithSameCandidatesByRow() {
		final long debut = System.currentTimeMillis();
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "lookForCaseWithSameCandidatesByRow");
		final Map<Integer, List<Case>> mapCasesWithSameCandidatesByRowIdx = new HashMap<Integer, List<Case>>();
		for (final Row row : getSudoku().getRowsList()) {
			final List<Case> rowEmptyCases = row.getEmptyCases();
			if (!rowEmptyCases.isEmpty()) {
				final int sizeEmptyCases = rowEmptyCases.size();
				for (int i = 0; i < sizeEmptyCases - 1; i++) {
					for (int j = i + 1; j < sizeEmptyCases; j++) {
						final List<Integer> cand1 = new ArrayList<Integer>(rowEmptyCases.get(i).getCandidates());
						cand1.removeAll(rowEmptyCases.get(j).getCandidates());
						if (cand1.isEmpty()) {
							// Les deux cases d'index i et j dans la liste ont exactement les mêmes candidats, on les
							// ajoute dans la map de travail
							if (mapCasesWithSameCandidatesByRowIdx.get(rowEmptyCases.get(i).getRow()) == null) {
								mapCasesWithSameCandidatesByRowIdx.put(rowEmptyCases.get(i).getRow(),
												new ArrayList<Case>());
								mapCasesWithSameCandidatesByRowIdx.get(rowEmptyCases.get(i).getRow()).add(
												rowEmptyCases.get(i));
							}
							mapCasesWithSameCandidatesByRowIdx.get(rowEmptyCases.get(i).getRow()).add(
											rowEmptyCases.get(j));
						}
					}
				}
			}
		}

		// On vérifie si la map de travail est remplie ou non. Si elle l'est, on traite ligne par ligne
		if (mapCasesWithSameCandidatesByRowIdx.isEmpty()) {
			// Aucun groupe de cases pour chacune des lignes ne contiennent exactement le même nombre de candidats
			LOG.log(Level.INFO, "Aucun groupe de cases pour chacune des lignes n'a exactement les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRow", debut);
			return;
		}

		LOG.log(Level.INFO, "Quelques cases pour certaines lignes ont exactement les mêmes candidats");
		final List<Integer> rows2Keep = new ArrayList<Integer>();
		for (final Integer rowIdx : mapCasesWithSameCandidatesByRowIdx.keySet()) {
			final List<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRowIdx.get(rowIdx);
			if (casesWithSameCandidates.size() != casesWithSameCandidates.get(0).getCandidates().size()) {
				// Le nombre de candidats et le nombre d'éléments de la liste des cases ayant les mêmes candidats
				// diffèrent. On ne peut aller plus loin
				LOG.log(Level.INFO,
								"Ligne {0} non exploitable car le nombre de candidats diffère de celui du nombre de cases ayant les mêmes candidats. Nombre de candidats : {1} - Nombre de cases : {2}",
								new Object[] { rowIdx, casesWithSameCandidates.get(0).getCandidates().size(),
												casesWithSameCandidates.size() });
				continue;
			}
			rows2Keep.add(rowIdx);
		}

		if (rows2Keep.isEmpty()) {
			LOG.log(Level.INFO, "Aucun ligne exploitable malgré le fait que des cases avaient les mêmes candidats");
			LogUtils.logExitingMethod(LOG, Level.INFO, getClass().getSimpleName(),
							"lookForCaseWithSameCandidatesByRow", debut);
			return;
		}
		for (final Row row : getSudoku().getRowsList()) {
			if (!rows2Keep.contains(Integer.valueOf(row.getIndex()))) {
				// Ce n'est pas une ligne à exploiter, on passe à la suite
				continue;
			}
			final List<Case> casesWithSameCandidates = mapCasesWithSameCandidatesByRowIdx.get(Integer.valueOf(row
							.getIndex()));
			final List<Integer> candidates = casesWithSameCandidates.get(0).getCandidates();
			final List<Case> rowEmptyCases = row.getEmptyCases();
			for (final Case emptyCase : rowEmptyCases) {
				if (casesWithSameCandidates.contains(emptyCase)) {
					// Il s'agit d'une des cases à garder et contenant exactement les mêmes candidats que certaines
					// autres cases vides
					continue;
				}
				// On supprime de la case vide, les candidats trouvés précédemment
				emptyCase.getCandidates().removeAll(candidates);
			}
			LOG.log(Level.INFO, "Fin application de la NakedGroup méthode pour la ligne {0} avec les candidats {1}",
							new Object[] { row.getIndex(), candidates });
		}
	}
}
