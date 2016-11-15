# Objenome

A dynamic and adaptive dependency injection (DI) container
with a concise fluent syntax that simplifies everything, so that you can
enjoy the vast possibilities that its non-deterministic, ambiguity-tolerant model can generate.

It is designed to automatically assemble software from combinations of components.

Decision ambiguities about dependencies and parameter values defines a non-deterministic
parameter space that can be searched, mutated, combined, and optimized across in order to maximize
objective design goals.

Powered by a deterministic, minimal, fluent, pure Java, no-nonsense dependency-injection container derived from
Dagger v1.0 (not 2.0).



# Usage

_For a complete example, see the Coffee Maker example in the test package 'objenome.example.simple'._

## Implementing

### @the(...)

```java
@the( extend=ParentModel.class, in={ComponentX.class,ComponentY.class} )
class MyModel { ...
```

Describes, by annotation, how a container/model/interface/module class may be automatically
configured by dependency implementations. (Replaces @Module)

### @in (dependency target)

Constructor, method, or field annotation indicating a dependency target.  (Replaces @Inject)

```java
@in Constructable(SomeObject parameter) {
@in void setParameter(SomeObject parameter) {
@in SomeObject field;
```

### @out (dependency source)

Value exported via a method's return value.  (Replaces @Provide)

```java
@outSomeObject getSomeObject() { ...
```


## Instancing

The "O" utility class's static methods create the dependency factories that build instances.

```java
    CoffeeApp x = O.of( DripCoffeeModule.class ).a(CoffeeApp.class);
```

or

```java
    CoffeeApp x = O.of( new DripCoffeeModule() ).a(CoffeeApp.class);
```

Since this is all pure Java, these configurations can be dynamically generated
and re-used in different ways at runtime.


## Ambiguity

What happens if multiple possible sources are known for a given dependency target?

What if a dependency remains unspecified?

Normally, situations involving dependency ambiguity result in a kind of failure analogous to a programming syntax error.

But let's imagine how this can be resolved, at run-time:

 * Failure (default behavior of normal DI containers)
 * Dynamically generated Menus that allow a user to decide
 * Generate all permutations (ex: and use them in parallel or sequence)
 * A heuristic or random choice, decided in a way that optimizes an objective function (see next section)


## Parameter Optimization

If we know the boundaries of a non-empty possibility space, what exactly do we hope to find in it?

Sometimes we don't know the best implementation of an interface to use for a certain situation, or the
best number to use as a parameter in a formula, but we do have an idea of an acceptable range to look for it.

Or maybe we just want to run a configuration with a given set of constant input values and collect or
stream the results.

All of these situations can be "solved" by a system which searches or permutes the parameter space.

 * Stream/batch processing of a specific set of inputs
 * Evolutionary Algorithms
 * Numerical Optimization
 * Reinforcement learning

Each of these technologies can be applied as different kinds of parameter searches.  Some are meant to be
exhaustive, some are selective due to the combinatorial explosion or high dimensionality of the model,
and some may be expected to run indefinitely and/or interactively.


----

More documentation coming soon, in the mean-time, see Dagger 1.0's.
