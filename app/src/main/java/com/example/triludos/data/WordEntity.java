package com.example.triludos.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "WordEntity")
public class WordEntity {
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "hint")
    private String hint;

    @ColumnInfo(name = "lang")
    private String lang;

    public WordEntity( String word, String hint, String lang) {
        this.word = word;
        this.hint = hint;
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
