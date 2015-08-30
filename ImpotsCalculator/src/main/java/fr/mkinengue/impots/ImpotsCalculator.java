package fr.mkinengue.impots;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;

import fr.mkinengue.impots.bean.Tranche;

/**
 * Calcule l'impôt sur le revenu en fonction des tranches d'une année donnée
 * 
 * @author mkinengue
 */
public class ImpotsCalculator {

	/**
	 * Contient pour une année donnée les tranche min, max d'une tranche d'impôt donnée ainsi que le taux d'imposition
	 * pour cette tranche
	 */
	private static MultiKeyMap BAREME_PAR_ANNEE = new MultiKeyMap();

	/**
	 * Supprime le barême de l'année donnée en paramètre ou alors l'ensemble du barême si l'année fournie est nulle
	 * 
	 * @param annee
	 */
	public static void clearBaremeParAnnee(final Integer annee) {
		if (annee == null) {
			BAREME_PAR_ANNEE.clear();
		} else if (BAREME_PAR_ANNEE.get(annee) != null) {
			BAREME_PAR_ANNEE.remove(annee);
		}
	}

	/**
	 * Construit le barême des impôts de l'année 2008
	 */
	public static void fillAnnee2008() {
		// On supprime l'année 2008
		clearBaremeParAnnee(2008);

		// On insère l'année 2008
		BAREME_PAR_ANNEE.put(2008, 0, 5687, 0f);
		BAREME_PAR_ANNEE.put(2008, 5688, 11344, 0.055f);
		BAREME_PAR_ANNEE.put(2008, 11345, 25195, 0.14f);
		BAREME_PAR_ANNEE.put(2008, 25196, 67546, 0.3f);
		BAREME_PAR_ANNEE.put(2008, 67547, Integer.MAX_VALUE, 0.4f);
	}

	/**
	 * Construit le barême des impôts de l'année 2009
	 */
	public static void fillAnnee2009() {
		// On supprime l'année 2009
		clearBaremeParAnnee(2009);

		// On insère l'année 2009
		BAREME_PAR_ANNEE.put(2009, 0, 5853, 0f);
		BAREME_PAR_ANNEE.put(2009, 5854, 11674, 0.055f);
		BAREME_PAR_ANNEE.put(2009, 11675, 25927, 0.14f);
		BAREME_PAR_ANNEE.put(2009, 25928, 69505, 0.3f);
		BAREME_PAR_ANNEE.put(2009, 69506, Integer.MAX_VALUE, 0.4f);
	}

	/**
	 * Construit le barême des impôts de l'année 2010
	 */
	public static void fillAnnee2010() {
		// On supprime l'année 2010
		clearBaremeParAnnee(2010);

		// On insère l'année 2010
		BAREME_PAR_ANNEE.put(2010, 0, 5875, 0f);
		BAREME_PAR_ANNEE.put(2010, 5876, 11720, 0.055f);
		BAREME_PAR_ANNEE.put(2010, 11721, 26030, 0.14f);
		BAREME_PAR_ANNEE.put(2010, 26031, 69783, 0.3f);
		BAREME_PAR_ANNEE.put(2010, 69784, Integer.MAX_VALUE, 0.4f);
	}

	/**
	 * Construit le barême des impôts de l'année 2011
	 */
	public static void fillAnnee2011() {
		// On supprime l'année 2011
		clearBaremeParAnnee(2011);

		// On insère l'année 2011
		BAREME_PAR_ANNEE.put(2011, 0, 5963, 0f);
		BAREME_PAR_ANNEE.put(2011, 5964, 11896, 0.055f);
		BAREME_PAR_ANNEE.put(2011, 11897, 26420, 0.14f);
		BAREME_PAR_ANNEE.put(2011, 26421, 70830, 0.3f);
		BAREME_PAR_ANNEE.put(2011, 70831, Integer.MAX_VALUE, 0.4f);
	}

	/**
	 * Construit le barême des impôts de l'année 2012
	 */
	public static void fillAnnee2012() {
		// On supprime l'année 2012
		clearBaremeParAnnee(2012);

		// On insère l'année 2012
		BAREME_PAR_ANNEE.put(2012, 0, 5963, 0f, 0f);
		BAREME_PAR_ANNEE.put(2012, 5964, 11896, 0.055f, 327.97f);
		BAREME_PAR_ANNEE.put(2012, 11897, 26420, 0.14f, 1339.13f);
		BAREME_PAR_ANNEE.put(2012, 26421, 70830, 0.3f, 5566.33f);
		BAREME_PAR_ANNEE.put(2012, 70831, 150000, 0.41f, 13357.63f);
		BAREME_PAR_ANNEE.put(2012, 150001, Integer.MAX_VALUE, 0.45f, 19357.63f);
	}

