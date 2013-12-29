package fr.mkinengue.projetinfox.transposition.tools;

import java.math.BigDecimal;

/**
 * Utilitaire pour les nombres
 * 
 * @author MKINENGU
 */
public class NumberUtils {

	/** Constructeur privé */
	private NumberUtils() {
	}

	/**
	 * Effectue la multiplication de deux double en utilisant les BigDecimal pour éviter le bug Java des double
	 * 
	 * @param d1
	 * @param d2
	 * @return double
	 */
	public static double multiply(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.multiply(bd2).doubleValue();
	}

	/**
	 * Effectue l'addition de deux double en utilisant les BigDecimal pour éviter le bug Java des double
	 * 
	 * @param d1
	 * @param d2
	 * @return double
	 */
	public static double add(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.add(bd2).doubleValue();
	}

	/**
	 * Effectue la soustraction de deux double (d1 - d2) en utilisant les BigDecimal pour éviter le bug Java des double
	 * 
	 * @param d1
	 * @param d2
	 * @return double
	 */
	public static double substract(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.subtract(bd2).doubleValue();
	}

	/**
	 * Effectue la soustraction de deux double (d1 - d2) en utilisant les BigDecimal pour éviter le bug Java des double
	 * 
	 * @param d1
	 * @param d2
	 * @return double
	 */
	public static double divide(double d1, double d2) {
		// BigDecimal bd1 = new BigDecimal(d1);
		// BigDecimal bd2 = new BigDecimal(d2);
		// DecimalFormat df = new DecimalFormat();
		// df.setMaximumFractionDigits(2);
		// df.setMinimumFractionDigits(0);
		return d1 / d2;
		// return Double.parseDouble(df.format(bd1.divide(bd2).doubleValue()));
	}
}
