/**
 * 
 */
package fr.mkinengue.sudoku.bean;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Column;

/**
 * @author mkinengue<br />
 *         Classe de test de Column
 */
public class ColumnTest {

	private Column col;

	/**
	 * Constantes
	 */
	private interface Constantes {
		static int SIZE = 9;
		static int INDEX = 4;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		col = new Column(Constantes.SIZE, Constantes.INDEX);
	}

	/**
	 * Teste le premier constructeur de Column
	 * 
	 * @see Column#Column(int, int)
	 */
	@Test
	public void testColumn() {
		Assert.assertNotNull(col);
		Assert.assertEquals(Constantes.INDEX, col.getIndex());
		Assert.assertNotNull(col.getColumn());
		Assert.assertEquals(Constantes.SIZE, col.getColumn().length);
		for (int i = 0; i < Constantes.SIZE; i++) {
			Assert.assertNull(col.getColumn()[i]);
		}
		Assert.assertTrue(col.isColumnType());
	}

	/**
	 * Teste le second constructeur de Column
	 * 
	 * @see Column#Column(Case[], int)
	 */
	@Test
	public void testColumn2() {
		final Case[] cc = getArrayCase(Constantes.SIZE, Constantes.INDEX);
		final Column col = new Column(cc, Constantes.INDEX);
		Assert.assertEquals(Constantes.INDEX, col.getIndex());
		Assert.assertNotNull(col.getColumn());
		for (final Case c : col.getColumn()) {
			boolean found = false;
			for (final Case c2 : cc) {
				if (c2.equals(c)) {
					found = true;
				}
			}
			Assert.assertTrue(found);
		}
		Assert.assertTrue(col.isColumnType());
	}

	/**
	 * Teste le getter getColumn
	 * 
	 * @see Column#getColumn()
	 */
	@Test
	public void testGetColumn() {
		Assert.assertNotNull(col);
		Assert.assertNotNull(col.getColumn());
		Assert.assertEquals(Constantes.SIZE, col.getColumn().length);
		for (int i = 0; i < Constantes.SIZE; i++) {
			Assert.assertNull(col.getColumn()[i]);
		}
	}

	/**
	 * Teste le getter getColumn
	 * 
	 * @see Column#getColumn()
	 */
	@Test
	public void testGetColumn2() {
		final Case[] cc = getArrayCase(Constantes.SIZE, Constantes.INDEX);
		final Column col = new Column(cc, Constantes.INDEX);
		Assert.assertNotNull(col.getColumn());
		for (final Case c : col.getColumn()) {
			boolean found = false;
			for (final Case c2 : cc) {
				if (c2.equals(c)) {
					found = true;
				}
			}
			Assert.assertTrue(found);
		}
	}

	/**
	 * Teste le getter isColumnType
	 * 
	 * @see Column#isColumnType()
	 */
	@Test
	public void testIsColumnType() {
		Assert.assertTrue(col.isColumnType());
	}

	/**
	 * Retourne un tableau de cases représentant une colonne d'index indexCol et de taille size
	 * 
	 * @param size taille de la colonne
	 * @param indexCol index de la colonne
	 * @return Case[]
	 */
	private Case[] getArrayCase(final int size, final int indexCol) {
		final Case[] cc = new Case[size];
		for (int i = 0; i < size; i++) {
			cc[i] = new Case(i, indexCol, null, size);
		}
		return cc;
	}
}
