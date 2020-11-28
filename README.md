# Java-Reselect

Experimental reimplementation of [Reselect](https://github.com/reduxjs/reselect)
in Java.

```java
Function<State, Integer> orangeSelector = (State s) -> s.oranges;
Function<State, Integer> applesSelector = (State s) -> s.apples;

Function<State, String> selector = Reselect.createCachedSelector(
  orangeSelector,
  applesSelector,
  (oranges, apples) -> oranges + " oranges " + apples + " apples (#" + counter.incrementAndGet() + ")");
```

There are `createCachedSelector` overloads taking 1 or 2 selectors.

For an arbitrary number of selectors, the order of arguments is inverted 
compared to *Reselect* so we can benefit from varargs. Then only issue is
that parameters need a cast, e.g.

```java
    Function<State, String> selector = Reselect.createCachedSelector(
      (Object[] arr) -> {
        Integer oranges = (Integer) arr[0];
        Integer apples = (Integer) arr[1];
        Integer bananaSelector = (Integer) arr[2];
        return ...
      },
      orangeSelector,
      applesSelector,
      bananaSelector,
      ...);
);
```
