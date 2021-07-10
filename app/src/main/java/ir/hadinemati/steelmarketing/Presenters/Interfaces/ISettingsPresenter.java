package ir.hadinemati.steelmarketing.Presenters.Interfaces;

import androidx.annotation.Nullable;

import java.util.List;

public interface ISettingsPresenter {


    void getAllIgnoredContacts();
    void AddNewIgnoredContact(String phoneNumber,@Nullable String Name);
    void RemoveIgnoredContact(String phoneNumber);

    void getAllProducts();
    void AddNewProduct(String Name);
    void RemoveProductByName(String Name);




}
