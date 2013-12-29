/**
 * 
 */
package fr.perso.sudoku.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.perso.sudoku.bean.Case;

/**
 * @author mkinengue<br />
 *         Classe de test des utilitaires
 */
public class SudokuUtilsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Vérifie que le liste des cases non vides en paramètre ne contient pas deux cases ayant la même valeur
	 * 
	 * @see SudokuUtils#verifyCases(fr.perso.sudoku.bean.Case[])
	 */
	@Test
	public void testVerifyCases() {
		final Case[] listeCases = new Case[5];
		listeCases[0] = new Case(0, 0, null, 5);
		listeCases[1] = new Case(0, 1, Integer.valueOf(1), 5);
		listeCases[2] = new Case(0, 2, Integer.valueOf(2), 5);
		listeCases[3] = new Case(0, 3, Integer.valueOf(5), 5);
		listeCases[4] = new Case(0, 4, null, 5);

		Assert.assertTrue(SudokuUtils.verifyCases(listeCases));
	}

	/**
	 * Vérifie que le liste des cases non vides en paramètre contient au moins deux cases ayant la même valeur
	 * 
	 * @see SudokuUtils#verifyCases(fr.perso.sudoku.bean.Case[])
	 */
	@Test
	public void testVerifyCases2() {
		final Case[] listeCases = new Case[5];
		listeCases[0] = new Case(0, 0, null, 5);
		listeCases[1] = new Case(0, 1, Integer.valueOf(1), 5);
		listeCases[2] = new Case(0, 2, Integer.valueOf(2), 5);
		listeCases[3] = new Case(0, 3, Integer.valueOf(5), 5);
		listeCases[4] = new Case(0, 4, Integer.valueOf(1), 5);

		Assert.assertTrue(!SudokuUtils.verifyCases(listeCases));
	}

	/**
	 * @see SudokuUtils#extractColumn(int, Case[][])
	 */
	@Test
	public void testExtractColumn() {
		final Case[][] grille = new Case[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				grille[i][j] = new Case(i, j, null, 4);
			}
		}

		final Case[] casesCol = SudokuUtils.extractColumn(1, grille);
		int nb = 0;
		for (final Case c : casesCol) {
			boolean v = (c.getRow() == 0 && c.getColumn() == 1) || (c.getRow() == 1 && c.getColumn() == 1);
			v = v || (c.getRow() == 2 && c.getColumn() == 1) || (c.getRow() == 3 && c.getColumn() == 1);
			Assert.assertTrue(v);
			nb++;
		}
		Assert.assertEquals(4, nb);
	}

	/**
	 * @see SudokuUtils#extractRow(int, Case[][])
	 */
	@Test
	public void testExtractRow() {
		final Case[][] grille = new Case[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				grille[i][j] = new Case(i, j, null, 4);
			}
		}

		final Case[] casesCol = SudokuUtils.extractRow(3, grille);
		int nb = 0;
		for (final Case c : casesCol) {
			boolean v = (c.getRow() == 3 && c.getColumn() == 0) || (c.getRow() == 3 && c.getColumn() == 1);
			v = v || (c.getRow() == 3 && c.getColumn() == 2) || (c.getRow() == 3 && c.getColumn() == 3);
			Assert.assertTrue(v);
			nb++;
		}
		Assert.assertEquals(4, nb);
	}

	/**
	 * @see SudokuUtils#getClasses(String)
	 */
	@Test
	public void testGetClasses() {
		final String packName = "fr.perso.sudoku.utils";
		final Class<?>[] classes = SudokuUtils.getClasses(packName);
		int nb = 0;
		for (final Class<?> c : classes) {
			if ("SudokuUtils".equals(c.getSimpleName()) || "SudokuUtilsTest".equals(c.getSimpleName())) {
				nb++;
			}
		}
		Assert.assertEquals(2, nb);
	}
}
