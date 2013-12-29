package fr.mkinengue.impots.bean;

/**
 * Définit une tranche d'impôt
 * 
 * @author mkinengue
 */
public class Tranche {

	private float min;

	private float max;

	private float taux;

	/**
	 * Constructeur par défaut
	 */
	public Tranche() {
		min = Float.MIN_VALUE;
		max = Float.MAX_VALUE;
		taux = Float.NaN;
	}

	/**
	 * Constructeur initialisant la tranche minimale et maximale de la tranche
	 * 
	 * @param min
	 * @param max
	 * @param taux
	 */
	public Tranche(final float min, final float max, final float taux) {
		this.min = min;
		this.max = max;
		this.taux = taux;
	}

	/**
	 * @return the min
	 */
	public float getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(final float min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public float getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(final float max) {
		this.max = max;
	}

	/**
	 * @return the taux
	 */
	public float getTaux() {
		return taux;
	}

	/**
	 * @param taux the taux to set
	 */
	public void setTaux(final float taux) {
		this.taux = taux;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[min:" + min + " - max:" + max + " - taux:" + taux + "]";
	}
}
