/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.simpletest.steps;

import java.lang.reflect.Modifier;
import java.util.Objects;

import org.pitest.simpletest.CanNotCreateTestClassException;
import org.pitest.simpletest.TestStep;
import org.pitest.testapi.Description;

/**
 * @author henry
 *
 */
public final class NoArgsInstantiateStep implements TestStep {

  private final Class<?> clazz;

  public static NoArgsInstantiateStep instantiate(final Class<?> clazz) {
    return new NoArgsInstantiateStep(clazz);
  }

  public NoArgsInstantiateStep(final Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public Object execute(final Description testDescription, final Object target) {
    try {
      final Class<?> c = this.clazz;
      if (Modifier.isAbstract(c.getModifiers())) {
        throw new CanNotCreateTestClassException(
            "Cannot instantiate the abstract class " + c.getName(), null);
      } else {
        return c.getDeclaredConstructor().newInstance();
      }
    } catch (final Throwable e) {
      e.printStackTrace();
      throw new CanNotCreateTestClassException(e.getMessage(), e);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(clazz);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final NoArgsInstantiateStep other = (NoArgsInstantiateStep) obj;
    return Objects.equals(clazz, other.clazz);
  }
}
