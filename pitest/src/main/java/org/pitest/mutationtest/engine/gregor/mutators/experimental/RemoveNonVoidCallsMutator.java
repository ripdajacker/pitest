package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import org.objectweb.asm.MethodVisitor;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public enum RemoveNonVoidCallsMutator implements MethodMutatorFactory {
  INSTANCE;

  @Override
  public MethodVisitor create(MutationContext context, MethodInfo methodInfo,
      MethodVisitor methodVisitor) {
    return new RemoveNonVoidCallsVisitor(context, methodVisitor, this);
  }

  @Override
  public String getGloballyUniqueId() {
    return "EXPERIMENTAL_REMOVE_NON_VOID_CALLS";
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }
}
