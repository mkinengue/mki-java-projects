package fr.mkinengue.projetinfox.transposition.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mkinengue.projetinfox.transposition.tools.NumberUtils;

/**
 * Parser de fichiers lisant des additions, multiplications, soustractions et divisions sous la forme Ri = +/- Rj +/- Rk
 * où 1 <= i,j,k <= M, avec M le nombre total de registres et, Ri = aRj où 1 <= i,j <= M et "a" dans un corps "k", ici
 * l'ensemble Z/pZ <br />
 * Les fichiers à parser contiennent aussi des affectations de nombre telles que Ri = a où 1 <= i <= M et "a" dans un
 * corps "k", ici l'ensemble Z/pZ <br />
 * Si un registre donné n'est pas initialisé spécifiquement dans le fichier, celui-ci sera initialisé automatiquement à
 * zéro
 */
public class Parser {

	private static final Logger LOG = Logger.getLogger(Parser.class.getSimpleName());

	private static final char REGISTRE = 'R';

	private int nbTotalReg;

	private String pathFile;

	private double[] registres;

	/**
	 * Retourne les opérateurs utilisés
	 */
	private interface Operateur {
		String EGAL = "=";
		String PLUS = "+";
		String MOINS = "-";
	}

	/**
	 * Expressions régulières pour gérer les instructions
	 */
	private interface Regexp {
		/** Ri */
		String REG = "([+|-]{0,1}([\\s]*)" + REGISTRE + "[1-9]{1})([0-9]*)";

		/** Ri = k */
		String AFFECTATION = "(" + REGISTRE + "[1-9]{1})([0-9]*)([\\s]*)(=)([\\s]*)([+|-]{0,1})([\\s]*)([0-9]+)";

		/** Ri = +/- Rj +/- Rk */
		String SOMME = "(" + REGISTRE + "[1-9]{1})([0-9]*)([\\s]*)(=)([\\s]*)([+|-]{0,1})([\\s]*)(" + REGISTRE
				+ "[1-9]{1})([0-9]*)([\\s]*)([+|-]{1})([\\s]*)(" + REGISTRE + "[1-9]{1})([0-9]*)";

		/** Ri = aRj */
		String MULTIPLICATION = "("
				+ REGISTRE
				+ "[1-9]{1})([0-9]*)([\\s]*)(=)([\\s]*)([+|-]{0,1})([\\s]*)([0-9]*|[0-9]+[,|\\.|/][0-9]+)([\\s|\\*|\\.]{0,1})("
				+ REGISTRE + "[1-9]{1})([0-9]*)";
	}

	/**
	 * Constructeur initialisant les nbReg registres et récupère le chemin du fichier d'instructions des registres
	 * 
	 * @param nbReg
	 * @param cheminFichier
	 */
	public Parser(int nbReg, String cheminFichier) {
		nbTotalReg = nbReg;
		pathFile = cheminFichier;
		registres = new double[nbReg];
	}

	/**
	 * Retourne la valeur contenue dans le registre numéro numeroRegistre (variant de 1 à nbTotalReg)
	 * 
	 * @param numeroRegistre
	 * @return int
	 */
	public double getRegistreValue(int numeroRegistre) {
		if ((numeroRegistre <= 0) || (numeroRegistre > nbTotalReg)) {
			return -1d;
		}
		return registres[numeroRegistre - 1];
	}

