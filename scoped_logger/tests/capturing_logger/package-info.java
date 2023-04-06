/**
 * Implements a {@link java.lang.System.Logger} and {@link java.lang.System.LoggerFinder} for unit tests.
 * Logged messages are captured in a collection {@link capturing_logger.CapturingLogger#logs()} as
 * {@link capturing_logger.CapturedLog captured logs}.
 * <p>
 * These can be used by tests to construct appropriate assertions.
 * </p>
 */
package capturing_logger;