Contains projects owned and maintained by The Logic Lab.

The following are [Maven](http://maven.apache.org/)-driven code modules providing particular functionality.  The inter-dependencies are intended to be as minimal as possible and are considered to be "loosely coupled".

## tll-core ##
_Core functionality mainly utility routines:_

  * String
  * Enum
  * Collection
  * Crypto
  * Reflection (find classes under a package name)
  * Validation

## tll-config ##
_Loads one or more property files from disk._

This module extends Apache's [commons configuration project](http://commons.apache.org/configuration) with additional functionality:

  * Ability to load multiple property files into a single in memory `Config` instance.
  * Extract properties from a loaded `Config` instance to a `Map<String, String>` instance allowing for property filtering.
  * Save loaded properties to disk allowing for property filtering.

## tll-mail ##
_Dispatches emails programmatically._

Programmatic email assembly and distribution using Spring and javax.mail via a simplified api.

## tll-schema ##
_Processes one or more classes providing model property metadata._

  * Schema annotations enabling the loading of schema related information on entity properties.  The main use of this information is to decorate model data when sent to the client for client-side validation.

## tll-model ##
_Base and helper classes for defining application entities._

  * Business key annotations which enable checking of business key constraints before entities are persisted.
  * Validation support based on [Hibernate Validator (JSR 303)](https://www.hibernate.org/412.html).
  * Single entity methods for setting both sides of a bi-directional entity relationship.
  * `EntityFactory` that handles eager setting of entity ids at time of object creation.
  * `EntityGraph` and `IEntityGraphBuilder` for easily creating entity object graphs useful for testing.

## tll-dao ##
_Data persistence interfaces for basic CRUD ops and large result set retrieval via paging routines._

## tll-service ##
_Level of abstraction that sits directly above the dao layer supporting full @Transaction support._

Transactions are realized via Spring's `@Transactional` via [AspectJ](http://www.eclipse.org/aspectj/) Compile Time Weaving having _no_ dependence on Spring's application context.

## tll-servlet ##
_Core servlet functionality._

  * `WebClientCacheFilter` which adds caching directives to an `HttpServletResponse` configured by adding declarative caching directives in the application's `web.xml` file.

## client-ui ##
_Provides extended UI related functionality for GWT._

This includes in part:
  * "Global" Message display via a message panel able to display messages categorized by severity and origin (client or server).
  * "Local" message display via tooltip type messages triggered by mouse hovering.
  * Option panel supporting the selection of a single option via mouse click.
  * Toolbar panel which displays images triggering onvaluechange events.

## client-view ##
_A way to create and manage views in a GWT context._

This view functionality includes:
  * View caching (configurable).
  * Poppable views. A popped is removed from the DOM layout flow and is draggable.
  * View refreshing where data is re-retrieved from its data source including server side fetching via GWT RPC.

## client-field ##
_A way to present and modify model data in a GWT context._

This functionality includes:
  * Model/field data binding supporting related one and _related many_ bindings.
  * Field level validation both single field and intra-field (multiple fields) validation.
  * Local and global validation feedback message display.

## client-persist ##
_Provides CRUD functionality in a GWT context of an application's model data (entities)._

## client-listing ##
_Paging functionality for large collections of application/model data in a GWT context._

## client-login ##
_Login/logout functionality in a GWT context based on [Spring Security](http://www.acegisecurity.org/)._


---


## smbiz ##
_Small Business Web Administration Interface_

JEE Web 2.0 prototype app using the latest and best of breed open source frameworks.

Administrative interface for managing small business B2C processes.