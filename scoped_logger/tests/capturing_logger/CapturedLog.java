package capturing_logger;

import java.util.ResourceBundle;

public record CapturedLog(
        System.Logger.Level level,
        ResourceBundle bundle,
        String msg,
        Throwable thrown,
        Object... params
) {
}
