package com.example.dparm.ocrscanneraac.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Card")
public class CardEntry {
    // COMPLETED (3) Annotate the id as PrimaryKey. Set autoGenerate to true.
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cardNumber;


    // COMPLETED (4) Use the Ignore annotation so Room knows that it has to use the other constructor instead
    @Ignore
    public CardEntry(String cardNumber) {
        this.cardNumber = cardNumber;

    }

    public CardEntry(int id, String cardNumber) {
        this.id = id;
        this.cardNumber = cardNumber ;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }



}
