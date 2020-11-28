package com.github.pcdv.reselect;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author pcdv
 */
class Memoize1<R1, T> implements Function<R1, T> {

  private final Function<R1, T> function;

  private final BiPredicate<R1, R1> equals;

  private R1 lastArg;

  private T lastResult;

  private boolean neverCalled = true;

  public Memoize1(Function<R1, T> function, BiPredicate<R1, R1> equals) {
    this.function = function;
    this.equals = equals;
  }

  @Override
  public T apply(R1 arg) {
    if (neverCalled || !equals.test(lastArg, arg)) {
      lastResult = function.apply(arg);
      lastArg = arg;
      neverCalled = false;
    }
    return lastResult;
  }
}
