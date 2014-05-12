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
 * Méthode consistant à rechercher dans une ligne, colonne ou région donnée la seule case vide afin de lui attribuer la
 * dernière valeur de la série
 */
public class Singleton extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(Singleton.class.getSimpleName());

	public Singleton(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		final long debut = System.currentTimeMillis();
		setSingletonFromRows();
		setSingletonFromColumns();
		setSingletonFromRegions();

		LOG.log(Level.INFO, "Méthode Singleton exécutée en {0} ms", System.currentTimeMillis() - debut);
	}

	/**
	 * Mets � jour l'unique case si elle existe pour chacune des lignes de la grille
	 */
	private void setSingletonFromRows() {
		final Case[][] grille = getSudoku().getGrille();
		for (int row = 0; row < grille.length; row++) {
			final Case[] rowCases = getSudoku().getCasesByRow(row);
			setSingleton(rowCases);
		}
	}

	/**
	 * Mets � jour l'unique case si elle existe pour chacune des colonnes de la grille
	 */
	private void setSingletonFromColumns() {
		final Case[][] grille = getSudoku().getGrille();
		for (int col = 0; col < grille.length; col++) {
			final Case[] columnCases = getSudoku().getCasesByColumn(col);
			setSingleton(columnCases);
		}
	}

	/**
	 * Mets � jour l'unique case si elle existe pour chacune des r�gions de la grille
	 */
	private void setSingletonFromRegions() {
		final Map<Case, Region> regionsByFirstCase = getSudoku().getRegionsByFirstCase();
		for (final Region region : regionsByFirstCase.values()) {
			final Case[] regionCases = region.getCases();
			setSingleton(regionCases);
		}
	}

	private Integer[] getAllCandidates() {
		final Integer[] candidates = new Integer[getSudoku().getGrille().length];
		for (int i = 1; i <= candidates.length; i++) {
			candidates[i - 1] = i;
		}
		return candidates;
	}

	/**
	 * Recherche et retourne l'unique case vide de cases<br />
	 * Si plusieurs cases vides sont trouv�es ou si aucune case vide n'est trouv�e, retourne null<br />
	 * 
	 * @param cases tableau de cases o� l'on recherche une case vide
	 * @param candidates liste de tous les candidats dont on vide un �l�ment trouv� dans un case non vide
	 * @return Case ou null
	 */
	private Case getUniqueEmptyCase(final Case[] cases, final Integer[] candidates) {
		boolean found = false;
		Case emptyCase = null;
		for (final Case case1 : cases) {
			if (case1.isEmpty()) {
				if (found) {
					emptyCase = null;
				} else {
					found = true;
					emptyCase = case1;
				}
			} else {
				candidates[case1.getValue() - 1] = null;
			}
		}

		return emptyCase;
	}

	/**
	 * Mets � jour l'unique case vide de rowCases � la seule valeur possible
	 * 
	 * @param rowCases liste des cases dont on cherche l'unique case vide
	 */
	private void setSingleton(final Case[] rowCases) {
		final Integer[] candidates = getAllCandidates();
		final Case uniqueEmptyCase = getUniqueEmptyCase(rowCases, candidates);
		if (uniqueEmptyCase == null) {
			// Aucune case vide ou au moins deux cases vides trouv�es
			return;
		}

		for (final Integer value : candidates) {
			if (value != null) {
				uniqueEmptyCase.setValue(value);

				// On remplit la map des occurrences des nombres
				getSudoku().updateMapOccurrencesByNumber(uniqueEmptyCase.getValue());

				// On supprime la case de la liste des cases vides
				getSudoku().getEmptyCases().remove(uniqueEmptyCase);
			}
		}
	}
}
