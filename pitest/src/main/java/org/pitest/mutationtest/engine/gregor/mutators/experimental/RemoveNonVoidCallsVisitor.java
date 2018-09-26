package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.POP2;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public class RemoveNonVoidCallsVisitor extends MethodVisitor {

  private final MutationContext context;
  private final MethodMutatorFactory factory;

  private MethodInstruction latestInvoke;

  public RemoveNonVoidCallsVisitor(MutationContext context, MethodVisitor methodVisitor,
      MethodMutatorFactory factory) {
    super(Opcodes.ASM6, methodVisitor);
    this.context = context;
    this.factory = factory;
  }

  @Override
  public void visitParameter(String name, int access) {
    clearIfSet();
    super.visitParameter(name, access);
  }

  @Override
  public AnnotationVisitor visitAnnotationDefault() {
    clearIfSet();
    return super.visitAnnotationDefault();
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    clearIfSet();
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor,
      boolean visible) {
    clearIfSet();
    return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
    clearIfSet();
    super.visitAnnotableParameterCount(parameterCount, visible);
  }

  @Override
  public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor,
      boolean visible) {
    clearIfSet();
    return super.visitParameterAnnotation(parameter, descriptor, visible);
  }

  @Override
  public void visitAttribute(Attribute attribute) {
    clearIfSet();
    super.visitAttribute(attribute);
  }

  @Override
  public void visitCode() {
    clearIfSet();
    super.visitCode();
  }

  @Override
  public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
    clearIfSet();
    super.visitFrame(type, nLocal, local, nStack, stack);
  }

  @Override
  public void visitInsn(int opcode) {
    if (latestInvoke != null) {
      if (Opcodes.POP == opcode || Opcodes.POP2 == opcode) {
        String description = String
            .format("Removed call to method %s.%s", latestInvoke.owner, latestInvoke.name);
        MutationIdentifier identifier = context.registerMutation(factory, description);
        if (context.shouldMutate(identifier)) {
          popStack(latestInvoke.descriptor, latestInvoke.name);
          popThisIfNotStatic(latestInvoke.opcode);
          latestInvoke = null;
          return;
        }
      }

      clearIfSet();
      this.mv.visitInsn(opcode);
    } else {
      super.visitInsn(opcode);
    }
  }

  @Override
  public void visitIntInsn(int opcode, int operand) {
    clearIfSet();
    super.visitIntInsn(opcode, operand);
  }

  @Override
  public void visitVarInsn(int opcode, int var) {
    clearIfSet();
    super.visitVarInsn(opcode, var);
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    clearIfSet();
    super.visitTypeInsn(opcode, type);
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
    clearIfSet();
    super.visitFieldInsn(opcode, owner, name, descriptor);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
      boolean isInterface) {
    clearIfSet();

    if (MethodInfo.isVoid(descriptor) || isCallToSuperOrOwnConstructor(name, owner)) {
      this.mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    } else {
      latestInvoke = new MethodInstruction(opcode, owner, name, descriptor, isInterface);
    }
  }

  private void popThisIfNotStatic(final int opcode) {
    if (!isStatic(opcode)) {
      this.mv.visitInsn(POP);
    }
  }

  private static boolean isStatic(final int opcode) {
    return INVOKESTATIC == opcode;
  }

  private void popStack(final String desc, final String name) {
    final Type[] argTypes = Type.getArgumentTypes(desc);
    for (int i = argTypes.length - 1; i >= 0; i--) {
      final Type argumentType = argTypes[i];
      if (argumentType.getSize() != 1) {
        this.mv.visitInsn(POP2);
      } else {
        this.mv.visitInsn(POP);
      }
    }

    if (MethodInfo.isConstructor(name)) {
      this.mv.visitInsn(POP);
    }
  }

  private boolean isCallToSuperOrOwnConstructor(final String name,
      final String owner) {
    return MethodInfo.isConstructor(name)
        && (owner.equals(this.context.getClassInfo().getName())
        || this.context.getClassInfo().getSuperName().equals(owner));
  }

  private void clearIfSet() {
    if (latestInvoke != null) {
      this.mv.visitMethodInsn(
          latestInvoke.opcode,
          latestInvoke.owner,
          latestInvoke.name,
          latestInvoke.descriptor,
          latestInvoke.isInterface
      );
      latestInvoke = null;
    }
  }

  @Override
  public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
      Object... bootstrapMethodArguments) {
    clearIfSet();
    super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
  }

  @Override
  public void visitJumpInsn(int opcode, Label label) {
    clearIfSet();
    super.visitJumpInsn(opcode, label);
  }

  @Override
  public void visitLabel(Label label) {
    clearIfSet();
    super.visitLabel(label);
  }

  @Override
  public void visitLdcInsn(Object value) {
    clearIfSet();
    super.visitLdcInsn(value);
  }

  @Override
  public void visitIincInsn(int var, int increment) {
    clearIfSet();
    super.visitIincInsn(var, increment);
  }

  @Override
  public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    clearIfSet();
    super.visitTableSwitchInsn(min, max, dflt, labels);
  }

  @Override
  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    clearIfSet();
    super.visitLookupSwitchInsn(dflt, keys, labels);
  }

  @Override
  public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    clearIfSet();
    super.visitMultiANewArrayInsn(descriptor, numDimensions);
  }

  @Override
  public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor,
      boolean visible) {
    clearIfSet();
    return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    clearIfSet();
    super.visitTryCatchBlock(start, end, handler, type);
  }

  @Override
  public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath,
      String descriptor, boolean visible) {
    clearIfSet();
    return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
  }

  @Override
  public void visitLocalVariable(String name, String descriptor, String signature, Label start,
      Label end, int index) {
    clearIfSet();
    super.visitLocalVariable(name, descriptor, signature, start, end, index);
  }

  @Override
  public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath,
      Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
    clearIfSet();
    return super
        .visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    clearIfSet();
    super.visitLineNumber(line, start);
  }

  @Override
  public void visitMaxs(int maxStack, int maxLocals) {
    clearIfSet();
    super.visitMaxs(maxStack, maxLocals);
  }

  @Override
  public void visitEnd() {
    clearIfSet();
    super.visitEnd();
  }

  private static final class MethodInstruction {

    int opcode;
    String owner;
    String name;
    String descriptor;
    boolean isInterface;

    MethodInstruction(int opcode, String owner, String name, String descriptor,
        boolean isInterface) {
      this.opcode = opcode;
      this.owner = owner;
      this.name = name;
      this.descriptor = descriptor;
      this.isInterface = isInterface;
    }
  }
}
