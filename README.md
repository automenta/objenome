# Objenome

Objenome is a dynamic and adaptive dependency injection framework.

It has a concise fluent syntax that simplifies everything, so that you can
enjoy the vast possibilities that its non-deterministic, ambiguity-tolerant model can generate.

It is derived from Dagger v1.0 (not 2.0).

----

# How it works

For a complete example, see the Coffee Maker example in the test package 'objenome.example.simple'.

## Implementating

### @the(...) class Something { ...

Indicates a class is to be automatically configured by the dependencies specified by this annotation's
parameters and the appropriately annotated class fields, methods, and constructor parameters.  (Replaces @Module)

### @in Constructor(Object parameter) {
### @in void setInstance(Object parameter) { ...
### @in Object field;

Constructor, method, or field annotation indicating a dependency target.  (Replaces @Inject)

### @out Object anInstance() { ...

Value exported via a method's return value.  (Replaces @Provide)

## Instantiating

The "O" utility class's static methods create the dependency factories that build instances.

```java
    CoffeeApp x = O.of( DripCoffeeModule.class ).a(CoffeeApp.class);
```

or

```java
    CoffeeApp x = O.of( new DripCoffeeModule() ).a(CoffeeApp.class);
```

'nuff said.

----

More documentation coming soon, in the mean-time, see Dagger 1.0's.
