/**
 * 
 */
package fr.mkinengue.sudoku.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for logging
 */
public final class LogUtils {

	private static Logger LOG = Logger.getLogger(LogUtils.class.getSimpleName());

	static {
		LOG.setLevel(Level.FINEST);
		LOG.setFilter(new MyFilter());
	}

	/**
	 * Private constructor to prevent implemetation of this class
	 */
	private LogUtils() {
	}

	/**
	 * Return the static logger to use to log the application
	 * 
	 * @return Logger
	 */
	public static Logger getLogger() {
		return LOG;
	}

	/**
	 * Logging entering a method by allowing the level of log desired
	 * 
	 * @param logger
	 * @param level
	 * @param className
	 * @param methodName
	 * @return true
	 */
	public static boolean logEnteringMethod(final Logger logger, final Level level, final String className,
			final String methodName) {
		logger.log(level, "{0} : Entering {1} ...", new Object[] { className, methodName });
		return true;
	}

	/**
	 * Logging exiting a method by allowing the level of log desired and by indicating the time spent in the method if
	 * start is provided
	 * 
	 * @param logger
	 * @param level
	 * @param className
	 * @param methodName
	 * @param start
	 * @return true
	 */
	public static boolean logExitingMethod(final Logger logger, final Level level, final String className,
			final String methodName, final Long start) {
		String msg = "{0} : Exiting {1}";
		final Object[] params = new Object[] { className, methodName, null };
		if (start != null) {
			msg += " after {2} ms";
			params[2] = System.currentTimeMillis() - start;
		}
		logger.log(level, msg, params);
		return true;
	}
}
