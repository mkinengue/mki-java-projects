package fr.mkinengue.sudoku.methodes.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;

/**
 * M�thode consistant � chercher dans une ligne, colonne ou r�gion donn�e pour chacun des nombres de la s�rie s'il n'a
 * qu'une seule possibilit�. Si tel est le cas, il est valoris� dans la case correspondante
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
		for (int possible = 1; possible <= maxNb; possible++) {
			for (int row = 0; row < maxNb; row++) {
				setUniqueCaseWithPossibility(possible, getSudoku().getCasesByRow(row));
			}
		}
	}

	private void setUniqueCaseForColumns() {
		final int maxNb = getSudoku().getGrille().length;
		for (int possible = 1; possible <= maxNb; possible++) {
			for (int column = 0; column < maxNb; column++) {
				setUniqueCaseWithPossibility(possible, getSudoku().getCasesByColumn(column));
			}
		}
	}

	private void setUniqueCaseForRegions() {
		final int maxNb = getSudoku().getGrille().length;
		final Map<Case, Region> regionsByFirstCase = getSudoku().getMapRegionsByFirstCase();
		for (int possible = 1; possible <= maxNb; possible++) {
			for (final Region region : regionsByFirstCase.values()) {
				setUniqueCaseWithPossibility(possible, region.getCases());
			}
		}
	}

	/**
	 * Valorise si elle existe l'unique case contenant la possiblit� possible, pour toutes les cases de cases<br />
	 * Ne fait rien si la case n'est pas trouv�e ou si plusieurs cases poss�dent cette possibilit�
	 * 
	 * @param candidat candidat � chercher
	 * @param cases cases parmi lesquelles cherch�es
	 */
	private void setUniqueCaseWithPossibility(final int candidat, final Case[] cases) {
		Case c = null;
		boolean unique = false;
		for (final Case case1 : cases) {
			if (case1.getCandidates().contains(candidat)) {
				if (!unique) {
					c = case1;
					unique = true;
				} else {
					c = null;
				}
			}
		}

		if (c != null) {
			c.setValue(candidat);

			// On remplit la map des occurrences des nombres
			getSudoku().updateMapOccurrencesByNumber(c.getValue());

			// On supprime la case de la liste des cases vides
			getSudoku().getEmptyCases().remove(c);
		}
	}
}
