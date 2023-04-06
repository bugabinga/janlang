package net.bugabinga.janlang.scoped_logger;

import java.lang.System.Logger.Level;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.*;
import static java.util.stream.Collectors.joining;

class ScopedSystemLogger implements ScopedLogger {
    private final System.Logger system_logger;
    private final Collection<Supplier<String>> messages = new ConcurrentLinkedDeque<>();
    private final Collection<Supplier<Map<String, Object>>> lazy_data = new ConcurrentLinkedDeque<>();
    private final Map<String, Supplier<Object>> data = new ConcurrentHashMap<>();
    private final AtomicReference<Level> level = new AtomicReference<>(INFO);

    ScopedSystemLogger(final String name, final ResourceBundle bundle) {
        system_logger = System.getLogger(name, bundle);
    }

    ScopedSystemLogger(final String name) {
        system_logger = System.getLogger(name);
    }

    @Override
    public ScopedLogger level(final Level level) {
        this.level.set(level);
        return this;
    }

    @Override
    public ScopedLogger data(final String key, final Object value) {
        data.put(key, () -> value);
        return this;
    }

    @Override
    public ScopedLogger data(final String key, final Supplier<Object> value) {
        data.put(key, value);
        return this;
    }

    @Override
    public ScopedLogger data(final Map<String, Object> map) {
        map.forEach((key, value) -> data.put(key, () -> value));
        return this;
    }

    @Override
    public ScopedLogger data(final Supplier<Map<String, Object>> map) {
        lazy_data.add(map);
        return this;
    }

    @Override
    public ScopedLogger message(final String text) {
        messages.add(() -> text);
        return this;
    }

    @Override
    public ScopedLogger message(final Supplier<String> text) {
        messages.add(text);
        return this;
    }

    @Override
    public void log() {
        final var constructed_message = messages.stream()
                .map(Supplier::get)
                .collect(joining(System.lineSeparator()));

        final var constructed_data =

        system_logger.log(level.get(), constructed_message);
    }

    @Override
    public void trace(final String text) {
        level(TRACE);
        message(text);
        log();
    }

    @Override
    public void trace(final Supplier<String> text) {
        level(TRACE);
        message(text);
        log();
    }

    @Override
    public void debug(final String text) {
        level(DEBUG);
        message(text);
        log();
    }

    @Override
    public void debug(final Supplier<String> text) {
        level(DEBUG);
        message(text);
        log();
    }

    @Override
    public void info(final String text) {
        level(INFO);
        message(text);
        log();
    }

    @Override
    public void info(final Supplier<String> text) {
        level(INFO);
        message(text);
        log();
    }

    @Override
    public void error(final String text) {
        level(ERROR);
        message(text);
        log();
    }

    @Override
    public void error(final Supplier<String> text) {
        level(ERROR);
        message(text);
        log();
    }

    @Override
    public ScopedLogger enterScope(final Category category, final String name) {
        return this;
    }

    @Override
    public String toString() {
        return "ScopedSystemLogger{name=%s}".formatted(system_logger.getName());
    }
}
