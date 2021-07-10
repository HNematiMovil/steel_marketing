package ir.hadinemati.steelmarketing.Models.Entity;

import java.util.Observable;

public class ProductPriceDO {

  String Name;
  int BuyPrice;
  int SellPrice;
  int Count;

    public ProductPriceDO(String name, int buyPrice, int sellPrice, int count) {
        Name = name;
        BuyPrice = buyPrice;
        SellPrice = sellPrice;
        Count = count;
    }


    public int getProfit(){
        return Count*(SellPrice-BuyPrice);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        BuyPrice = buyPrice;
    }

    public int getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(int sellPrice) {
        SellPrice = sellPrice;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
