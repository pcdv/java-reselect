package com.github.pcdv.reselect;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemoizeTest {

  @Test
  void memoize1() {
    AtomicInteger counter = new AtomicInteger();

    Function<String, Integer> memo =
      Reselect.memoize((String s) -> counter.incrementAndGet());

    assertEquals(1, memo.apply("A"));
    assertEquals(1, memo.apply("A"));
    assertEquals(2, memo.apply("B"));
    assertEquals(2, memo.apply("B"));
    assertEquals(3, memo.apply("A"));
  }

  @Test
  void memoize2() {
    AtomicInteger counter = new AtomicInteger();

    BiFunction<String, String, Integer> memo =
      Reselect.memoize((String s, String t) -> counter.incrementAndGet());

    assertEquals(1, memo.apply("A", "A"));
    assertEquals(1, memo.apply("A", "A"));
    assertEquals(2, memo.apply("B", "A"));
    assertEquals(2, memo.apply("B", "A"));
    assertEquals(3, memo.apply("B", "B"));
    assertEquals(3, memo.apply("B", "B"));
  }
}