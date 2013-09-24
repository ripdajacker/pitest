package org.pitest.mutationtest.incremental;

import org.pitest.mutationtest.MutationMetaData;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationResultListener;

public class HistoryListener implements MutationResultListener {

  private final HistoryStore historyStore;

  public HistoryListener(final HistoryStore historyStore) {
    this.historyStore = historyStore;
  }

  public void runStart() {

  }

  public void handleMutationResult(final MutationMetaData metaData) {
    for (final MutationResult each : metaData.getMutations()) {
      this.historyStore.recordResult(each);
    }

  }

  public void runEnd() {

  }

}
