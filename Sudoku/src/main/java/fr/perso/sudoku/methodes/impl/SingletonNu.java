package fr.perso.sudoku.methodes.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.perso.sudoku.bean.Case;
import fr.perso.sudoku.core.Sudoku;
import fr.perso.sudoku.methodes.Methode;
import fr.perso.sudoku.methodes.MethodeAbstract;

/**
 * M�thode consistant � chercher toutes les cases de la grille vides et n'ayant qu'un candidat dans sa liste de
 * candidats. Le candidat unique est alors affect� � la case vide
 */
public class SingletonNu extends MethodeAbstract implements Methode {

	private static Logger LOG = Logger.getLogger(SingletonNu.class.getSimpleName());

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
					c.setValue(c.getCandidates().get(0).intValue());

					// On remplit la map des occurrences des nombres
					getSudoku().updateMapOccurrencesByNumber(c.getValue());

					// On supprime la case de la liste des cases vides
					getSudoku().getEmptyCases().remove(c);
				}
			}
		}

		LOG.log(Level.INFO, "Méthode SingletonNu exécutée en {0} ms", System.currentTimeMillis() - debut);
	}

}
