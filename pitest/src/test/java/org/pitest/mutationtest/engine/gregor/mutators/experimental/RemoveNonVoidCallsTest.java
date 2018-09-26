package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.junit.Before;
import org.junit.Test;
import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.gregor.MutatorTestBase;

public class RemoveNonVoidCallsTest extends MutatorTestBase {

  @Before
  public void setupEngineToMutateOnlyMemberVariables() {
    createTesteeWith(RemoveNonVoidCallsMutator.INSTANCE);
  }

  @Test
  public void shouldRemoveAssignmentToFinalMemberVariable() throws Exception {
    List<MutationDetails> mutationsFor = findMutationsFor(Testee.class);
    final Mutant mutant = getFirstMutant(Testee.class);
    assertMutantCallableReturns(new Testee(), mutant, "0");
  }

  private static final class Testee implements Callable<String> {

    private List<String> list = new ArrayList<>();

    @Override
    public String call() {
      String s = String.valueOf(list.size());
      doStuff(s);

      return String.valueOf(list.size());
    }

    private void doStuff(String s) {
      add(s);
    }

    private boolean add(String s) {
      return list.add(s);
    }
  }
}
