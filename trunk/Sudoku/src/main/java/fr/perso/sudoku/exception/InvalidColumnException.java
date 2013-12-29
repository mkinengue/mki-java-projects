package fr.perso.sudoku.exception;

/**
 * Exception dans le cas où une colonne est invalide de par son contenu
 */
public class InvalidColumnException extends SudokuException {

	private static final long serialVersionUID = -7373891050324029244L;

	/**
	 * @param message
	 */
	public InvalidColumnException(final String message) {
		super(message);
	}
}
