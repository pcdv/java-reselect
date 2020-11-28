package com.github.pcdv.reselect;

import java.util.function.BiFunction;

/**
 * @author pcdv
 */
class Memoize2<R1, R2, T> implements BiFunction<R1, R2, T> {

  private final BiFunction<R1, R2, T> function;

  private boolean neverCalled =true;

  private R1 lastArg1;

  private R2 lastArg2;

  private T lastResult;

  public Memoize2(BiFunction<R1, R2, T> function) {
    this.function = function;
  }

  @Override
  public T apply(R1 arg1, R2 arg2) {
    if (neverCalled || lastArg1 != arg1 || lastArg2 != arg2) {
      lastResult = function.apply(arg1, arg2);
      lastArg1 = arg1;
      lastArg2 = arg2;
      neverCalled = false;
    }
    return lastResult;
  }
}
