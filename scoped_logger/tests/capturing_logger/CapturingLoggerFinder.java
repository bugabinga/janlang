package capturing_logger;

public class CapturingLoggerFinder extends System.LoggerFinder {
    @Override
    public System.Logger getLogger(final String name, final Module __) {
        return new CapturingLogger(name);
    }
}
