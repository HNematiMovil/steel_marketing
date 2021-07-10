package ir.hadinemati.steelmarketing.Views.Interfaces;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;

public interface ISettingView {


    void OnIgnoreContactsListGenerated(List<ContactDO> ignoredContactList);
    void OnIgnoreContactsListItemRemoved(List<ContactDO> ignoredContactList);
    void OnNewContactExists();

    void OnProductListGenerated(List<String> products);
    void OnNewProductExists();


}
