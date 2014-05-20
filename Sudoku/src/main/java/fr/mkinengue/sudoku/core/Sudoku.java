package fr.mkinengue.sudoku.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.bean.Column;
import fr.mkinengue.sudoku.bean.Region;
import fr.mkinengue.sudoku.bean.Row;
import fr.mkinengue.sudoku.exception.InvalidRegionException;
import fr.mkinengue.sudoku.exception.InvalidRowException;
import fr.mkinengue.sudoku.exception.NotValidException;
import fr.mkinengue.sudoku.exception.SudokuException;
import fr.mkinengue.sudoku.logger.LogUtils;
import fr.mkinengue.sudoku.methodes.Methode;
import fr.mkinengue.sudoku.methodes.impl.NakedGroup;
import fr.mkinengue.sudoku.utils.SudokuUtils;

/**
 * Classe d'implémentation de lé résolution d'un Sudoku
 */
public class Sudoku {

	private static Logger LOG = Logger.getLogger(Sudoku.class.getSimpleName());

	static {
		LOG.setLevel(Level.FINEST);
	}

	private static final int MAX_ITER_TO_SOLVE = 1000;

	private final int size;

	private List<Methode> methodes;

	private final Case[][] grille;

	private List<Row> rowsList;

	private List<Column> columnsList;

	private Map<Case, Region> regionsByFirstCase;

	// Index des régions par case
	private Map<Case, Region> regionsByCase;

	/** Liste prioritaire des nombres à traiter dans l'ordre croissant d'index */
	private LinkedList<Integer> priorityNumbers;

	/** Liste prioritaire des index de ligne à traiter dans l'ordre croissant d'index */
	private LinkedList<Integer> priorityRows;

	/** Liste prioritaire des index de colonne à traiter dans l'ordre croissant d'index */
	private LinkedList<Integer> priorityColumns;

	/** Liste prioritaire des premières cases de région à traiter dans l'ordre croissant d'index */
	private LinkedList<Case> priorityRegions;

	/** Map du nombre total de valeurs dans la grille de chacun des nombres */
	private final Map<Integer, Integer> occurrenceByNumber;

	private final List<Case> emptyCases;

	/** Espacement correspondant au préfixe dû au numéro de la ligne */
	private static final String PREFIX_ROW_NUMBER_VALUE = "     ";

	/** Espacement correspondant à une case vide */
	private static final String DISP_EMPTY_CASE_VALUE = "   ";

	/** Nom du package contenant les méthodes de résolution ou d'élimination du Sudoku */
	private static final String PACKAGE_METHODES_RES_SUDOKU = "fr.mkinengue.sudoku.methodes.impl";

	/**
	 * Liste statique des méthodes de résolution à exclure. Uniquement pour les tests
	 */
	private static List<Class<?>> TO_EXCLUDE = new ArrayList<Class<?>>();
	static {
		// TO_EXCLUDE.add(CaseByCaseElimination.class);
		// TO_EXCLUDE.add(RowElimination.class);
		// TO_EXCLUDE.add(ColumnElimination.class);
		// TO_EXCLUDE.add(IndirectElimination.class);
		// TO_EXCLUDE.add(Singleton.class);
		// TO_EXCLUDE.add(SingletonCache.class);
		// TO_EXCLUDE.add(SingletonNu.class);
		TO_EXCLUDE.add(NakedGroup.class);
	}

	/**
	 * Constructeur initialisant une grille de sudoku de taille size. La grille contient size*size cases
	 * 
	 * @param size taille de base de la grille de Sudoku
	 */
	public Sudoku(final int size) {
		this.size = size;
		occurrenceByNumber = new HashMap<Integer, Integer>();
		emptyCases = new ArrayList<Case>();
		grille = new Case[size][size];
		initialization();
	}

