package fr.mkinengue.projetinfox.transposition.parser;

import junit.framework.TestCase;

import org.junit.Test;

import fr.mkinengue.projetinfox.transposition.parser.Parser;

/**
 * Classe de tests unitaires du parser
 */
public class ParserTest extends TestCase {

	private Parser myParser;

	@Override
	protected void setUp() {
		myParser = new Parser(10, null);
	}

	@Test
	public void testIsRegistre() {
		assertFalse("null n'est pas une instruction valide", Parser.isRegistre(null));
		assertFalse("'' n'est pas une instruction valide", Parser.isRegistre(""));
		assertFalse("' ' n'est pas une instruction valide", Parser.isRegistre(" "));

		for (int i = 1; i < 101; i++) {
			assertTrue("R" + i + " aurait dû être un registre", Parser.isRegistre("R" + i));
		}

		assertFalse("R 11 ne devrait pas être une registre", Parser.isRegistre("R 11"));
		assertFalse(" R11 ne devrait pas être une registre", Parser.isRegistre(" R11"));
		assertFalse("'R11 ' ne devrait pas être une registre", Parser.isRegistre("R11 "));
		assertFalse("R1 1 ne devrait pas être une registre", Parser.isRegistre("R1 1"));
	}

	@Test
	public void testIsAffectation() {
		assertFalse("null n'est pas une instruction valide", Parser.isAffectation(null));
		assertFalse("'' n'est pas une instruction valide", Parser.isAffectation(""));
		assertFalse("' ' n'est pas une instruction valide", Parser.isAffectation(" "));

		assertTrue("'R2 = 3' est une instruction valide", Parser.isAffectation("R2 = 3"));
		assertTrue("'R2= 3' est une instruction valide", Parser.isAffectation("R2= 3"));
		assertTrue("'R2= 3' est une instruction valide", Parser.isAffectation("R2 =3"));
		assertTrue("'R2= 3' est une instruction valide", Parser.isAffectation("R2=3"));

		assertTrue("'R2 = +3' est une instruction valide", Parser.isAffectation("R2 = +3"));
		assertTrue("'R2= +3' est une instruction valide", Parser.isAffectation("R2= +3"));
		assertTrue("'R2= +3' est une instruction valide", Parser.isAffectation("R2 =+3"));
		assertTrue("'R2= +3' est une instruction valide", Parser.isAffectation("R2=+3"));

		assertTrue("'R2 = + 3' est une instruction valide", Parser.isAffectation("R2 = + 3"));
		assertTrue("'R2= + 3' est une instruction valide", Parser.isAffectation("R2= + 3"));
		assertTrue("'R2= + 3' est une instruction valide", Parser.isAffectation("R2 =+ 3"));
		assertTrue("'R2= + 3' est une instruction valide", Parser.isAffectation("R2=+ 3"));

		assertTrue("'R2 = -3' est une instruction valide", Parser.isAffectation("R2 = -3"));
		assertTrue("'R2= -3' est une instruction valide", Parser.isAffectation("R2= -3"));
		assertTrue("'R2= -3' est une instruction valide", Parser.isAffectation("R2 =-3"));
		assertTrue("'R2= -3' est une instruction valide", Parser.isAffectation("R2=-3"));

		assertTrue("'R2 = - 3' est une instruction valide", Parser.isAffectation("R2 = - 3"));
		assertTrue("'R2= - 3' est une instruction valide", Parser.isAffectation("R2= - 3"));
		assertTrue("'R2= - 3' est une instruction valide", Parser.isAffectation("R2 =- 3"));
		assertTrue("'R2= - 3' est une instruction valide", Parser.isAffectation("R2=- 3"));
	}

	@Test
	public void testIsSomme() {
		assertFalse("null n'est pas une instruction valide", Parser.isSomme(null));
		assertFalse("'' n'est pas une instruction valide", Parser.isSomme(""));
		assertFalse("' ' n'est pas une instruction valide", Parser.isSomme(" "));
		assertFalse("'R2 = 3' n'est pas une somme", Parser.isSomme("R2 = 3"));
		assertFalse("'R2 = 3R4' n'est pas une somme", Parser.isSomme("R2 = 3R4"));

		assertTrue("'R2 = R1 + R43' est une instruction valide", Parser.isSomme("R2 = R1 + R43"));
		assertTrue("'R2 = R1 - R4003' est une instruction valide", Parser.isSomme("R2 = R1 - R4003"));
		assertTrue("'R2 = + R1 + R43' est une instruction valide", Parser.isSomme("R2 = + R1 + R43"));
		assertTrue("'R2 = + R1 - R43' est une instruction valide", Parser.isSomme("R2 = + R1 - R43"));
		assertTrue("'R2 = - R1 + R4003' est une instruction valide", Parser.isSomme("R2 = - R1 + R4003"));
		assertTrue("'R2 = - R1 - R4003' est une instruction valide", Parser.isSomme("R2 = - R1 - R4003"));

		assertTrue("'R2=R1+R43' est une instruction valide", Parser.isSomme("R2=R1+R43"));
		assertTrue("'R2=R1-R4003' est une instruction valide", Parser.isSomme("R2=R1-R4003"));
		assertTrue("'R2=+R1+R43' est une instruction valide", Parser.isSomme("R2=+R1+R43"));
		assertTrue("'R2=+R1-R43' est une instruction valide", Parser.isSomme("R2=+R1-R43"));
		assertTrue("'R2=-R1+R4003' est une instruction valide", Parser.isSomme("R2=-R1+R4003"));
		assertTrue("'R2=-R1-R4003' est une instruction valide", Parser.isSomme("R2=-R1-R4003"));
	}

