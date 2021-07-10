package ir.hadinemati.steelmarketing.Presenters.Interfaces;

public interface ICallsListPresenter {

    void getCallsList(String filter);
    void getExtraInformation(String uuid);
    void getProductsList();

    void SyncCalls();
}
