package fr.mkinengue.sudoku.exception;

public class NotValidException extends SudokuException {

	private static final long serialVersionUID = -7373891050324029244L;

	public NotValidException(String message) {
		super(message);
	}
}
