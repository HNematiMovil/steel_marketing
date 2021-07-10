package ir.hadinemati.steelmarketing.Views.Interfaces;

import java.util.List;

public interface ISellView {

    void OnProductsListGenerated(List<String> products);
    void OnFormCompletionError();
    void WaitToSync();
    void SyncDone();

}
