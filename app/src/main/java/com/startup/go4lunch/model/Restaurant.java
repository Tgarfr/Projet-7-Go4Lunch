package com.startup.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Restaurant {

    private final long id;
    private final String name;
    private final String type;
    private final float latitude;
    private final float longitude;
    private final String address;
    private final String openingTime;


    public Restaurant(Long id, String name, String type, float latitude, float longitude, String address, String openingTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.openingTime = openingTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningTime() {
        return openingTime;
    }


    public static Restaurant from(@NonNull OverpassElements overpassElements) {
        if (overpassElements.getTags().getName() != null) {
            long id = overpassElements.getId();
            String name = overpassElements.getTags().getName();
            String type = overpassElements.getTags().getCuisine();
            float latitude = overpassElements.getLat();
            float longitude = overpassElements.getLon();

            String address = null;
            if (overpassElements.getTags().getHouseNumber() != null) {
                address = overpassElements.getTags().getHouseNumber() + " ";
            }
            if (overpassElements.getTags().getStreet() != null) {
                address = address + overpassElements.getTags().getStreet() + " ";
            }
            if (overpassElements.getTags().getPostCode() != null) {
                address = address + overpassElements.getTags().getPostCode() + " ";
            }
            if (overpassElements.getTags().getCity() != null) {
                address = address + overpassElements.getTags().getCity();
            }
            if (address == null) {
                address = "Address not provided";
            }

            String openingTime;
            if (overpassElements.getTags().getOpeningHours() != null) {
                openingTime = overpassElements.getTags().getOpeningHours();
            } else {
                openingTime = "Opening time not provided";
            }

            return new Restaurant(id, name, type, latitude, longitude, address, openingTime);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Restaurant restaurant = (Restaurant) object;
        return restaurant.id == getId() &&
                Objects.equals(restaurant.name, getName()) &&
                Objects.equals(restaurant.type, getType()) &&
                restaurant.latitude == getLatitude() &&
                restaurant.longitude == getLongitude() &&
                Objects.equals(restaurant.address, getAddress()) &&
                Objects.equals(restaurant.openingTime, getOpeningTime());
    }
}