	/**
	 * Initialisation d'une grille de sudoku par la grille d'entiers en paramètre.<br />
	 * La grille est supposée être carrée et valide
	 * 
	 * @param grille grille de Sudoku servant à initialiser l'objet Sudoku à résoudre
	 */
	public Sudoku(final Integer[][] grille) {
		size = grille.length;
		occurrenceByNumber = new HashMap<Integer, Integer>();
		emptyCases = new ArrayList<Case>();
		this.grille = new Case[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.grille[i][j] = new Case(i, j, grille[i][j], size);
				// On remplit la map des occurrences des nombres
				updateMapOccurrencesByNumber(grille[i][j]);

				if (grille[i][j] == null) {
					// Case vide, on complète la liste des cases vides
					emptyCases.add(this.grille[i][j]);
				} else if (grille[i][j] <= 0 || grille[i][j] > size) {
					throw new SudokuException("La case (" + i + "," + j
									+ ") ne peut pas contenir une valeur négative ou plus grande strictement que "
									+ size + ". Valeur contenue : " + grille[i][j]);
				}
			}
		}
		initialization();
	}

	/**
	 * Initialise les paramètres de la grille.<br />
	 * A appeler après intialisation de la grille et du paramètre size
	 */
	private void initialization() {
		methodes = new ArrayList<Methode>();
		rowsList = new ArrayList<Row>(size);
		columnsList = new ArrayList<Column>(size);
		regionsByFirstCase = new HashMap<Case, Region>(size);
		regionsByCase = new HashMap<Case, Region>(size * size);

		buildRowsFromGrille();
		buildColumnsFromGrille();
		buildRegionsFromGrille();
		initMethodes();

		// Initialisation des listes prioritaires
		buildAndFillPriorityLists();
	}

	/**
	 * Initialise les méthodes de résolution de la grille de Sudoku
	 */
	private void initMethodes() {
		// Définitions des méthodes de résolution
		final long debut = System.currentTimeMillis();
		final Class<?>[] classes = SudokuUtils.getClasses(PACKAGE_METHODES_RES_SUDOKU);
		for (final Class<?> classe : classes) {
			try {
				if (!TO_EXCLUDE.contains(classe) && !classe.getSimpleName().endsWith("Test")) {
					final Constructor<?> constructor = classe.getConstructor(Sudoku.class);
					final Object methInstance = constructor.newInstance(this);
					methodes.add((Methode) methInstance);
				}
			} catch (final Exception e) {
				throw new SudokuException(
								"Une exception a été levée en essayant de charger les méthodes de résolution", e);
			}
		}
		LOG.log(Level.INFO, "Fin exécution initialisation des méthodes en {0} ms", System.currentTimeMillis() - debut);
	}

	/**
	 * Initialise la liste des lignes à partir de la grille de Sudoku
	 */
	private void buildRowsFromGrille() {
		for (final Case[] element : grille) {
			rowsList.add(element[0].getRow(), new Row(element, element[0].getRow()));
		}
	}

	/**
	 * Initialise la liste des colonnes à partir de la grille de Sudoku
	 */
	private void buildColumnsFromGrille() {
		for (int j = 0; j < size; j++) {
			columnsList.add(j, new Column(size, j));
			for (int i = 0; i < size; i++) {
				columnsList.get(j).addCase(grille[i][j]);
			}
		}
	}

	/**
	 * Construit une map des différentes régions du Sudoku.<br />
	 * Une région est une mini grille carrée contenant size cases dont les indices de ligne et colonne sont liés par une
	 * formule<br />
	 * La map des régions a pour clé la case ayant les indices de ligne et colonne les plus faibles dans la région
	 */
	private void buildRegionsFromGrille() {
		final int sizeSquared = (int) Math.sqrt(size);
		for (int i = 0; i < size; i += sizeSquared) {
			for (int j = 0; j < size; j += sizeSquared) {
				regionsByFirstCase.put(grille[i][j], new Region(size, grille[i][j]));
				for (int k = i; k < i + sizeSquared; k++) {
					for (int l = j; l < j + sizeSquared; l++) {
						regionsByFirstCase.get(grille[i][j]).addCase(grille[k][l]);
					}
				}
			}
		}
	}

	/**
	 * Construit et remplit les listes de priorité des traitements pour les lignes, colonnes, régions et nombres
	 */
	private void buildAndFillPriorityLists() {
		// Instanciation de toutes les listes
		priorityNumbers = new LinkedList<Integer>();
		priorityRows = new LinkedList<Integer>();
		priorityColumns = new LinkedList<Integer>();
		priorityRegions = new LinkedList<Case>();

		// Liste des nombres, index de ligne et de colonne prioritaires à traiter
		for (int i = 0; i < size; i++) {
			priorityNumbers.add(Integer.valueOf(i));
			priorityRows.add(Integer.valueOf(i));
			priorityColumns.add(Integer.valueOf(i));
		}

		// Liste des régions prioritaires à traiter
		for (final Case c : regionsByFirstCase.keySet()) {
			priorityRegions.add(c);
		}
	}

	/**
	 * Incrémente le nombre d'occurrences
	 * 
	 * @param value
	 */
	public void updateMapOccurrencesByNumber(final Integer value) {
		if (value == null) {
			return;
		}
		// On initialise la map comptant le nombre d'occurrences de chacun des nombres dans la grille
		if (occurrenceByNumber.get(value) == null) {
			// Premier passage pour ce nombre, on initialise à zéro et on va incrémenter plus tard
			occurrenceByNumber.put(value, 0);
		}
		occurrenceByNumber.put(value, occurrenceByNumber.get(value) + 1);
	}

	/**
	 * Met à jour les listes de priorité suite à la modification de la case changedCase<br />
	 * Pour chacune des listes de priorité, on positionne en début de liste les références de la case correspondante
	 * 
	 * @param Case changedCase case fraîchement modifiée
	 */
	public void updatePrioritiesByCase(final Case changedCase) {
		// Mise à jour de la liste de priorité des lignes
		priorityRows.remove(Integer.valueOf(changedCase.getRow()));
		priorityRows.addFirst(Integer.valueOf(changedCase.getRow()));

		// Mise à jour de la liste de priorité des colonnes
		priorityColumns.remove(Integer.valueOf(changedCase.getColumn()));
		priorityColumns.addFirst(Integer.valueOf(changedCase.getColumn()));

		// Mise à jour de la liste de priorité des nombres si la case n'est pas vide
		if (changedCase.getValue() != null) {
			priorityNumbers.remove(changedCase.getValue());
			priorityNumbers.addFirst(changedCase.getValue());
		}

		// Mise à jour de la liste de priorité des régions
		// On cherche la région correspondant à la case
		final Region regionOfChangedCase = regionsByCase.get(changedCase);
		if (regionOfChangedCase == null) {
			LOG.warning("Attention : la région de la case nouvellement valuée (" + changedCase
							+ ") n'a pas été trouvée => pas de mise à jour de la liste de priorité des régions.");
			return;
		}
		// On cherche maintenant la première case de la région
		final Case firstCaseRegion = regionOfChangedCase.getFirstCase();
		priorityRegions.remove(firstCaseRegion);
		priorityRegions.addFirst(firstCaseRegion);
	}

	/**
	 * Mets à jour la case en paramètre dans le cas où sa liste de candidats ne contient plus qu'un unique élément. Dans
	 * un tel cas, la case est valorisée avec le candidat, la liste des candidats vidée, la map du nombre d'occurrences
	 * des valeurs contenues dans la grille incrémentée et la case currCase supprimée de la liste des cases vides de la
	 * grille. Les listes des priorités ont été également mises à jour<br />
	 * 
	 * @param currCase
	 */
	public void updateCaseWithOneCandidate(Case currCase) {
		if (currCase.getCandidates().size() == 1) {
			currCase.setValue(currCase.getCandidates().get(0).intValue());

			// On vide la liste des candidats de la case juste remplie
			currCase.getCandidates().clear();

			// On remplit la map des occurrences des nombres
			updateMapOccurrencesByNumber(currCase.getValue());

			// On met à jour la liste des priorités
			updatePrioritiesByCase(currCase);

			// On supprime la case de la liste des cases vides
			getEmptyCases().remove(currCase);
		}
	}

	/**
	 * Méthode de résolution de la grille de Sudoku<br />
	 * Tant que la grille n'est pas pleine et que l'on n'a pas fini d'itérer le nombre maximum fixé, on applique
	 * successivement toutes les méthodes de résolution définies<br />
	 * Retourne Vrai si la grille est résolue et faux sinon<br />
	 * Lève une exception NotValidException si la grille contient des incohérences lors de la résolution
	 * 
	 * @return vrai/faux
	 */
	public boolean solve() {
		LogUtils.logEnteringMethod(LOG, Level.INFO, getClass().getSimpleName(), "solve");
		final long debut = System.currentTimeMillis();
		// Réduction de la grille avant de commencer
		// reduceCaseByCase();
		int cptExec = 0;
		while (!isFull() && cptExec < MAX_ITER_TO_SOLVE) {
			for (final Methode methode : methodes) {
				methode.execute();
			}

			cptExec++;
			if (cptExec % 100 == 0) {
				LOG.log(Level.INFO, "Fin itération {0}", cptExec);
			}
		}

		if (!isValid()) {
			throw new NotValidException("La grille a été remplie mais n'est pas valide");
		}

		// LogUtils.logExitingMethod(level, className, methodName, start)
		LOG.log(Level.INFO, "Fin de tentative de résolution en {0} ms après {1} itérations",
						new Object[] { System.currentTimeMillis() - debut, cptExec });
		return isFull();
	}

	/**
	 * @return List&lt;Methode&gt; : methodes
	 */
	public List<Methode> getMethodes() {
		return methodes;
	}

	/**
	 * @return int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * REtourne la grille de Sudoku
	 * 
	 * @return Case[][]
	 */
	public Case[][] getGrille() {
		return grille;
	}

	/**
	 * Retourne vrai si la grille est pleine et faux sinon
	 * 
	 * @return vrai / faux
	 */
	public boolean isFull() {
		return emptyCases.isEmpty();
	}

	/**
	 * Retourne vrai si la grille de Sudoku est valide et cohérente<br />
	 * Lève une exception si une incohérence et trouvée dans l'une des lignes, colonnes ou régions
	 * 
	 * @return vrai / faux
	 */
	public boolean isValid() {
		for (int i = 0; i < size; i++) {
			if (!SudokuUtils.verifyCases(getCasesByRow(i))) {
				throw new InvalidRowException("La ligne numéro [" + (i + 1)
								+ "] a au moins deux cases ayant la même valeur");
			}
			if (!SudokuUtils.verifyCases(getCasesByColumn(i))) {
				throw new InvalidRowException("La colonne numéro [" + (i + 1)
								+ "] a au moins deux cases ayant la même valeur");
			}
		}

		// Vérifier par région
		final Map<Case, Region> mapRegionsByFirstCase = getRegionsByFirstCase();
		for (final Region reg : mapRegionsByFirstCase.values()) {
			if (!SudokuUtils.verifyCases(reg.getCases())) {
				throw new InvalidRegionException("La région de la case [" + reg.getFirstCase()
								+ "] a au moins deux cases ayant la même valeur");
			}
		}
		return true;
	}

	/**
	 * @return the rowsList
	 */
	public List<Row> getRowsList() {
		return rowsList;
	}

	/**
	 * @return the columnsList
	 */
	public List<Column> getColumnsList() {
		return columnsList;
	}

	/**
	 * Retourne la map associant pour chaque case de la grille la région à laquelle elle appartient.
	 * 
	 * @return the regionsByCase
	 */
	public Map<Case, Region> getRegionsByCase() {
		return regionsByCase;
	}

	/**
	 * @return thr emptyCases
	 */
	public List<Case> getEmptyCases() {
		return emptyCases;
	}

	/**
	 * Retourne la liste des cases de la grille de la ligne d'index row.<br />
	 * Lève une {@link Exception} InvalidRowException si l'index de ligne n'est pas valide
	 * 
	 * @param row index de la ligne à extraire
	 * @return Case[]
	 */
	public Case[] getCasesByRow(final int row) {
		return SudokuUtils.extractRow(row, grille);
	}

	public Case[] getCasesByColumn(final int column) {
		return SudokuUtils.extractColumn(column, grille);
	}

	/**
	 * Retourne la région correspondant à la case en paramètre.<br />
	 * Un index de cases par région est rensigné de façon à accélérer les traitements<br />
	 * Lève une exception si aucune région n'est trouvée pour la case
	 * 
	 * @param c case dont on cherche la région à laquelle elle appartient
	 * @return Region
	 */
	public Region getRegionByCase(final Case c) {
		if (regionsByCase.get(c) != null) {
			return regionsByCase.get(c);
		}

		final int sizeQuared = (int) Math.sqrt(grille[0].length);
		final float diffRowC = (float) Math.floor((float) (Math.abs(c.getRow()) * 1.0 / sizeQuared));
		final float diffColC = (float) Math.floor((float) (Math.abs(c.getColumn()) * 1.0 / sizeQuared));
		for (final Case fstCaseReg : regionsByFirstCase.keySet()) {
			final float diffRowFst = (float) Math.floor((float) (Math.abs(fstCaseReg.getRow()) * 1.0 / sizeQuared));
			final float diffColFst = (float) Math.floor((float) (Math.abs(fstCaseReg.getColumn()) * 1.0 / sizeQuared));
			if (diffRowC == diffRowFst && diffColC == diffColFst) {
				final Region region = regionsByFirstCase.get(fstCaseReg);
				regionsByCase.put(c, region);
				return region;
			}
		}
		throw new SudokuException("La case " + c
						+ " n'appartient à aucune région. Régions connues par première case : " + regionsByFirstCase);
	}

	/**
	 * Retourne la map des régions de la grille par case de la région ayant les index de colonne et de ligne les moins
	 * élevés
	 * 
	 * @return
	 */
	public Map<Case, Region> getRegionsByFirstCase() {
		return regionsByFirstCase;
	}

	/**
	 * @return
	 */
	public LinkedList<Integer> getPriorityNumbers() {
		return priorityNumbers;
	}

	/**
	 * @return
	 */
	public LinkedList<Integer> getPriorityRows() {
		return priorityRows;
	}

	/**
	 * @return
	 */
	public LinkedList<Integer> getPriorityColumns() {
		return priorityColumns;
	}

	/**
	 * @return
	 */
	public LinkedList<Case> getPriorityRegions() {
		return priorityRegions;
	}

	/**
	 * @return
	 */
	public Map<Integer, Integer> getOccurrenceByNumber() {
		return occurrenceByNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder strB = new StringBuilder();
		// Affichage des numéros de colonne
		strB.append(PREFIX_ROW_NUMBER_VALUE);
		for (int j = 0; j < size; j++) {
			if (j > 0 && j % (int) Math.sqrt(size) == 0) {
				strB.append(" ");
			}
			strB.append("  ");
			if (j < 10) {
				strB.append("0");
			}
			strB.append(j);
		}
		strB.append("\n");

		for (int i = 0; i < size; i++) {
			// Début affichage des colonnes
			strB.append(PREFIX_ROW_NUMBER_VALUE);
			for (int j = 0; j <= size; j++) {
				strB.append("----");
			}
			strB.append("-");
			strB.append("\n");
			if (i != size && i > 1 && i % (int) Math.sqrt(size) == 0) {
				// On double les barres horizontales pour séparer les régions
				strB.append(PREFIX_ROW_NUMBER_VALUE);
				for (int j = 0; j <= size; j++) {
					strB.append("----");
				}
				strB.append("-");
				strB.append("\n");
			}

			final StringBuilder strBTmp = new StringBuilder();
			for (int j = 0; j <= size; j++) {
				if (strBTmp.length() == 0) {
					// On préfixe la ligne par son numéro
					if (i < 10) {
						strBTmp.append("0");
					}
					strBTmp.append(i).append(" : ");
				}
				strBTmp.append("|");
				if (j != size && j > 1 && j % (int) Math.sqrt(size) == 0) {
					// On double les barres verticales pour séparer les régions
					strBTmp.append("|");
				}

				if (j != size) {
					if (grille[i][j].getValue() == null) {
						strBTmp.append(DISP_EMPTY_CASE_VALUE);
					} else {
						strBTmp.append(" ").append(grille[i][j].getValue()).append(" ");
					}
				}
			}
			strB.append(strBTmp);
			strB.append("\n");
		}

		// On préfixe de 5 la fin de la ligne correspondant au numéro de la ligne qui préfixe
		strB.append(PREFIX_ROW_NUMBER_VALUE);
		for (int j = 0; j <= size; j++) {
			strB.append("----");
		}
		strB.append("-");
		return strB.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public static void main(final String[] args) {
		final Integer[][] grille = { { null, null, null, null, 6, null, null, 8, 9 },
						{ null, 7, 2, 5, null, null, null, null, 3 },
						{ null, null, null, null, null, 8, null, null, null },
						{ null, null, null, null, null, 5, null, null, null },
						{ null, 3, 1, null, null, null, null, null, null },
						{ 6, null, null, null, null, null, 8, 2, null },
						{ 9, null, null, null, null, 6, 7, null, null }, { null, 4, null, 1, null, null, null, 6, 8 },
						{ 3, null, null, null, 2, null, 5, null, null } };
		final Sudoku sudoku = new Sudoku(grille);
		System.out.println(sudoku);
		System.out.println("Sudoku is full : " + sudoku.isFull());

		// LOG.log(Level.INFO, "Sudoku résolu en {0} ms et {1} param", new Object[] { 20, "toto" });
		System.out.println("Methodes : " + Package.getPackage("fr.mkinengue.sudoku.methodes.impl"));

		final boolean solved = sudoku.solve();
		System.out.println("Sudoku solved : " + solved);
		System.out.println("Sudoku is full : " + sudoku.isFull());
		System.out.println(sudoku);

		// TODO Test isValid()
		// TODO Test RegionByFirstCase
		// TODO Test getRegionByCase
		// TODO Test getCasesByRow()
		// TODO Test getCasesByColumn()
		// TODO Test solve()
	}
}
