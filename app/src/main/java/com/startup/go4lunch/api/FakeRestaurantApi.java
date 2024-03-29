package com.startup.go4lunch.api;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.startup.go4lunch.model.Restaurant;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class FakeRestaurantApi implements RestaurantApi {

    private final MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>(FAKE_RESTAURANTS_LIST);

    @NonNull
    public MutableLiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLiveData;
    }

    private static final List<Restaurant> FAKE_RESTAURANTS_LIST = Arrays.asList(
            new Restaurant(0L, "Restaurant 0", "type 0", 1000, 2000, "1 chemin de la Paix, 75002 Paris", "0h00","0600000000","http://website0.fr"),
            new Restaurant(1L, "Restaurant 1", "type 1", 1111, 2111, "1 rue de la Paix, 75002 Paris", "1h00","0600000001","http://website1.fr"),
            new Restaurant(2L, "Restaurant 2", "type 2", 1222, 2222, "2 rue de la Paix, 75002 Paris", "2h00","0600000002","http://website2.com"),
            new Restaurant(3L, "Restaurant 3", "type 3", 1333, 2333, "3 rue de la Paix, 75002 Paris", "3h00","0600000003","http://website3.com"),
            new Restaurant(4L, "Restaurant 4", "type 4", 1444, 2444, "4 rue de la Paix, 75002 Paris", "4h00","0600000004","http://website4.fr"),
            new Restaurant(5L, "Restaurant 5", "type 5", 1555, 2555, "5 rue de la Paix, 75002 Paris", "5h00","0600000005","http://website5.fr"));

    @Override
    public void fetchLocationNearLocation(@NonNull Location location) {}
}