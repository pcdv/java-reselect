package com.github.pcdv.reselect;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Simple reimplementation of reselect in Java.
 *
 * See https://github.com/reduxjs/reselect
 */
public class Reselect {

  private static <R> BiPredicate<R, R> SAME() {
    return (a, b) -> a == b;
  }

  public static <State, R1, T> Function<State, T> createCachedSelector
    (Function<State, R1> selector,
     Function<R1, T> combiner) {
    Function<R1, T> memo = memoize(combiner);
    return memoize(state -> memo.apply(selector.apply(state)));
  }

  public static <State, R1, R2, T> Function<State, T> createCachedSelector(
    Function<State, R1> selector1,
    Function<State, R2> selector2,
    BiFunction<R1, R2, T> combiner) {
    BiFunction<R1, R2, T> memo = memoize(combiner);
    return memoize(state -> memo.apply(selector1.apply(state),
                                       selector2.apply(state)));
  }

  public static <State, T> Function<State, T> createCachedSelector
    (Function<Object[], T> combiner,
     Function<State, ?>... selectors) {

    Function<Object[], T> memo = new Memoize1<>(combiner, Arrays::equals);

    return memoize(state -> {
      Object[] params = new Object[selectors.length];
      for (int i = 0; i < params.length; i++) {
        params[i] = selectors[i].apply(state);
      }
      return memo.apply(params);
    });
  }

  /**
   * Returns a function that memorizes the results of the previous call and
   * returns them again if the same input parameters are provided.
   */
  public static <T, R1> Function<R1, T> memoize(Function<R1, T> function) {
    return new Memoize1<>(function, SAME());
  }

  /**
   * Returns a function that memorizes the results of the previous call and
   * returns them again if the same input parameters are provided.
   */
  public static <T, R1, R2> BiFunction<R1, R2, T> memoize(BiFunction<R1, R2, T> function) {
    return new Memoize2<>(function);
  }
}
