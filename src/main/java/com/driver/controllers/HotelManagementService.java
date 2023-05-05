package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelManagementService {
    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();

    public String addHotel(Hotel hotel) {
        if(Objects.isNull(hotel) || Objects.isNull(hotel.getHotelName()))
            return "FAILURE";

        Boolean present =  hotelManagementRepository.isPresent(hotel.getHotelName());

        if(!present)
            return "FAILURE";

        hotelManagementRepository.saveHotel(hotel);
        return "SUCCESS";
    }

    public void addUser(User user) {
        hotelManagementRepository.saveUser(user);
    }

    public String getBestHotel() {
        int facilities = 0;
        ArrayList<Hotel> hotels = hotelManagementRepository.getHotelList();
        String bestHotel = hotels.get(0).getHotelName();

        for(Hotel hotel : hotels){
            if(hotel.getFacilities().size() > facilities) {
                facilities = hotel.getFacilities().size();
                bestHotel = hotel.getHotelName();
            }
            else if(hotel.getFacilities().size() == facilities && hotel.getHotelName().compareTo(bestHotel) < 0){
                facilities = hotel.getFacilities().size();
                bestHotel = hotel.getHotelName();
            }
        }
        if(facilities > 0)
            return bestHotel;
        else
            return "";
    }

    public int calculateAmount(Booking booking) {
        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);
        hotelManagementRepository.saveBooking(booking);

        Hotel hotel = hotelManagementRepository.getHotel(booking.getHotelName());
        int availableRooms = hotel.getAvailableRooms();
        int bookedRooms = booking.getNoOfRooms();

        if(bookedRooms > availableRooms)
            return -1;

        hotel.setAvailableRooms(availableRooms - bookedRooms);
        hotelManagementRepository.saveHotel(hotel);
        hotelManagementRepository.addPersonBooking(booking.getBookingAadharCard(), booking);
        int amount = bookedRooms * hotel.getPricePerNight();
        booking.setAmountToBePaid(amount);
        return amount;
    }

    public int getBookings(Integer aadharCard) {
       Optional<ArrayList<Booking>> optional =  hotelManagementRepository.getBookingsByAUser(aadharCard);
       if(optional.isPresent())
           return optional.get().size();

       return 0;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.getHotel(hotelName);
        if(!Objects.isNull(hotel)){
            List<Facility> facilities = hotel.getFacilities();

            for(Facility facility : newFacilities){
                if(!facilities.contains(facility))
                    facilities.add(facility);
            }
            hotel.setFacilities(facilities);
            hotelManagementRepository.saveHotel(hotel);
            return hotel;
        }
        return null;
    }
}
