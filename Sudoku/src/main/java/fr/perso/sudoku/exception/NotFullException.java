package fr.perso.sudoku.exception;

public class NotFullException extends SudokuException {

	private static final long serialVersionUID = -7373891050324029244L;

	public NotFullException(String message) {
		super(message);
	}
}
