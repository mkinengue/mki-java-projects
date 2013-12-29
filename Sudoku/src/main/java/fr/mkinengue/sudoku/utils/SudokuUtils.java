package fr.mkinengue.sudoku.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import fr.mkinengue.sudoku.bean.Case;
import fr.mkinengue.sudoku.exception.InvalidColumnException;
import fr.mkinengue.sudoku.exception.InvalidRowException;
import fr.mkinengue.sudoku.exception.SudokuException;

/**
 * Classe utilitaire contenant toutes les fonctions nécessaires à la résolution du Sudoku
 */
public class SudokuUtils {

	/**
	 * Constructeur privé par défaut
	 */
	private SudokuUtils() {
	}

	/**
	 * Vérifie qu'il n'existe pas deux cases vides de la liste des cases en paramètre ayant la même valeur.<br />
	 * Retourne vrai s'il n'existe aucune paire de cases non vides de mêmes valeurs et faux s'il existe au moins une
	 * paire de cases non vides ayant une même valeur
	 * 
	 * @param cc liste des cases à vérifier
	 * @return vrai / faux
	 */
	public static boolean verifyCases(final Case[] cc) {
		final List<Integer> listOfValue = new ArrayList<Integer>();
		for (int i = 0; i < cc.length; i++) {
			if (!cc[i].isEmpty()) {
				if (listOfValue.contains(cc[i].getValue())) {
					return false;
				}
				listOfValue.add(cc[i].getValue());
			}
		}
		return true;
	}

	/**
	 * Extrait de la grille de Sudoku la ligne d'index row<br />
	 * Lève une exception InvalidRowException si l'index est négatif ou supérieur ou égal au nombre d'éléments d'une
	 * ligne
	 * 
	 * @param row index de la ligne à extraire
	 * @param grille grille de Sudoku
	 * @return Case[] ligne extraite
	 */
	public static Case[] extractRow(final int row, final Case[][] grille) throws InvalidRowException {
		final int size = grille[0].length;
		if (row < 0 || row >= size) {
			throw new InvalidRowException(
					"Le numéro de ligne fourni pour l'extraction des cases de la ligne doit être compris entre 0 et "
							+ (size - 1) + " inclus. Ligne : " + row);
		}
		return grille[row];
	}

	/**
	 * Extrait de la grille de Sudoku la colonne d'index column<br />
	 * Lève une exception InvalidColumnException si l'index est négatif ou supérieur ou égal au nombre d'éléments d'une
	 * ligne
	 * 
	 * @param column index de la colonne à extraire
	 * @param grille grille de Sudoku
	 * @return Case[] colonne extraite
	 */
	public static Case[] extractColumn(final int column, final Case[][] grille) throws InvalidColumnException {
		final int size = grille[0].length;
		if (column < 0 || column >= size) {
			throw new InvalidColumnException(
					"Le numéro de colonne fourni pour l'extraction des cases de la colonne doit être compris entre 0 et "
							+ (size - 1) + " inclus. Colonne : " + column);
		}
		final Case[] colCases = new Case[size];
		for (int i = 0; i < size; i++) {
			colCases[i] = grille[i][column];
		}
		return colCases;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 * 
	 * @param packageName The name of base package
	 * @return Array of classes
	 * @throws SudokuException
	 */
	public static Class<?>[] getClasses(final String packageName) {
		try {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			final String path = packageName.replace('.', '/');
			final Enumeration<URL> resources = classLoader.getResources(path);
			final List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				final URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
			for (final File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			return classes.toArray(new Class[classes.size()]);
		} catch (final ClassNotFoundException e) {
			throw new SudokuException("L'une des classes n'a pas été trouvée dans le package " + packageName, e);
		} catch (final IOException e) {
			throw new SudokuException(
					"Une exception de type IO a été trouvée lors de la recherche des classes du package " + packageName,
					e);
		}
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 * 
	 * @param directory The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(final File directory, final String packageName)
			throws ClassNotFoundException {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		final File[] files = directory.listFiles();
		for (final File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
