package tech.pcreate.subscriptionkeeper.database;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity (tableName = "subs")
@TypeConverters(DateConverter.class)
public class Subscription  {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String currency;
    public float amount;
    public Date beginDate;
    public int recurring;

    public Subscription(int id, String name, String currency, int amount, Date beginDate, int recurring) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.amount = amount;
        this.beginDate = beginDate;
        this.recurring = recurring;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public int getRecurring() {
        return recurring;
    }

    public void setRecurring(int recurring) {
        this.recurring = recurring;
    }

    public Subscription(){
        this.name = "";
        this.currency = "";
        this.amount = 0;
        this.beginDate = null;
        this.recurring = 0;
    }
}
