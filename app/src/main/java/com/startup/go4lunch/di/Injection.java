package com.startup.go4lunch.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.api.FirebaseWorkmateApi;
import com.startup.go4lunch.api.OverpassRestaurantApi;
import com.startup.go4lunch.repository.RestaurantRepository;
import com.startup.go4lunch.repository.WorkmateRepository;

public class Injection {

    private static volatile RestaurantRepository restaurantRepository;
    private static volatile WorkmateRepository workmateRepository;

    private static RestaurantRepository createRestaurantRepository() {
        return new RestaurantRepository(new OverpassRestaurantApi());
    }

    private static WorkmateRepository createWorkmateRepository() {
        return new WorkmateRepository(new FirebaseWorkmateApi());
    }

    public static RestaurantRepository getRestaurantRepository() {
        if (restaurantRepository == null) {
            synchronized (RestaurantRepository.class) {
                if (restaurantRepository == null) {
                    restaurantRepository = createRestaurantRepository();
                }
            }
        }
        return restaurantRepository;
    }

    public static WorkmateRepository getWorkmateRepository() {
        if (workmateRepository == null) {
            synchronized (WorkmateRepository.class) {
                if (workmateRepository== null) {
                    workmateRepository = createWorkmateRepository();
                }
            }
        }
        return workmateRepository;
    }

    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}