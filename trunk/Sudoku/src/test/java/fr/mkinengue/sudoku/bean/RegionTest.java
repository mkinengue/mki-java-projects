/**
 * 
 */
package fr.mkinengue.sudoku.bean;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.exception.NotValidException;

/**
 * @author mkinengue<br />
 *         Teste de la construction de la région
 */
public class RegionTest {

	private Region region;

	/**
	 * Constantes de la classe
	 */
	private interface Constantes {
		static int VAL = 4;
		static int SIZE = 9;
		static Case CASE_30 = new Case(3, 0, VAL, SIZE);
		static Case CASE_31 = new Case(3, 1, null, SIZE);
		static Case CASE_32 = new Case(3, 2, null, SIZE);
		static Case CASE_40 = new Case(4, 0, null, SIZE);
		static Case CASE_41 = new Case(4, 1, null, SIZE);
		static Case CASE_42 = new Case(4, 2, null, SIZE);
		static Case CASE_50 = new Case(5, 0, null, SIZE);
		static Case CASE_51 = new Case(5, 1, null, SIZE);
		static Case CASE_52 = new Case(5, 2, null, SIZE);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		region = new Region(Constantes.SIZE, Constantes.CASE_30);
		buildRegion(region);
	}

	/**
	 * Construit la région en ajoutant les cases correspondantes
	 * 
	 * @param Region reg : région
	 */
	private void buildRegion(final Region reg) {
		reg.addCase(Constantes.CASE_30);
		reg.addCase(Constantes.CASE_31);
		reg.addCase(Constantes.CASE_32);
		reg.addCase(Constantes.CASE_40);
		reg.addCase(Constantes.CASE_41);
		reg.addCase(Constantes.CASE_42);
		reg.addCase(Constantes.CASE_50);
		reg.addCase(Constantes.CASE_51);
		reg.addCase(Constantes.CASE_52);
	}

	/**
	 * @see Region#Region(int, Case)
	 */
	@Test
	public void testRegion() {
		Assert.assertNotNull(region);
		Assert.assertEquals(Constantes.CASE_30, region.getFirstCase());
		Assert.assertNotNull(region.getCases());
		for (final Case c : region.getCases()) {
			Assert.assertNotNull(c);
		}
	}

	/**
	 * @see Region#addCase(Case)
	 */
	@Test
	public void testAddCase() {
		final Region reg = new Region(Constantes.SIZE, Constantes.CASE_30);
		Assert.assertNotNull(reg);
		for (final Case c : reg.getCases()) {
			Assert.assertNull(c);
		}

		Assert.assertTrue(reg.addCase(Constantes.CASE_30));
		for (final Case c : reg.getCases()) {
			if (c != null) {
				Assert.assertEquals(Constantes.CASE_30, c);
				continue;
			}
			Assert.assertNull(c);
		}

		Assert.assertTrue(reg.addCase(Constantes.CASE_41));
		int nbMatches = 0;
		for (final Case c : reg.getCases()) {
			if (c != null) {
				Assert.assertTrue(Constantes.CASE_30.equals(c) || Constantes.CASE_41.equals(c));
				nbMatches++;
				continue;
			}
			Assert.assertNull(c);
		}
		Assert.assertEquals(2, nbMatches);

		// Ajout impossible dans une région pleine
		Assert.assertFalse(region.addCase(Constantes.CASE_30));
	}

	/**
	 * @see Region#contains(Case)
	 */
	@Test
	public void testContains() {
		Assert.assertTrue(region.contains(Constantes.CASE_41));
		Assert.assertTrue(region.contains(Constantes.CASE_50));
		Assert.assertTrue(region.contains(Constantes.CASE_32));
		Assert.assertTrue(region.contains(Constantes.CASE_30));
	}

	/**
	 * @see Region#getCases()
	 */
	@Test
	public void testGetCases() {
		Assert.assertNotNull(region.getCases());
		Assert.assertEquals(Constantes.SIZE, region.getCases().length);
	}

