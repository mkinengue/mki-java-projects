package fr.perso.sudoku.exception;

public class NotCompletedException extends SudokuException {

	private static final long serialVersionUID = -7300864760560569732L;

	public NotCompletedException(String message) {
		super(message);
	}
}