	/**
	 * Parse le fichier d'instructions et les exécute lorsqu'elles sont reconnues
	 */
	public void parse() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(pathFile));
			while (scanner.hasNextLine()) {
				String instruction = scanner.nextLine();
				execute(instruction);
			}
		} catch (FileNotFoundException e) {
			LOG.log(Level.SEVERE, "Le fichier " + pathFile + " n'a pas été trouvé", e);
			throw new RuntimeException(e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * Exécute une instruction de type registre et retourne true si l'instruction a été exécutée<br />
	 * Si l'instruction ne peut être exécutée ou n'est pas reconnue, retourne false<br />
	 * 
	 * @param instruction
	 * @return true / false
	 * @throws RuntimeException
	 */
	public boolean execute(String instruction) throws RuntimeException {
		if (isAffectation(instruction)) {
			return executeAffectation(instruction);
		} else if (isSomme(instruction)) {
			return executeSomme(instruction);
		} else if (isMultiplication(instruction)) {
			return executeMultiplication(instruction);
		}
		return false;
	}

	/**
	 * Retourne true/false si le paramètre "registre" est
	 * 
	 * @param registre
	 * @return true/false
	 */
	public static boolean isRegistre(String registre) {
		return (registre != null) && registre.matches(Regexp.REG);
	}

	/**
	 * Retourne le numéro de registre du registre donné en argument<br />
	 * Retourne 0 s'il ne s'agit pas d'un registre
	 * 
	 * @param registre
	 * @return int
	 */
	private static int getNumeroRegistre(String registre) {
		if (!isRegistre(registre)) {
			return 0;
		}
		String[] splitReg = registre.split(Character.toString(REGISTRE));
		return Integer.parseInt(splitReg[1]);
	}

	/**
	 * Retourne l'index correspondant au registre donné en argument<br />
	 * Retourne -1 s'il ne s'agit pas d'un registre
	 * 
	 * @param registre
	 * @return int
	 */
	public static int getIndexRegistre(String registre) {
		return getNumeroRegistre(registre) - 1;
	}

	/**
	 * Exécute une instruction de type affectation tant que le registre à affecter est valide
	 * 
	 * @param instrAffectation
	 * @return true/false
	 */
	private boolean executeAffectation(String instrAffectation) {
		String[] split = instrAffectation.split(Operateur.EGAL);
		String registre = split[0].trim();
		int idxReg = getIndexRegistre(registre);
		if (idxReg >= nbTotalReg) {
			return false;
		}

		double val = 0d;
		String affectInstr = split[1].trim();

		if (affectInstr.startsWith(Operateur.MOINS)) {
			val = -Double.parseDouble(affectInstr.substring(1).trim());
		} else if (affectInstr.startsWith(Operateur.PLUS)) {
			val = Double.parseDouble(affectInstr.substring(1).trim());
		} else {
			val = Double.parseDouble(affectInstr.trim());
		}

		// Valorisation du registre dans le tableau des registres
		registres[idxReg] = val;
		return true;
	}

	/**
	 * Exécute une instruction de type somme "Ri = +/-Rj+/-Rk"
	 * 
	 * @param instrSomme
	 * @return true / false
	 */
	private boolean executeSomme(String instrSomme) {
		String[] split = instrSomme.split(Operateur.EGAL);
		String regAAffecter = split[0].trim();
		int idxReg = getIndexRegistre(regAAffecter);
		if (idxReg >= nbTotalReg) {
			return false;
		}

		// +/-Rj+/-Rk
		String somme = split[1].trim();
		Pattern p = Pattern.compile(Regexp.REG);
		Matcher matcher = p.matcher(somme);
		double val = 0d;
		while (matcher.find()) {
			// On récupère le registre de la somme (avec son éventuelk signe)
			String reg = matcher.group().trim();
			double signe = 1d;
			if (reg.startsWith(Operateur.MOINS)) {
				signe = -1d;
			}
			val = NumberUtils.add(val, NumberUtils.multiply(signe, getRegistreValue(getNumeroRegistre(reg))));
		}

		// Valorisation du registre dans le tableau des registres
		registres[idxReg] = val;
		return true;
	}

	/**
	 * Exécute une instruction de type somme "Ri = +/-aRj"
	 * 
	 * @param instrMultiplication
	 * @return true / false
	 */
	private boolean executeMultiplication(String instrMultiplication) {
		String[] split = instrMultiplication.split(Operateur.EGAL);
		String regAAffecter = split[0].trim();
		int idxReg = getIndexRegistre(regAAffecter);
		if (idxReg >= nbTotalReg) {
			return false;
		}

		// +/-aRj
		String multiplication = split[1].trim();
		double signe = 1d;
		if (multiplication.startsWith(Operateur.MOINS)) {
			signe = -1d;
			multiplication = multiplication.substring(1);
		} else if (multiplication.startsWith(Operateur.PLUS)) {
			multiplication = multiplication.substring(1).trim();
		}

		// on multiplie par le scalaire
		double val = NumberUtils.multiply(signe, getScalaire(multiplication));

		// On multiplie par la valeur du registre
		String[] regSplit = multiplication.split(Character.toString(REGISTRE));
		int numeroReg = Integer.parseInt(regSplit[1]);
		val = NumberUtils.multiply(val, getRegistreValue(numeroReg));

		// Valorisation du registre dans le tableau des registres
		registres[idxReg] = val;
		return true;
	}

	/**
	 * Retourne la valeur du scalaire de la multiplications
	 * 
	 * @param multiplication
	 * @return double
	 */
	private double getScalaire(String multiplication) {
		boolean regFound = false;
		double scalaire = 1d;
		String dividende = null;
		StringBuilder scal = new StringBuilder();
		for (int i = 0; i < multiplication.length() && !regFound; i++) {
			Character c = multiplication.charAt(i);
			if (Character.isDigit(c)) {
				scal.append(c.toString());
			} else if ((c == '.') || (c == ',')) {
				scal.append(".");
			} else if (c == '/') {
				dividende = scal.toString();
				scal.delete(0, scal.length());
			} else if (c == REGISTRE) {
				regFound = true;
			}
		}

		if (dividende != null) {
			// Le scalaire est sous forme de fraction
			scalaire = NumberUtils.divide(Double.parseDouble(dividende), Double.parseDouble(scal.toString()));
		} else if (scal.length() != 0) {
			scalaire = Double.parseDouble(scal.toString());
		}
		return scalaire;
	}

	/**
	 * Retourne true si l'instruction est une affectation de registre
	 * 
	 * @param instruction
	 * @return true/false
	 */
	public static boolean isAffectation(String instruction) {
		if ((instruction == null) || instruction.trim().isEmpty()) {
			return false;
		}
		return instruction.matches(Regexp.AFFECTATION);
	}

	/**
	 * Retourne true si l'instruction est une somme de registres
	 * 
	 * @param instruction
	 * @return true/false
	 */
	public static boolean isSomme(String instruction) {
		if ((instruction == null) || instruction.trim().isEmpty()) {
			return false;
		}
		return instruction.matches(Regexp.SOMME);
	}

	/**
	 * Retourne true si l'instruction est une multiplication de registres par un scalaire
	 * 
	 * @param instruction
	 * @return true/false
	 */
	public static boolean isMultiplication(String instruction) {
		if ((instruction == null) || instruction.trim().isEmpty()) {
			return false;
		}
		return instruction.matches(Regexp.MULTIPLICATION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (double reg : registres) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(reg);
		}
		sb.insert(0, "[");
		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) {
		// System.out.println(Double.parseDouble("2.3"));

		Parser myParser = new Parser(4, null);
		String instruction = "R2 = 3";
		myParser.execute(instruction);

		// instruction = "R1 = 5";
		// myParser.execute(instruction);
		//
		// instruction = "R3 = R1 - R2";
		// myParser.execute(instruction);
		//
		// instruction = "R4 = - R1 - R2";
		// myParser.execute(instruction);
		//
		// instruction = "R5 = - R1 + R2";
		// myParser.execute(instruction);
		//
		// instruction = "R6 = R1 + R2";
		// myParser.execute(instruction);
		//
		// instruction = "R7 = + R1 + R2";
		// myParser.execute(instruction);
		//
		// instruction = "R8 = + R1";
		// myParser.execute(instruction);
		//
		// instruction = "R9 = - R1";
		// myParser.execute(instruction);
		//
		// instruction = "R10 = - 3R1";
		// myParser.execute(instruction);
		//
		// instruction = "R11 = - 3*R1";
		// myParser.execute(instruction);
		//
		// instruction = "R12 = - 3.R1";
		// myParser.execute(instruction);
		//
		// instruction = "R13 = 2.R2";
		// myParser.execute(instruction);
		//
		// instruction = "R14 = + 2.R2";
		// myParser.execute(instruction);
		//
		// instruction = "R15 = 2*R2";
		// myParser.execute(instruction);
		//
		// instruction = "R16 = + 2*R2";
		// myParser.execute(instruction);
		//
		// instruction = "R17 = 2R2";
		// myParser.execute(instruction);
		//
		// instruction = "R18 = + 2R2";
		// myParser.execute(instruction);

		instruction = "R3 = + 2/9R2";
		myParser.execute(instruction);

		System.out.println(myParser);
	}
}
