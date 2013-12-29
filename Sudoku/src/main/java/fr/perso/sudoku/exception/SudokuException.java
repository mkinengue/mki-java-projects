package fr.perso.sudoku.exception;

public class SudokuException extends RuntimeException {
	
	private static final long serialVersionUID = 8324140428348553881L;

	public SudokuException() {
		super();
	}
	
	public SudokuException(String message) {
		super(message);
	}
	
	public SudokuException(Throwable cause) {
		super(cause);
	}
	
	public SudokuException(String message, Throwable cause) {
		super(message, cause);
	}
}
