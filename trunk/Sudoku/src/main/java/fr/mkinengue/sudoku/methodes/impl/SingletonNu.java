package fr.mkinengue.sudoku.methodes.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.core.Sudoku;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.MethodeAbstract;
import fr.mkinengue.sudoku.utils.SudokuUtils;

/**
 * Méthode consistant à chercher toutes les cases vides de la grille n'ayant qu'un candidat dans sa liste de candidats.
 * Le candidat unique est alors affecté à la case vide et les listes des priorités mises à jour
 */
public class SingletonNu extends MethodeAbstract implements Methode {

	/** Logger spécifique de la classe */
	private static Logger LOG = Logger.getLogger(SingletonNu.class.getSimpleName());

	/**
	 * Constructeur initialisant la méthode en lui fournissant le Sudoku sur lequel elle s'applique
	 * 
	 * @param sudoku
	 */
	public SingletonNu(final Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		final long debut = System.currentTimeMillis();
		final Case[][] grille = getSudoku().getGrille();
		for (final Case[] cc : grille) {
			for (final Case c : cc) {
				if (c.isEmpty() && c.getCandidates().size() == 1) {
					SudokuUtils.updateCaseWithOneCandidate(c, getSudoku());
				}
			}
		}

		LOG.log(Level.INFO, "Méthode SingletonNu exécutée en {0} ms", System.currentTimeMillis() - debut);
	}
}
