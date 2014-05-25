package fr.mkinengue.sudoku.methodes.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;
import fr.mkinengue.sudoku.utils.SudokuUtils;

/**
 * Méthode consistant à chercher dans une ligne, colonne ou région donnée pour chacun des nombres de la série s'il
 * n'apparaît qu'une fois en tant que possibilité. Si tel est le cas, il est valorisé dans la case correspondante
 */
public class SingletonCache extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(SingletonCache.class.getSimpleName());

	public SingletonCache(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		final long debut = System.currentTimeMillis();
		setUniqueCaseForRows();
		setUniqueCaseForColumns();
		setUniqueCaseForRegions();

		LOG.log(Level.INFO, "Méthode SingletonCache exécutée en {0} ms", System.currentTimeMillis() - debut);
	}

	private void setUniqueCaseForRows() {
		final int maxNb = getSudoku().getGrille().length;
		for (Integer possible = 1; possible <= maxNb; possible++) {
			for (int row = 0; row < maxNb; row++) {
				setUniqueCaseWithPossibility(possible, getSudoku().getCasesByRow(row));
			}
		}
	}

	private void setUniqueCaseForColumns() {
		final int maxNb = getSudoku().getGrille().length;
		for (Integer possible = 1; possible <= maxNb; possible++) {
			for (int column = 0; column < maxNb; column++) {
				setUniqueCaseWithPossibility(possible, getSudoku().getCasesByColumn(column));
			}
		}
	}

	private void setUniqueCaseForRegions() {
		final int maxNb = getSudoku().getGrille().length;
		final Map<Case, Region> regionsByFirstCase = getSudoku().getRegionsByFirstCase();
		for (Integer possible = 1; possible <= maxNb; possible++) {
			for (final Region region : regionsByFirstCase.values()) {
				setUniqueCaseWithPossibility(possible, region.getCases());
			}
		}
	}

	/**
	 * Valorise si elle existe l'unique case contenant la possiblité "candidat", pour toutes les cases de cases<br />
	 * Ne fait rien si la case n'est pas trouvée ou si plusieurs cases possèdent cette possibilité
	 * 
	 * @param candidat candidat à chercher
	 * @param cases cases parmi lesquelles cherchées
	 */
	private void setUniqueCaseWithPossibility(final Integer candidat, final Case[] cases) {
		Case c = null;
		int nbOcc = 0;
		for (final Case case1 : cases) {
			if (case1.isEmpty() && case1.getCandidates().contains(candidat)) {
				nbOcc++;
				c = case1;
			}

			if (nbOcc >= 2) {
				// Le nombre d'occurrences du candidat dans les possibles est déjà supérieur à 1, on arrête
				break;
			}
		}

		if (nbOcc == 1) {
			SudokuUtils.updateCaseWithOneCandidate(c, getSudoku());
		}
	}
}
