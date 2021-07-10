package ir.hadinemati.steelmarketing.Presenters.Interfaces;

import java.util.List;

public interface ICallerInformationPresenter {

    void getProducts();

    void Save(String phoneNumber , String ProductName ,String Gender, String Name , String date);

}
