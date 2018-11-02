package com.example.dparm.ocrscanneraac.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CardDao {
    @Query("SELECT * FROM card")
    List<CardEntry> loadAllCards();

    @Insert
    void insertCard(CardEntry cardEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCard(CardEntry cardEntry);

    @Delete
    void deleteCard(CardEntry cardEntry);

    // To query the database with id and get the Card object
    @Query("SELECT * FROM card WHERE id = :id")
    CardEntry loadTaskById(int id);
}
