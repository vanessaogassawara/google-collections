/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet.ArrayImmutableSet;

/**
 * Implementation of {@link ImmutableSet} with two or more elements.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(serializable = true)
final class RegularImmutableSet<E> extends ArrayImmutableSet<E> {
   // the same elements in hashed positions (plus nulls)
  private final Object[] table;
  private final int mask; // 'and' with an int to get a valid table index
  private final int hashCode;

  RegularImmutableSet(
      Object[] elements, int hashCode, Object[] table, int mask) {
    super(elements);
    this.table = table;
    this.mask = mask;
    this.hashCode = hashCode;
  }

  @Override public boolean contains(Object target) {
    if (target == null) {
      return false;
    }
    for (int i = Hashing.smear(target.hashCode()); true; i++) {
      Object candidate = table[i & mask];
      if (candidate == null) {
        return false;
      }
      if (candidate.equals(target)) {
        return true;
      }
    }
  }

  @Override public int hashCode() {
    return hashCode;
  }

  @Override boolean isHashCodeFast() {
    return true;
  }
}