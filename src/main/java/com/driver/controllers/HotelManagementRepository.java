package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Repository
public class HotelManagementRepository {
    private HashMap<String, Hotel> hotelDb = new HashMap<>();
    private HashMap<Integer, User> userDb = new HashMap<>();
    private HashMap<String , Booking> bookingDb = new HashMap<>();

    private HashMap<Integer , ArrayList<Booking>> personBookings = new HashMap<>();
    public ArrayList<Hotel> getHotelList() {
        ArrayList<Hotel> hotels = new ArrayList<>();

        for(Hotel hotel : hotelDb.values())
            hotels.add(hotel);

        return hotels;
    }


    public void saveHotel(Hotel hotel) {
        hotelDb.put(hotel.getHotelName(), hotel);
    }

    public void saveUser(User user) {
        userDb.put(user.getaadharCardNo(),user);
    }

    public void saveBooking(Booking booking) {
        bookingDb.put(booking.getBookingId(), booking);
    }

    public Hotel getHotel(String hotelName) {
        if(hotelDb.containsKey(hotelName))
            return hotelDb.get(hotelName);
        else
            return null;
    }

    public Optional<ArrayList<Booking>> getBookingsByAUser(Integer aadharCard) {
        if(personBookings.containsKey(aadharCard))
            return Optional.of(personBookings.get(aadharCard));

        return Optional.empty();
    }

    public void addPersonBooking(int aadharCard, Booking booking) {
        if(Objects.isNull(personBookings.get(aadharCard))) {
            personBookings.put(aadharCard, new ArrayList<Booking>());
            personBookings.get(aadharCard).add(booking);
        }
        else
            personBookings.get(aadharCard).add(booking);
    }
}
