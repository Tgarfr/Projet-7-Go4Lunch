package com.startup.go4lunch.ui;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantMapMarker;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.LocationRepository;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.SearchRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class MapFragmentViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final RestaurantRepository restaurantRepository;
    private final SearchRepository searchRepository;
    private final WorkmateRepository workmateRepository;
    private final MutableLiveData<Location> mapCenterLocationLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantMapMarker>> restaurantMapMarkerListLiveData = new MutableLiveData<>();


    public MapFragmentViewModel(@NonNull LocationRepository locationRepository, @NonNull RestaurantRepository restaurantRepository, @NonNull SearchRepository searchRepository,@NonNull WorkmateRepository workmateRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        this.searchRepository = searchRepository;
        this.workmateRepository = workmateRepository;

        locationRepository.getLocationLiveData().observeForever(locationObserver);
        searchRepository.getMapFragmentSearchLivedata().observeForever(searchObserver);
        restaurantRepository.getRestaurantListLiveData().observeForever(restaurantListObserver);
        workmateRepository.getWorkmateListLiveData().observeForever(workmateListObserver);
    }

    @Override
    protected void onCleared() {
        locationRepository.getLocationLiveData().removeObserver(locationObserver);
        searchRepository.getMapFragmentSearchLivedata().removeObserver(searchObserver);
        restaurantRepository.getRestaurantListLiveData().removeObserver(restaurantListObserver);
        workmateRepository.getWorkmateListLiveData().removeObserver(workmateListObserver);
        super.onCleared();
    }

    @NonNull
    public LiveData<Location> getMapCenterLocationLiveData() {
        return mapCenterLocationLiveData;
    }

    @NonNull
    public LiveData<List<RestaurantMapMarker>> getRestaurantMapMarkerListLiveData() {
        return restaurantMapMarkerListLiveData;
    }

    private final Observer<Location> locationObserver = location -> {
        if (getRestaurantToCenterOnLiveData().getValue() == null) {
            mapCenterLocationLiveData.setValue(location);
        }
    };

    private final Observer<String> searchObserver = string -> {
        if (string != null) {
            Restaurant restaurant = getRestaurantFromString(string);
            if (restaurant != null) {
                Location location =new Location(restaurant.getName());
                location.setLatitude(restaurant.getLatitude());
                location.setLongitude(restaurant.getLongitude());
                mapCenterLocationLiveData.setValue(location);
            }
        }
    };

    @NonNull
    private LiveData<Restaurant> getRestaurantToCenterOnLiveData() {
        return Transformations.map(searchRepository.getMapFragmentSearchLivedata(), string -> {
            if (string != null) {
                return restaurantRepository.getRestaurantResearchedFromString(string);
            }
            return null;
        });
    }

    private final Observer<List<Restaurant>> restaurantListObserver = restaurantList -> {
        List<RestaurantMapMarker> restaurantMapMarkerList = new ArrayList<>();
        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                restaurantMapMarkerList.add(new RestaurantMapMarker(restaurant, workmateLunchOnRestaurant(restaurant.getId())));
            }
        }
        restaurantMapMarkerListLiveData.setValue(restaurantMapMarkerList);
    };

    private final Observer<List<Workmate>> workmateListObserver = workmatesList -> {
        List<RestaurantMapMarker> restaurantMapMarkerList = restaurantMapMarkerListLiveData.getValue();
        if (restaurantMapMarkerList != null) {
            for(RestaurantMapMarker restaurantMapMarker: restaurantMapMarkerList) {
                restaurantMapMarker.setWorkmateLunchOnRestaurant(workmateLunchOnRestaurant(restaurantMapMarker.getRestaurant().getId()));
            }
        }
        restaurantMapMarkerListLiveData.setValue(restaurantMapMarkerList);
    };

    public void setEndSearch() {
        searchRepository.setMapFragmentSearch(null);
    }

    @Nullable
    private Restaurant getRestaurantFromString(@NonNull String string) {
        return restaurantRepository.getRestaurantResearchedFromString(string);
    }

    private boolean workmateLunchOnRestaurant(long restaurantUid) {
        List<Workmate> workmateList = workmateRepository.getWorkmateListLiveData().getValue();
        if (workmateList != null) {
            for (Workmate workmate : workmateList) {
                if (workmate.getRestaurantSelectedUid() != null) {
                    if (workmate.getRestaurantSelectedUid() == restaurantUid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void updateLocation(Location location) {
        if (location == null) {
            location = LocationRepository.getCompanyLocation();
        }
        locationRepository.setLocation(location);
        restaurantRepository.updateLocationRestaurantList(location);
    }
}
