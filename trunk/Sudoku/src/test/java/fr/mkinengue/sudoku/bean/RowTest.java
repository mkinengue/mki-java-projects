/**
 * 
 */
package fr.mkinengue.sudoku.bean;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Row;

/**
 * @author mkinengue<br />
 *         Classe de test de Row
 */
public class RowTest {

	private Row row;

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
		row = new Row(Constantes.SIZE, Constantes.INDEX);
	}

	/**
	 * Teste le premier constructeur de Row
	 * 
	 * @see Row#Row(int, int)
	 */
	@Test
	public void testRow() {
		Assert.assertNotNull(row);
		Assert.assertEquals(Constantes.INDEX, row.getIndex());
		Assert.assertNotNull(row.getRow());
		Assert.assertEquals(Constantes.SIZE, row.getRow().length);
		for (int i = 0; i < Constantes.SIZE; i++) {
			Assert.assertNull(row.getRow()[i]);
		}
		Assert.assertTrue(row.isRowType());
	}

	/**
	 * Teste le second constructeur de Row
	 * 
	 * @see Row#Row(Case[], int)
	 */
	@Test
	public void testRow2() {
		final Case[] cc = getArrayCase(Constantes.SIZE, Constantes.INDEX);
		final Row row = new Row(cc, Constantes.INDEX);
		Assert.assertEquals(Constantes.INDEX, row.getIndex());
		Assert.assertNotNull(row.getRow());
		for (final Case c : row.getRow()) {
			boolean found = false;
			for (final Case c2 : cc) {
				if (c2.equals(c)) {
					found = true;
				}
			}
			Assert.assertTrue(found);
		}
		Assert.assertTrue(row.isRowType());
	}

	/**
	 * Teste le getter getRow
	 * 
	 * @see Row#getRow()
	 */
	@Test
	public void testGetRow() {
		Assert.assertNotNull(row);
		Assert.assertNotNull(row.getRow());
		Assert.assertEquals(Constantes.SIZE, row.getRow().length);
		for (int i = 0; i < Constantes.SIZE; i++) {
			Assert.assertNull(row.getRow()[i]);
		}
	}

	/**
	 * Teste le getter getRow
	 * 
	 * @see Row#getRow()
	 */
	@Test
	public void testGetRow2() {
		final Case[] cc = getArrayCase(Constantes.SIZE, Constantes.INDEX);
		final Row row = new Row(cc, Constantes.INDEX);
		Assert.assertNotNull(row.getRow());
		for (final Case c : row.getRow()) {
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
	 * Teste le getter isRowType
	 * 
	 * @see Row#isRowType()
	 */
	@Test
	public void testIsRowType() {
		Assert.assertTrue(row.isRowType());
	}

	/**
	 * Retourne un tableau de cases représentant une colonne d'index indexRow et de taille size
	 * 
	 * @param size taille de la colonne
	 * @param indexRow index de la colonne
	 * @return Case[]
	 */
	private Case[] getArrayCase(final int size, final int indexRow) {
		final Case[] cc = new Case[size];
		for (int i = 0; i < size; i++) {
			cc[i] = new Case(i, indexRow, null, size);
		}
		return cc;
	}
}
