package com.eventbooking;

public class Booking {
    private final String customerName;
    private final int seatNumber;

    public Booking(String customerName, int seatNumber) {
        this.seatNumber = seatNumber;
        this.customerName = customerName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "customerName='" + customerName + '\'' +
                ", seatNumber=" + seatNumber +
                '}';
    }
}
