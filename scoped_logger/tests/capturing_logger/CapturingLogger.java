package capturing_logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CapturingLogger implements System.Logger {
    private final String name;
    private final Deque<CapturedLog> logs;

    CapturingLogger(final String name) {
        this.name = name;
        logs = new ConcurrentLinkedDeque<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(final Level __) {
        return true;
    }

    @Override
    public void log(final Level level, final ResourceBundle bundle, final String msg, final Throwable thrown) {
        logs.add(new CapturedLog(level, bundle, msg, thrown));
    }

    @Override
    public void log(final Level level, final ResourceBundle bundle, final String format, final Object... params) {
        logs.add(new CapturedLog(level, bundle, format, null, params));
    }

    public Collection<CapturedLog> logs() {
        return Collections.unmodifiableCollection(logs);
    }
}