	/**
	 * @see Region#getCasesByRow(int)
	 */
	@Test
	public void testGetCasesByRow() {
		final List<Case> cases0 = region.getCasesByRow(3);
		int nbMatches = 0;
		for (final Case c : cases0) {
			Assert.assertTrue(Constantes.CASE_30.equals(c) || Constantes.CASE_31.equals(c)
					|| Constantes.CASE_32.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);

		final List<Case> cases1 = region.getCasesByRow(4);
		nbMatches = 0;
		for (final Case c : cases1) {
			Assert.assertTrue(Constantes.CASE_40.equals(c) || Constantes.CASE_41.equals(c)
					|| Constantes.CASE_42.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);

		final List<Case> cases2 = region.getCasesByRow(5);
		nbMatches = 0;
		for (final Case c : cases2) {
			Assert.assertTrue(Constantes.CASE_50.equals(c) || Constantes.CASE_51.equals(c)
					|| Constantes.CASE_52.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);
	}

	/**
	 * @see Region#getCasesByColumn(int)
	 */
	@Test
	public void testGetCasesByColumn() {
		final List<Case> cases0 = region.getCasesByColumn(0);
		int nbMatches = 0;
		for (final Case c : cases0) {
			Assert.assertTrue(Constantes.CASE_30.equals(c) || Constantes.CASE_40.equals(c)
					|| Constantes.CASE_50.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);

		final List<Case> cases1 = region.getCasesByColumn(1);
		nbMatches = 0;
		for (final Case c : cases1) {
			Assert.assertTrue(Constantes.CASE_31.equals(c) || Constantes.CASE_41.equals(c)
					|| Constantes.CASE_51.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);

		final List<Case> cases2 = region.getCasesByColumn(2);
		nbMatches = 0;
		for (final Case c : cases2) {
			Assert.assertTrue(Constantes.CASE_32.equals(c) || Constantes.CASE_42.equals(c)
					|| Constantes.CASE_52.equals(c));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);
	}

	/**
	 * @see Region#getColumns()
	 */
	@Test
	public void testGetColumns() {
		final List<Integer> columns = region.getColumns();
		int nbMatches = 0;
		for (final Integer idxCol : columns) {
			Assert.assertTrue((idxCol.intValue() == 0) || (idxCol.intValue() == 1) || (idxCol.intValue() == 2));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);
	}

	/**
	 * @see Region#getColumns()
	 */
	@Test
	public void testGetRows() {
		final List<Integer> rows = region.getRows();
		int nbMatches = 0;
		for (final Integer idxRow : rows) {
			Assert.assertTrue((idxRow.intValue() == 3) || (idxRow.intValue() == 4) || (idxRow.intValue() == 5));
			nbMatches++;
		}
		Assert.assertEquals(3, nbMatches);
	}

	/**
	 * @see Region#getFirstCase()
	 */
	@Test
	public void testGetFirstCase() {
		Assert.assertEquals(Constantes.CASE_30, region.getFirstCase());
	}

	/**
	 * @see Region#getFirstCase()
	 */
	@Test
	public void testGetSize() {
		Assert.assertEquals(Constantes.SIZE, region.getSize());
	}

	/**
	 * @see Region#isComplete()
	 */
	@Test
	public void testIsComplete() {
		Assert.assertTrue(region.isComplete());

		final Region reg = new Region(Constantes.SIZE, Constantes.CASE_30);
		reg.addCase(Constantes.CASE_30);
		reg.addCase(Constantes.CASE_31);
		reg.addCase(Constantes.CASE_32);

		Assert.assertTrue(!region.isComplete());
	}

	/**
	 * @see Region#isValid()
	 */
	@Test
	public void testIsValid() {
		Assert.assertTrue(region.isValid());

		final Region reg = new Region(Constantes.SIZE, Constantes.CASE_30);
		Assert.assertTrue(reg.addCase(Constantes.CASE_30));
		Assert.assertTrue(reg.addCase(Constantes.CASE_31));
		Assert.assertTrue(reg.addCase(Constantes.CASE_32));
		Assert.assertTrue(reg.addCase(Constantes.CASE_40));
		Assert.assertTrue(reg.addCase(Constantes.CASE_41));
		Assert.assertTrue(reg.addCase(Constantes.CASE_42));
		Assert.assertTrue(reg.addCase(Constantes.CASE_50));
		Assert.assertTrue(reg.addCase(Constantes.CASE_51));
		Assert.assertTrue(reg.addCase(new Case(6, 0, null, Constantes.SIZE)));
		try {
			reg.isValid();
			Assert.fail("Une exception aurait dû être levée");
		} catch (final NotValidException e) {
			Assert.assertTrue(true);
		}
	}
}
