package fr.mkinengue.sudoku.exception;

/**
 * Exception dans le cas où une colonne est invalide de par son contenu
 */
public class InvalidRegionException extends SudokuException {

	private static final long serialVersionUID = -7373891050324029244L;

	/**
	 * @param message
	 */
	public InvalidRegionException(final String message) {
		super(message);
	}
}
