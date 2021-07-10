package ir.hadinemati.steelmarketing.Models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;

@Dao
public interface PotentialCustomerPhoneCallDao {

    @Query("SELECT * FROM PotentialCustomerPhoneCall ORDER BY Id DESC")
    List<PotentialCustomerPhoneCall> getAll();

    @Query("SELECT * FROM PotentialCustomerPhoneCall WHERE product=:pName ORDER BY Id DESC ")
    List<PotentialCustomerPhoneCall> getPhoneCallByProductName(String pName);

    @Insert
    void AddNewPhoneCall(PotentialCustomerPhoneCall phoneCall);


}
