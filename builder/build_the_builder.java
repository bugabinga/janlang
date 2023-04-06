import static java.lang.System.*;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.spi.*;

final class build_the_builder {
    private static final int SUCCESS_EXIT_CODE = 0;
    private static final int NO_JAVAC_EXIT_CODE = 99;
    private static final int NO_JLINK_EXIT_CODE = 88;
    private static final int NO_JPACKAGE_EXIT_CODE = 77;
    private static final int JAVAC_FAIL_EXIT_CODE = 666;
    private static final int DELETE_FAIL_CODE = 132;

    private build_the_builder() {
    }

    public static void main(final String[] __) {
        final var start = Instant.now();
        out.println("BUILDING!");
        ToolProvider.findFirst("javac")
                .ifPresentOrElse(javac -> {

                    /*
                     * Use these options to learn more about javac
                     * javac.run(out, err, "--help");
                     * javac.run(out, err, "--help-extra");
                     * javac.run(out, err, "--help-lint");
                     */

                    final var javac_exit_code = javac.run(out, err,
                            // put class files into
                            // `output/bytecode` folder
                            "-d", "output/modules",
                            // force UTF8
                            "-encoding", "UTF-8",
                            // the place our module
                            // `net.bugabinga.janlang.builder`
                            // lives
                            "--module-source-path", "net.bugabinga.janlang.builder=source",
                            // module to compile
                            "--module", "net.bugabinga.janlang.builder",
                            // set java version
                            "--release", "17",
                            // generate debug information in class
                            // files
                            "-g",
                            // show deprecated code
                            "-deprecation",
                            // kill build on warnings
                            "-Werror",
                            // be strict about javadocs
                            "-Xdoclint:all",
                            // be strict with linting
                            "-Xlint:all",
                            // ensure consistent use of
                            // package-info.java
                            "-Xpkginfo:always",
                            // log a lot
                            // "-verbose",
                            // as long as we do not have our own
                            // processor, use none
                            "-proc:none");
                    // TODO use my throw pattern
                    if (javac_exit_code != 0) exit(JAVAC_FAIL_EXIT_CODE);
                }, () -> terminate(NO_JAVAC_EXIT_CODE, "NO JAVAC FOUND"));
        ToolProvider.findFirst("jlink")
                .ifPresentOrElse(jlink -> {
                    /*
                     * Use these to learn more about jlink options.
                     * jlink.run(out, err, "--help");
                     * jlink.run(out, err, "--list-plugins");
                     */

                    deleteDirectory(Paths.get("output/images/builder"));
                    jlink.run(out, err,
                            // place where compiled modules live
                            "--module-path", "output/modules", // TODO constants
                            // add our module
                            "--add-modules", "net.bugabinga.janlang.builder",
                            // place executable here
                            "--output", "output/images/builder",
                            // link to our issues page
                            "--vendor-bug-url", "https://github.com/bugabinga/janlang/issues",
                            // ZIP compress resources
                            "--compress=2",
                            // remove debug symbols from our code
                            "--strip-debug",
                            // remove debug symbols from class files
                            "--strip-java-debug-attributes",
                            // remove jdk tools
                            "--strip-native-commands",
                            // remove jdk header files
                            "--no-header-files",
                            // remove jdk man files
                            "--no-man-pages");
                }, () -> terminate(NO_JLINK_EXIT_CODE, "NO JLINK FOUND"));
        ToolProvider.findFirst("jpackage")
                .ifPresentOrElse(jpackage -> {
                    // jpackage.run(out, err, "--help");
                    deleteDirectory(Paths.get("output/packages/builder"));
                    jpackage.run(out, err,
                            // set a name
                            "--name", "builder",
                            // type of package to build
                            "--type", "app-image",
                            // entry point
                            "--module",
                            "net.bugabinga.janlang.builder/net.bugabinga.janlang.builder.CommandLineInterface",
                            // use the previously built jlink image
                            "--runtime-image", "output/images/builder",
                            // where to save the app
                            "--dest", "output/packages",
                            // metadata
                            "--app-version", "0", // TODO read from somewhere?
                            "--copyright", "© bugabinga.net 2022", // TODO generate
                            // year
                            "--description", "Builds the janlang project into an application.",
                            // app icon
                            "--icon", "source/icon.png",
                            // that would be us
                            "--vendor", "bugabinga.net",
                            // link to homepage
                            // "--about-url","https://github.com/bugabinga/janlang",
                            // TODO we need a licence
                            // "--license-file","../LICENCE",
                            //
                            "--verbose");
                }, () -> terminate(NO_JPACKAGE_EXIT_CODE, "NO JPACKAGE FOUND"));
        final long duration = Duration.between(start, Instant.now())
                .toMillis();
        terminate(SUCCESS_EXIT_CODE, "EXEC TIME: %dms", duration);
    }

    private static void deleteDirectory(final Path path_to_delete) {
        if (!Files.exists(path_to_delete)) return;
        try (final var file_tree = Files.walk(path_to_delete)) {
            file_tree
                    .sorted(Comparator.reverseOrder())
                    .forEachOrdered(build_the_builder::delete);
        } catch (final IOException __) {
            terminate(DELETE_FAIL_CODE,
                    "path %s could not be walked!".formatted(path_to_delete));
        }
    }

    private static void delete(final Path path) {
        try {
            Files.delete(path);
        } catch (final IOException __) {
            terminate(DELETE_FAIL_CODE, "path %s could not be deleted!".formatted(path));
        }
    }

    private static void terminate(final int code, final String message_format, final Object... arguments) {
        err.printf(message_format, arguments);
        exit(code);
    }
}
