package net.bugabinga.janlang.builder;

import java.nio.file.Path;

/**
 * A build model for this special build system.
 * The model captures all initially necessary information about the project,
 * that the build system needs to run its tasks.
 *
 * @author Oliver Jan Krylow
 */
class BuildModel
{
/**
 * Metadata of the java project.
 */
Metadata metadata;
/**
 * Structural information about the java project.
 */
Project  project;

/**
 * Data about the project, that is not directly encoded into the source code.
 * This information is typically used in logs, configs, reports, documentation
 * and packages.
 *
 * @author Oliver Jan Krylow
 */
static class Metadata
{
/**
 * The human, that authored the application.
 */
Person author;
/**
 * A pretty name to render, when the application name is shown to humans.
 */
String display_name;
/**
 * A cool and consice name for the application, that is used internally (logs,
 * config files, environment variables).
 */
String code_name;

/**
 * A person in the context of the build system is some one who is engaged with
 * the project in some form.
 *
 * @author Oliver Jan Krylow
 */
static class Person
{
/**
 * First name of a human.
 */
String name;
/**
 * Middle name of a human.
 */
String middle_name;
/**
 * Surname of a human.
 */
String surname;
/**
 * A corresponding email address for the person.
 */
String email;
/**
 * A nickname for the person as typically used online.
 */
String nickname;
}
}

/**
 * Defines the basic information, that the build system needs, to build a java
 * project. This build system uses the module path exclusively.
 *
 * @author Oliver Jan Krylow
 */
static class Project
{
/**
 * Path to the root of the project on the filesystem.
 */
Path   project_root;
/**
 * Name of JPMS module, that is the entry point into the project. This module
 * should export a package, that contains a class with a main method.
 */
String main_module;
/**
 * A class with the main method, that should be inside the {@link #main_module}.
 * This class can also simply extend javafx.application.Application instead of a
 * plain main method.
 */
String main_class;
}
}
