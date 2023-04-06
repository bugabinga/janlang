goals of this architecture are:
- unlimited Undo/Redo
- replayable session
- scoped logging
- testable
- segmented code for
	- gui Components
		- gui + Layout
		- logic ( responses to input, validation, sync with persistence)
		- lifecycle
		- api to persistence
	- gui Component Wiring
	- static application frame
	- persistence + data
	- resources

# scoped logging
make it possible, via a small bit of state (uuid), to track a trail of events in logs.
if an operator for example clicks a button, it might generate a new "log scope".
this scope, which contains a uuid, will henceworth be passed along, so that other log statements can include it.
later, the logs can be filtered by that uuid, in order to view a trail of logs, that where caused by the operators click.
scopes can be nested.
loggers log structured messages.
loggers persistence is shared with application.

## scoped log data structure
- uuid
- category (bootstrap, operator action, system event, app event)
- time of creation
- name of event
## creating loggers and scopes
loggers are created via a factory and injected into the parts of code, that need them (almost all?).
```java
final var logger = new ScopedLogger( "gui components" );
// pass logger to some module
final var components = new Components( logger, ... );
```
log structured messages via a fluent api:
```java
logger.level( Level.INFO )
	  .data( "key", some_value )
	  .data( "other key", some_other_value )
	  .message( "something important happened" )
	  .log();
// or
logger.data("key", some_value )
	  .data("other key", some_other_value )
	  .info("something important happened" );
```
enter a new scope by calling a method on the logger:
```java
Components( ScopedLogger logger, ...)
{
	logger.info("begin wiring of components");
	try ( final var wire_logger = logger.enterScope( Category.BOOT, "wire gui components together" ))
	{
		final var root = new RootComponent( wire_logger, ... );
		wire_logger.data( "name", root.name ).info( "created root component" );
	}
}
```
