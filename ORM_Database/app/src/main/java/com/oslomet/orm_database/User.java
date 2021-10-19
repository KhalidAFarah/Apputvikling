package com.oslomet.orm_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;
    public int getuid() {
        return uid;
    }
    public void setuid(int id) {
        this.uid = id;
    }
    public void setfirstName(String firstname) {
        this.firstName = firstname;
    }
    public String getfirstName() {
        return this.firstName;
    }
    public String getlastName() {
        return this.lastName;
    }
    public void setlastName(String lastname){
        this.lastName = lastname;
    }
}
