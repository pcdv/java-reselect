package com.github.pcdv.reselect;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author pcdv
 */
class ReselectTest {

  static class State implements Cloneable {
    int apples, oranges;

    @Override
    public State clone() {
      try {
        return (State) super.clone();
      }
      catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private final Function<State, Integer> orangeSelector = (State s) -> s.oranges;

  private final Function<State, Integer> applesSelector = (State s) -> s.apples;

  @Test
  void selector() {
    State state = new State();
    AtomicInteger counter = new AtomicInteger();

    Function<State, Integer> orangeSelector = (State s) -> s.oranges;

    Function<State, String> selector = Reselect.createCachedSelector(
      orangeSelector,
      n -> n + " oranges (#" + counter.incrementAndGet() + ")");

    assertEquals("0 oranges (#1)", selector.apply(state));
    assertEquals("0 oranges (#1)", selector.apply(state));

    state = state.clone();
    state.oranges = 12;

    assertEquals("12 oranges (#2)", selector.apply(state));
    assertEquals("12 oranges (#2)", selector.apply(state));
    state = state.clone();
    assertEquals("12 oranges (#2)", selector.apply(state));
  }

  @Test
  void selector2() {
    AtomicInteger counter = new AtomicInteger();
    Function<State, String> selector = Reselect.createCachedSelector(
      orangeSelector,
      applesSelector,
      (oranges, apples) -> oranges + " oranges " + apples + " apples (#" + counter.incrementAndGet() + ")");

    testSelector(selector);
  }

  @SuppressWarnings("unchecked")
  @Test
  void selectorN() {
    AtomicInteger counter = new AtomicInteger();
    Function<State, String> selector = Reselect.createCachedSelector(
      (Object[] arr) -> {
        Integer oranges = (Integer) arr[0];
        Integer apples = (Integer) arr[1];
        return oranges + " oranges " + apples + " apples (#" + counter.incrementAndGet() + ")";
      },
      orangeSelector,
      applesSelector);

    testSelector(selector);
  }

  private void testSelector(Function<State, String> selector) {
    State state = new State();
    assertEquals("0 oranges 0 apples (#1)", selector.apply(state));
    assertEquals("0 oranges 0 apples (#1)", selector.apply(state));

    state = state.clone();
    state.oranges = 12;

    assertEquals("12 oranges 0 apples (#2)", selector.apply(state));
    assertEquals("12 oranges 0 apples (#2)", selector.apply(state));

    state = state.clone();
    assertEquals("12 oranges 0 apples (#2)", selector.apply(state));
    assertEquals("12 oranges 0 apples (#2)", selector.apply(state));

    state = state.clone();
    state.apples = 3;
    assertEquals("12 oranges 3 apples (#3)", selector.apply(state));
    assertEquals("12 oranges 3 apples (#3)", selector.apply(state));
  }
}