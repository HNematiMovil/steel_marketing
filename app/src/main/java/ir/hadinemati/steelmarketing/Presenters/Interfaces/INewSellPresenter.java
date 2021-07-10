package ir.hadinemati.steelmarketing.Presenters.Interfaces;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.ProductPriceDO;

public interface INewSellPresenter {

    void getProductsList();

    void addNewSell(String phoneNumber, String Gender, String Name, List<ProductPriceDO> productPriceDOS , String persianDateTime);


}
