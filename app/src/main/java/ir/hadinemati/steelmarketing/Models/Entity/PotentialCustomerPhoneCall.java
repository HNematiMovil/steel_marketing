package ir.hadinemati.steelmarketing.Models.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class PotentialCustomerPhoneCall {


    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "gender")
    public String Gender;

    @ColumnInfo(name = "username")
    public String Username;

    @ColumnInfo(name = "phonenumber")
    public String PhoneNumber;

    @ColumnInfo(name = "product")
    public String Product;

    @ColumnInfo(name = "persiandate")
    public String PersianDate;

    @ColumnInfo(name = "uuid")
    public String UUID;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getPersianDate() {
        return PersianDate;
    }

    public void setPersianDate(String persianDate) {
        PersianDate = persianDate;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}