	/**
	 * Construit le barême des impôts de l'année 2015 pour l'année écoulée de 2014
	 */
	public static void fillAnnee2015() {
		// On supprime l'année 2015
		clearBaremeParAnnee(2015);

		// On insère l'année 2015
		BAREME_PAR_ANNEE.put(2015, 0, 9690, 0f, 0f);
		BAREME_PAR_ANNEE.put(2015, 9691, 26764, 0.14f, 1339.13f);
		BAREME_PAR_ANNEE.put(2015, 26765, 71754, 0.3f, 5566.33f);
		BAREME_PAR_ANNEE.put(2015, 71755, 151956, 0.41f, 13357.63f);
		BAREME_PAR_ANNEE.put(2015, 151957, Integer.MAX_VALUE, 0.45f, 19357.63f);
	}

	/**
	 * Construit le barême de l'année annee après l'avoir supprimé de la map
	 * 
	 * @param annee
	 * @param bareme
	 */
	public static void fillAnnee(final int annee, final MultiKeyMap bareme) {
		// On supprime l'année annee
		clearBaremeParAnnee(annee);

		// On insère l'année annee
		BAREME_PAR_ANNEE.put(annee, bareme);
	}

	/**
	 * Retourne le montant net imposable pour le revenu fiscal revenu
	 * 
	 * @param annee
	 * @param revenu
	 * @param partFiscale
	 * @param bareme
	 * @return float
	 */
	public static float getImpotsValue(final int annee, final float revenu, final float partFiscale,
					final MultiKeyMap bareme) {
		if (annee < 2008) {
			if (bareme == null) {
				throw new RuntimeException("Le barême pour l'année " + annee
								+ " doit être fourni car non géré par défaut");
			}
			fillAnnee(annee, bareme);
		} else if (annee == 2008) {
			fillAnnee2008();
		} else if (annee == 2009) {
			fillAnnee2009();
		} else if (annee == 2010) {
			fillAnnee2010();
		} else if (annee == 2011) {
			fillAnnee2011();
		} else if (annee == 2012) {
			fillAnnee2012();
		} else if (annee == 2015) {
			fillAnnee2015();
		}

		// Récupérer le barême
		final MultiKeyMap baremeAnnee = getBaremeParAnnee(annee);

		// Récupérer le quotient familial
		// float charge = 0.803575f;
		// charge = 0.78f;
		final float revenuNetImp = revenu;
		final float quotientFam = round(revenuNetImp / partFiscale, 2);

		Float impot = calculImpots2(quotientFam, baremeAnnee, partFiscale);
		impot = null;
		if (impot == null) {
			impot = calculImpots1(quotientFam, baremeAnnee, partFiscale);
		}

		// return (impot * partFiscale);
		return round(impot, 2);
	}

	/**
	 * @param quotientFam
	 * @param baremeAnnee
	 * @param partFiscale
	 * @return float
	 */
	private static float calculImpots1(final float quotientFam, final MultiKeyMap baremeAnnee, final float partFiscale) {
		float impot = 0f;
		final List<Tranche> tranches = getTranchesParQuotientFamilial(baremeAnnee, quotientFam);
		for (final Tranche tranche : tranches) {
			if (quotientFam >= tranche.getMin()) {
				if (quotientFam > tranche.getMax()) {
					final float revTranche = add(tranche.getMax(), -tranche.getMin()) * tranche.getTaux();
					impot = add(impot, round(revTranche, 2));
				} else {
					final float revTranche = add(quotientFam, -tranche.getMin()) * tranche.getTaux();
					impot = add(impot, round(revTranche, 2));
				}
			}
		}
		return impot * partFiscale;
	}

