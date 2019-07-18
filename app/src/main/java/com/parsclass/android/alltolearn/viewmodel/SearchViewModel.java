package com.parsclass.android.alltolearn.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parsclass.android.alltolearn.model.Article;
import com.parsclass.android.alltolearn.repository.SearchRepository;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

    SearchRepository repository;
    private MutableLiveData<ArrayList<Article>> liveData;
    private MutableLiveData<String> progressLoadStatus;

    public SearchViewModel() {

        repository=new SearchRepository();
    }

    public MutableLiveData<ArrayList<Article>> getLiveData(String query, String apiKey) {
        return repository.getLiveData(query,apiKey);
    }

    public MutableLiveData<String> getProgressLoadStatus() {
        return repository.getProgressLoadStatus();
    }

    public ArrayList<String> getSuggestion(String query){
        ArrayList<String> suggestion=new ArrayList<>();
        return suggestion;
    }
}




