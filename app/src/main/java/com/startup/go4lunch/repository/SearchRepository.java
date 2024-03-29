package com.startup.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SearchRepository {

    private final MutableLiveData<String> mapFragmentSearchLivedata = new MutableLiveData<>(null);
    private final MutableLiveData<String> restaurantListFragmentSearchLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<String> workmateListFragmentSearchLiveData = new MutableLiveData<>(null);

    @NonNull
    public LiveData<String> getMapFragmentSearchLivedata() {
        return mapFragmentSearchLivedata;
    }

    @NonNull
    public LiveData<String> getRestaurantListFragmentSearchLiveData() {
        return restaurantListFragmentSearchLiveData;
    }

    @NonNull
    public LiveData<String> getWorkmateListFragmentSearchLiveData() {
        return workmateListFragmentSearchLiveData;
    }

    public void setMapFragmentSearch(@Nullable String searchString) {
        mapFragmentSearchLivedata.setValue(searchString);
    }

    public void setRestaurantListSearch(@Nullable String searchString) {
        restaurantListFragmentSearchLiveData.setValue(searchString);
    }

    public void setWorkmateListSearch(@Nullable String searchString) {
        workmateListFragmentSearchLiveData.setValue(searchString);
    }
}