package net.bugabinga.janlang.builder;

import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Formatter;

/**
 * CLI for the custom build system of janlang.
 *
 * @author Oliver Jan Krylow
 */
public final class CommandLineInterface {
    /**
     * Gets returned to the OS to mean: "everything went ok".
     */
    private static final int SUCCESS_EXIT_CODE = 0;

    /**
     * Hidden, so that only {@link #main(String[])} can be used.
     */
    private CommandLineInterface() {
    }

    /**
     * @param arguments Command line arguments as given by the `main` method.
     */
    public static void main(final String[] arguments) {
        final var start = Instant.now();
        out.printf("builder cli: %s%n", Arrays.toString(arguments));
        out.println("TODO: parse arguments to find `check`");
        out.println(
                "TODO: how to declare the build model? all in code? some DSL? some new language?");
        out.println("TODO: implement check -> parsing of a module");
        final long duration_in_ms = Duration.between(start, Instant.now())
                .toMillis();
        terminate(SUCCESS_EXIT_CODE, "wall time: %dms", duration_in_ms);
    }

    /**
     * Terminates the program via {@link System#exit(int)}.
     * Prior to that, a formatted message gets printed onto the {@link System#err
     * error stream}.
     *
     * @param code           arbitrary signed integer value. {@code 0} means
     *                       success. The meaning of other values can be assigned
     *                       freely.
     * @param message_format A format string, that will be printed on the
     *                       {@link System#err error stream}.
     * @param arguments      Values, that will be used by the
     *                       {@code message_format}.
     * @see Formatter#format(String, Object...)
     */
    private static void terminate(
            final int code,
            final String message_format,
            final Object... arguments
    ) {
        err.printf(message_format, arguments);
        exit(code);
    }
}
