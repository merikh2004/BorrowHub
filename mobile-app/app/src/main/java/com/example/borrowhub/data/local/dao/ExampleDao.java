package com.example.borrowhub.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.borrowhub.data.local.entity.ExampleEntity;

import java.util.List;

@Dao
public interface ExampleDao {

    @Insert
    void insert(ExampleEntity example);

    @Update
    void update(ExampleEntity example);

    @Delete
    void delete(ExampleEntity example);

    @Query("SELECT * FROM examples")
    LiveData<List<ExampleEntity>> getAllExamples();
}
