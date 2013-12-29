package fr.mkinengue.sudoku.exception;

public class InvalidRowException extends SudokuException {

	private static final long serialVersionUID = -7373891050324029244L;

	public InvalidRowException(String message) {
		super(message);
	}
}