	@Test
	public void testIsMultiplication() {
		assertFalse("null n'est pas une instruction valide", Parser.isMultiplication(null));
		assertFalse("'' n'est pas une instruction valide", Parser.isMultiplication(""));
		assertFalse("' ' n'est pas une instruction valide", Parser.isMultiplication(" "));
		assertFalse("'R2 = 3' n'est pas une somme", Parser.isMultiplication("R2 = 3"));
		assertFalse("'R2=R1+R43' n'est pas une instruction valide", Parser.isMultiplication("R2=R1+R43"));
		assertFalse("'R2 = 34  R67' n'est pas une instruction valide", Parser.isMultiplication("R2 = 34  R67"));

		assertTrue("'R2 = 34R67' est une instruction valide", Parser.isMultiplication("R2 = 34R67"));
		assertTrue("'R2 = 34 R67' est une instruction valide", Parser.isMultiplication("R2 = 34 R67"));
		assertTrue("'R2 = 34*R67' est une instruction valide", Parser.isMultiplication("R2 = 34*R67"));
		assertTrue("'R2 = 34.R67' est une instruction valide", Parser.isMultiplication("R2 = 34.R67"));
	}

	@Test
	public void testGetIndexRegistre() {
		assertEquals("L'index attendu est -1", -1, Parser.getIndexRegistre(null));
		assertEquals("L'index attendu est -1", -1, Parser.getIndexRegistre(""));
		assertEquals("L'index attendu est -1", -1, Parser.getIndexRegistre(" "));

		for (int i = 1; i <= 1024; i++) {
			assertEquals("L'index attendu est " + (i - 1), i - 1, Parser.getIndexRegistre("R" + i));
		}
	}

	@Test
	public void testGetRegistreValue() {
		for (int i = 0; i >= -20; i--) {
			assertEquals("-1 était la valeur attendue", -1, myParser.getRegistreValue(i));
		}
		for (int i = 11; i <= 30; i++) {
			assertEquals("-1 était la valeur attendue", -1, myParser.getRegistreValue(i));
		}
		for (int i = 1; i <= 10; i++) {
			assertEquals("0 était la valeur attendue", 0, myParser.getRegistreValue(i));
		}
	}

	@Test
	public void testExecuteAffectation() {
		String instruction = "R2 = 3";
		assertTrue("L'instruction '" + instruction + "' aurait dû s'exécuter", myParser.execute(instruction));
		assertEquals("La valeur attendue pour le registre R2 est 3", 3, myParser.getRegistreValue(2));

		instruction = "R2 = + 4";
		assertTrue("L'instruction '" + instruction + "' aurait dû s'exécuter", myParser.execute(instruction));
		assertEquals("La valeur attendue pour le registre R2 est 4", 4, myParser.getRegistreValue(2));

		instruction = "R2 = +5";
		assertTrue("L'instruction '" + instruction + "' aurait dû s'exécuter", myParser.execute(instruction));
		assertEquals("La valeur attendue pour le registre R2 est 5", 5, myParser.getRegistreValue(2));

		instruction = "R12 = +5";
		assertFalse("L'instruction '" + instruction + "' n'aurait pas dû s'exécuter car registre inexistant",
				myParser.execute(instruction));

		instruction = "R10 = -7";
		assertTrue("L'instruction '" + instruction + "' aurait dû s'exécuter", myParser.execute(instruction));
		assertEquals("La valeur attendue pour le registre R10 est -7", -7, myParser.getRegistreValue(10));

		instruction = "R9 = - 9";
		assertTrue("L'instruction '" + instruction + "' aurait dû s'exécuter", myParser.execute(instruction));
		assertEquals("La valeur attendue pour le registre R9 est -9", -9, myParser.getRegistreValue(9));
	}
}
