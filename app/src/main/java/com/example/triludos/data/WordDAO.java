package com.example.triludos.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDAO {

    @Query("Select * from WordEntity where lang = 'fr';")
    List<WordEntity> getFrenchWords();

    @Query("Select * from WordEntity where lang = 'en';")
    List<WordEntity> getEnglishWords();
}
