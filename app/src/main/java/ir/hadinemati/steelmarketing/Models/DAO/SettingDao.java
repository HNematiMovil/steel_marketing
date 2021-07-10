package ir.hadinemati.steelmarketing.Models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.Setting;

@Dao
public interface SettingDao {


    @Query("SELECT * FROM setting")
    public List<Setting> getAll();

    @Query("SELECT value from SETTING WHERE name=:name")
    public String getSettingByName(String name);

    @Insert
    public void AddSetting(Setting setting);

    @Query("UPDATE Setting set value=:Value WHERE name=:settingName")
    public void UpdateSetting(String settingName , String Value);

    @Query("DELETE FROM setting WHERE name=:settingName")
    public void RemoveSettingByName(String settingName);

}
