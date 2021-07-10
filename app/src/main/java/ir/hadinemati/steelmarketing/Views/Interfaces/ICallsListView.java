package ir.hadinemati.steelmarketing.Views.Interfaces;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;

public interface ICallsListView {

    void OnCallsListGenerated(List<PotentialCustomerPhoneCall> callList);
    void OnProductsListGenerated(List<String> products);

    void SyncUpdated(int childIndex);
}
