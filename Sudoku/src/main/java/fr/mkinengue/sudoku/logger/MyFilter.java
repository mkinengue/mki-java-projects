package fr.mkinengue.sudoku.logger;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Filter to apply for logging
 */
public class MyFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoggable(final LogRecord record) {
		record.setLevel(Level.FINEST);
		return true;
	}
}
