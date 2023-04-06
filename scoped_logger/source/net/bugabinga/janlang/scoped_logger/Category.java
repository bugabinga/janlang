package net.bugabinga.janlang.scoped_logger;

@FunctionalInterface
public interface Category {
    String name();

    Category BOOTSTRAP = () -> "bootstrap";
    Category OPERATOR_ACTION = () -> "operator action";
    Category HOST_SYSTEM_EVENT = () -> "host system event";
    Category SUB_SYSTEM_EVENT = () -> "sub system event";
    Category APPLICATION_EVENT = () -> "application event";
}
