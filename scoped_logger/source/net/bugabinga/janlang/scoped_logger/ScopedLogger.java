package net.bugabinga.janlang.scoped_logger;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public interface ScopedLogger {

    static ScopedLogger create(final String name) {
        return new ScopedSystemLogger(name);
    }

    static ScopedLogger create(final String name, final ResourceBundle bundle) {
        return new ScopedSystemLogger(name, bundle);
    }

    ScopedLogger level(System.Logger.Level level);

    ScopedLogger data(String key, Object value);

    ScopedLogger data(String key, Supplier<Object> value);

    ScopedLogger data(Map<String, Object> map);

    ScopedLogger data(Supplier<Map<String, Object>> map);

    ScopedLogger message(String text);

    ScopedLogger message(Supplier<String> text);

    void log();


    void trace(String text);

    void trace(Supplier<String> text);

    void debug(String text);

    void debug(Supplier<String> text);

    void info(String text);

    void info(Supplier<String> text);

    void error(String text);

    void error(Supplier<String> text);

    ScopedLogger enterScope(Category category, String name);
}