	/**
	 * @param quotientFam
	 * @param baremeAnnee
	 * @param partFiscale
	 * @return float
	 * @see http://www.efl.fr/en-direct/indices_taux/fiscal/impot/formule_calcul.html
	 */
	private static Float calculImpots2(final float quotientFam, final MultiKeyMap baremeAnnee, final float partFiscale) {
		float impot = 0f;

		final MapIterator mapIterator = baremeAnnee.mapIterator();
		while (mapIterator.hasNext()) {
			final MultiKey next = (MultiKey) mapIterator.next();
			if (next.size() == 2) {
				return null;
			}
			final int min = (Integer) next.getKey(0);
			final int max = (Integer) next.getKey(1);
			final float taux = (Float) next.getKey(2);

			if (quotientFam >= min && quotientFam <= max || quotientFam >= max) {
				final float coeff = (Float) baremeAnnee.get(min, max, taux);

				final float calc = add(quotientFam * taux * partFiscale, -coeff * partFiscale);
				impot = round(add(impot, calc), 2);
			}
		}
		return null;
	}

	/**
	 * Arrondit value à nbPts chiffres après la virgule
	 * 
	 * @param value
	 * @param nbPts
	 * @return float
	 */
	private static float round(final float value, final int nbPts) {
		final double pow = Math.pow(10d, nbPts);
		final float val = Math.round(value * pow);
		return (float) (val / pow);
	}

	/**
	 * Ajoute les deux flaot f1 et f2 donnés en paramètre en utilisant BigDecimal afin d'éviter le problème connu de
	 * Java dans les additions de float et double
	 * 
	 * @param f1
	 * @param f2
	 * @return float
	 */
	private static float add(final float f1, final float f2) {
		final BigDecimal bg = new BigDecimal(f1);
		final BigDecimal bgAdd = bg.add(new BigDecimal(f2));
		return bgAdd.floatValue();
	}

	/**
	 * Retourne null si aucun barême pour l'année anne n'est défini. Retourne le barême correspondant si l'année est
	 * définie
	 * 
	 * @param annee
	 * @return MultiKeyMap
	 */
	private static MultiKeyMap getBaremeParAnnee(final int annee) {
		final MapIterator mapIterator = BAREME_PAR_ANNEE.mapIterator();
		final MultiKeyMap bareme = new MultiKeyMap();
		while (mapIterator.hasNext()) {
			final MultiKey next = (MultiKey) mapIterator.next();
			final int anneeTmp = (Integer) next.getKey(0);
			final int min = (Integer) next.getKey(1);
			final int max = (Integer) next.getKey(2);
			Float taux = null;
			if (next.size() == 4) {
				taux = (Float) next.getKey(3);
			}

			if (annee == anneeTmp) {
				if (taux == null) {
					bareme.put(min, max, BAREME_PAR_ANNEE.get(annee, min, max));
				} else {
					bareme.put(min, max, taux, BAREME_PAR_ANNEE.get(annee, min, max, taux));
				}
			}
		}
		return bareme;
	}

	/**
	 * Retourne l'ensemble des tranches du barême des impôts donné en paramètre
	 * 
	 * @param bareme
	 * @param quotientFamilial
	 * @return List&lt;Tranche&gt;
	 */
	private static List<Tranche> getTranchesParQuotientFamilial(final MultiKeyMap bareme, final float quotientFamilial) {
		final List<Tranche> tranches = new ArrayList<Tranche>();
		if (bareme != null) {
			final MapIterator mapIterator = bareme.mapIterator();
			while (mapIterator.hasNext()) {
				final MultiKey next = (MultiKey) mapIterator.next();
				final int min = (Integer) next.getKey(0);
				final int max = (Integer) next.getKey(1);
				Float taux = null;
				if (next.size() == 3) {
					taux = (Float) next.getKey(2);
				}

				if (quotientFamilial >= max || quotientFamilial >= min) {
					if (taux == null) {
						tranches.add(new Tranche(min, max, (Float) bareme.get(min, max)));
					} else {
						tranches.add(new Tranche(min, max, taux));
					}
				}
			}
		}
		return tranches;
	}

	public static void main(final String[] args) {
		final int annee = 2015;
		// final float revenu = 66656f;
		final float revenu = 36743f;
		final float part = 1f;
		// part=2f->6223.58 / part=2.5f->4751.725(1 ou 2 enf) / part=3f->4082.0703 (3enf)
		// part=3.5f->3412.43 (4enf) / part=4f->2742.76 (5enf) / part=4.5f->2073.105 (6enf)
		final float impot = getImpotsValue(annee, revenu, part, null);
		System.out.println("Impôt de l'année " + annee + " pour le revenu " + revenu + " avec quotiont familial "
						+ part + " est de : " + impot);
	}
}
