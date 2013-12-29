/**
 * 
 */
package fr.perso.sudoku.bean;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mkinengue<br />
 *         Classe de test du bean Case
 */
public class CaseTest {

	private Case c;

	/**
	 * Constantes pour les tests
	 */
	private interface Constantes {
		static int ROW = 0;
		static int COL = 0;
		static Integer VAL = 2;
		static int MAX_VAL = 9;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		c = new Case(Constantes.ROW, Constantes.COL, Constantes.VAL, Constantes.MAX_VAL);
	}

	/**
	 * Teste le constructeur d'une case avec aucune valeur dans la case
	 * 
	 * @see Case#Case(int, int, Integer, int)
	 */
	@Test
	public void testCaseEmpty() {
		final Case c = new Case(Constantes.ROW, Constantes.COL, null, Constantes.MAX_VAL);
		Assert.assertNotNull("La case aurait dû être initialisée", c);
		Assert.assertNotNull("La liste des candidats n'a pas été initialisée", c.getCandidates());
		Assert.assertTrue("La liste des candidats n'aurait pas dû être vide", !c.getCandidates().isEmpty());
		Assert.assertEquals(Constantes.MAX_VAL, c.getCandidates().size());
		Assert.assertEquals(Constantes.ROW, c.getRow());
		Assert.assertEquals(Constantes.COL, c.getColumn());
		Assert.assertNull("La valeur de la case aurait dû être nulle", c.getValue());
		Assert.assertEquals(Constantes.MAX_VAL, c.getMaxValue());
	}

	/**
	 * Teste le constructeur d'une case avec une valeur dans la case
	 * 
	 * @see Case#Case(int, int, Integer, int)
	 */
	@Test
	public void testCaseNotEmpty() {
		Assert.assertNotNull("La case aurait dû être initialisée", c);
		Assert.assertNotNull("La liste des candidats n'a pas été initialisée", c.getCandidates());
		Assert.assertTrue("La liste des candidats aurait dû être vide", c.getCandidates().isEmpty());
		Assert.assertEquals(Constantes.ROW, c.getRow());
		Assert.assertEquals(Constantes.COL, c.getColumn());
		Assert.assertEquals(Constantes.VAL, c.getValue());
		Assert.assertEquals(Constantes.MAX_VAL, c.getMaxValue());
	}

	/**
	 * Teste la liste des candidats dans le cas où elle est vide
	 * 
	 * @see Case#getCandidates()
	 */
	@Test
	public void testGetCandidatesEmpty() {
		Assert.assertNotNull("La liste des candidats n'a pas été initialisée", c.getCandidates());
		Assert.assertTrue("La liste des candidats aurait dû être vide", c.getCandidates().isEmpty());
	}

	/**
	 * Teste la liste des candidats dans le cas où elle est vide
	 * 
	 * @see Case#getCandidates()
	 */
	@Test
	public void testGetCandidatesNotEmpty() {
		final Case c = new Case(Constantes.ROW, Constantes.COL, null, Constantes.MAX_VAL);
		Assert.assertNotNull("La liste des candidats n'a pas été initialisée", c.getCandidates());
		Assert.assertTrue("La liste des candidats aurait dû être vide", !c.getCandidates().isEmpty());
		Assert.assertEquals(Constantes.MAX_VAL, c.getMaxValue());
	}

	/**
	 * Teste la récupération de la valeur de la ligne de la case
	 * 
	 * @see Case#getRow()
	 */
	@Test
	public void testGetRow() {
		Assert.assertEquals(Constantes.ROW, c.getRow());
	}

	/**
	 * Teste la récupération de la valeur de la colonne de la case
	 * 
	 * @see Case#getColumn()
	 */
	@Test
	public void testGetColumn() {
		Assert.assertEquals(Constantes.COL, c.getColumn());
	}

	/**
	 * Teste la récupération de la valeur de la case
	 * 
	 * @see Case#getValue()
	 */
	@Test
	public void testGetValue() {
		Assert.assertEquals(Constantes.VAL, c.getValue());
	}

	/**
	 * Teste la récupération de la valeur maximum de la case
	 * 
	 * @see Case#getMaxValue()
	 */
	@Test
	public void testGetMaxValue() {
		Assert.assertEquals(Constantes.MAX_VAL, c.getMaxValue());
	}

	/**
	 * Teste que la case n'est pas vide
	 * 
	 * @see Case#isEmpty()
	 */
	@Test
	public void testIsNotEmpty() {
		Assert.assertTrue(!c.isEmpty());
	}

	/**
	 * Teste que la case est pas vide
	 * 
	 * @see Case#isEmpty()
	 */
	@Test
	public void testIsEmpty() {
		final Case c = new Case(Constantes.ROW, Constantes.COL, null, Constantes.MAX_VAL);
		Assert.assertTrue(c.isEmpty());
	}

	/**
	 * Teste la suppression d'un candidat de la liste
	 * 
	 * @see Case#isEmpty()
	 */
	@Test
	public void testRemoveCandidates() {
		final Case c = new Case(Constantes.ROW, Constantes.COL, null, Constantes.MAX_VAL);
		Assert.assertNotNull("La liste des candidats n'a pas été initialisée", c.getCandidates());
		Assert.assertTrue("La liste des candidats aurait dû être vide", !c.getCandidates().isEmpty());
		final Integer val = Integer.valueOf(6);
		Assert.assertTrue(c.getCandidates().contains(val));
		c.removeCandidate(val);
		Assert.assertTrue(!c.getCandidates().contains(val));
	}

	/**
	 * Teste l'égalité de cases
	 * 
	 * @see Case#equals(Object)
	 */
	@Test
	public void testEquals() {
		Case c2 = new Case(Constantes.ROW, Constantes.COL, null, Constantes.MAX_VAL);
		Assert.assertEquals(c, c2);

		c2 = new Case(Constantes.ROW, Constantes.COL, Constantes.VAL, Constantes.MAX_VAL);
		Assert.assertEquals(c, c2);

		c2 = new Case(Constantes.ROW, Constantes.COL, Constantes.VAL, Constantes.MAX_VAL);
		Assert.assertEquals(c, c2);

		c2 = new Case(Constantes.ROW, Constantes.COL, Constantes.VAL, Constantes.MAX_VAL + 1);
		Assert.assertEquals(c, c2);

		c2 = new Case(Constantes.ROW + 1, Constantes.COL, Constantes.VAL, Constantes.MAX_VAL);
		Assert.assertFalse(c.equals(c2));

		c2 = new Case(Constantes.ROW, Constantes.COL + 1, Constantes.VAL, Constantes.MAX_VAL);
		Assert.assertFalse(c.equals(c2));

		Assert.assertFalse(c.equals(Integer.valueOf(3)));

		Assert.assertFalse(c.equals(null));
	}
}