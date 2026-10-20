package com.eventbooking;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final Event event;
    private final List<Integer> availableSeats = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public BookingService(Event event) {
        this.event = event;
        for (int i = 1; i <= event.getTotalSeats(); i++) {
            availableSeats.add(i);
        }
    }

    public synchronized Booking bookSeat(String customerName, int seatNumber) throws InvalidBookingException {
        validateSeat(seatNumber);
        Booking booking = new Booking(customerName, seatNumber);
        for (int i = 1; i <= availableSeats.size(); i++) {
            if (availableSeats.get(i) == seatNumber) {
                availableSeats.remove(i);
                break;
            }
        }
        bookings.add(booking);
        return booking;
    }

    public synchronized List<Integer> getAvailableSeats() {
        return new ArrayList<>(availableSeats);
    }

    public synchronized List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public Event getEvent() {
        return event;
    }

    public int getBookedCount() {
        return bookings.size();
    }

    private void validateSeat(int seatNumber) throws InvalidBookingException {
        if (seatNumber <= 0) {
            throw new InvalidBookingException("Seat Number must br positive");
        }
        if (!availableSeats.contains(seatNumber)) {
            throw new InvalidBookingException("Seat " + seatNumber + " is not available.");
        }
    }

}
