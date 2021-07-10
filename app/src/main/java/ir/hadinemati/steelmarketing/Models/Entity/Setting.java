package ir.hadinemati.steelmarketing.Models.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Setting {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo (name = "name")
    public String name;

    @ColumnInfo(name = "value")
    public String value;

    public Setting( String name, String value) {

        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
