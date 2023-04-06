import capturing_logger.CapturedLog;
import capturing_logger.CapturingLogger;
import net.bugabinga.janlang.scoped_logger.ScopedLogger;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.INFO;

interface TestSuite {
    static void main(final String[] __) {
        test_creation_of_scoped_logger();
        test_that_simple_info_log_sends_message();
    }

    static void test_that_simple_info_log_sends_message() {
        final var logger_name = "♥";
        final var scoped = ScopedLogger.create(logger_name);
        final var message = "Hi!";
        scoped.info(message);
        assertLoggerLogsSize(logger_name, 1);
        assertLoggerContainsLevel(logger_name, INFO);
        assertLoggerContainsMessage(logger_name, message);

        final Supplier<String> lazy_message = () -> "pretend i was costly to construct ;)";
        scoped.info(lazy_message);
        assertLoggerLogsSize(logger_name, 2);
        assertLoggerContainsMessage(logger_name, lazy_message.get());

        final var message_message = "different api, same result";
        scoped.level(INFO).message(message_message).log();
        assertLoggerLogsSize(logger_name, 3);
        assertLoggerContainsMessage(logger_name, message_message);

        final var message_no_level = "default level should be INFO";
        scoped.message(message_no_level).log();
        assertLoggerLogsSize(logger_name, 4);
        assertLoggerContainsOnlyLevel(logger_name, INFO);
        assertLoggerContainsMessage(logger_name, message_no_level);
    }

    static void test_creation_of_scoped_logger() {
        final var logger_name = "♥";
        final var scoped = ScopedLogger.create(logger_name);

        final var scoped_to_string = scoped.toString();
        assert scoped_to_string.contains(logger_name) : scoped_to_string;

        assertLoggerExists(logger_name);
    }

    static void assertLoggerExists(final String logger_name) {
        try {
            final var captured = getCapturingLogger(logger_name);
            assert captured != null;
        } catch (final ClassCastException __) {
            assert false;
        }
    }

    static void assertLoggerLogsSize(final String logger_name, final int expected_size) {
        final var captured = getCapturingLogger(logger_name);
        final int actual_size = captured.logs().size();
        assert actual_size == expected_size : "%n\texpected: %d%n\tactual: %d".formatted(expected_size, actual_size);
    }

    static void assertLoggerContains(final String logger_name, final Object expected, final Function<CapturedLog, ?> mapper) {
        final var captured = getCapturingLogger(logger_name);
        assert captured.logs()
                .stream()
                .map(mapper)
                .anyMatch(value -> Objects.equals(value, expected))
                : "%n\texpected: %s%n\tactual: %s"
                .formatted(
                        expected,
                        captured.logs()
                                .stream()
                                .map(mapper)
                                .toList()
                );
    }

    static void assertLoggerContainsMessage(final String logger_name, final String expected_message) {
        assertLoggerContains(logger_name, expected_message, CapturedLog::msg);
    }

    static void assertLoggerContainsLevel(final String logger_name, final System.Logger.Level expected_level) {
        assertLoggerContains(logger_name, expected_level, CapturedLog::level);
    }

    static void assertLoggerContainsOnly(final String logger_name, final Object expected, final Function<CapturedLog, ?> mapper) {
        final var captured = getCapturingLogger(logger_name);
        assert captured.logs()
                .stream()
                .map(mapper)
                .allMatch(value -> Objects.equals(value, expected))
                : "%n\texpected: %s%n\tactual: %s"
                .formatted(
                        expected,
                        captured.logs()
                                .stream()
                                .map(mapper)
                                .toList()
                );
    }

    static void assertLoggerContainsOnlyMessage(final String logger_name, final String expected_message) {
        assertLoggerContainsOnly(logger_name, expected_message, CapturedLog::msg);
    }

    static void assertLoggerContainsOnlyLevel(final String logger_name, final System.Logger.Level expected_level) {
        assertLoggerContainsOnly(logger_name, expected_level, CapturedLog::level);
    }

    private static CapturingLogger getCapturingLogger(final String logger_name) {
        return (CapturingLogger) System.getLogger(logger_name);
    }
}
