package fr.mkinengue.sudoku.methodes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	/**
	 * Cherche pour chaque ligne s'il existe une case vide pour laquelle et uniquement pour laquelle appparaît un
	 * candidat qui est par conséquent utilisé pour valoriser la case
	 */
	private void setUniqueCaseForRows() {
		final int maxNb = getSudoku().getGrille().length;
		for (int row = 0; row < maxNb; row++) {
			setCaseContainingUniqueCandidate(getSudoku().getCasesByRow(row));
		}
	}

	/**
	 * Cherche pour chaque colonne s'il existe une case vide pour laquelle et uniquement pour laquelle appparaît un
	 * candidat qui est par conséquent utilisé pour valoriser la case
	 */
	private void setUniqueCaseForColumns() {
		final int maxNb = getSudoku().getGrille().length;
		for (int column = 0; column < maxNb; column++) {
			setCaseContainingUniqueCandidate(getSudoku().getCasesByColumn(column));
		}
	}

	/**
	 * Cherche pour chaque région s'il existe une case vide pour laquelle et uniquement pour laquelle appparaît un
	 * candidat qui est par conséquent utilisé pour valoriser la case
	 */
	private void setUniqueCaseForRegions() {
		final Map<Case, Region> regionsByFirstCase = getSudoku().getRegionsByFirstCase();
		for (final Region region : regionsByFirstCase.values()) {
			setCaseContainingUniqueCandidate(region.getCases());
		}
	}

	/**
	 * Recherche dans la liste des cases en paramètres s'il existe une case dans laquelle et uniquement dans laquelle
	 * apparaît un candidat donné : si une telle case existe, le candidat est mis à jour dans la case et le Sudoku mis à
	 * jour en conséquence. <br />
	 * La méthode consiste à parcourir case par case et, construire une map de candidats associés à la liste des cases
	 * les contenant. Si une liste de cases contient un unique élément pour un candidat donné, alors ce candidat
	 * apparaît uniquement dans cette case pour le groupe de cases en paramètre et la condition est remplie
	 * 
	 * @param cases
	 */
	private void setCaseContainingUniqueCandidate(final Case[] cases) {
		final Map<Integer, List<Case>> mapCasesContainingCandidate = new HashMap<Integer, List<Case>>();
		for (final Case c : cases) {
			if (!c.isEmpty()) {
				continue;
			}
			for (final Integer cand : c.getCandidates()) {
				if (mapCasesContainingCandidate.get(cand) == null) {
					mapCasesContainingCandidate.put(cand, new ArrayList<Case>());
				}
				mapCasesContainingCandidate.get(cand).add(c);
			}
		}

		// On parcourt la map des cases par candidats : si un candidat n'est présent que dans une case, on valorise la
		// case avec le candidat
		for (final Integer cand : mapCasesContainingCandidate.keySet()) {
			final List<Case> casesContainingCand = mapCasesContainingCandidate.get(cand);
			if (casesContainingCand.size() == 1) {
				// Une unique case dans le groupe des cases en paramètre contient le candidat en cours, on valorise la
				// case avec le candidat
				SudokuUtils.updateCaseWithValue(Integer.valueOf(cand), casesContainingCand.get(0), getSudoku());
			}
		}
	}
}
