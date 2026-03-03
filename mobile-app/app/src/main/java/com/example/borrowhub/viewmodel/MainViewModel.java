package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.borrowhub.data.local.entity.ExampleEntity;
import com.example.borrowhub.repository.ExampleRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final ExampleRepository repository;
    private final LiveData<List<ExampleEntity>> allExamples;

    public MainViewModel(Application application) {
        super(application);
        repository = new ExampleRepository(application);
        allExamples = repository.getAllExamples();
    }

    public LiveData<List<ExampleEntity>> getAllExamples() {
        return allExamples;
    }

    // Dito ang Logic! Tumatanggap lang ang ViewModel ng raw data mula sa View.
    // Siya na ang bahalang bumuo ng Entity at kumausap sa Repository.
    public void addExample(String name, String description) {
        ExampleEntity newExample = new ExampleEntity(name, description);
        repository.insert(newExample);
    }
}
