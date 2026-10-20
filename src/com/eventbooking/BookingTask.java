package com.eventbooking;

public class BookingTask implements Runnable{
    private final BookingService bookingService;
    private final String customerName;
    private final int seatNumber;

    public BookingTask(BookingService bookingService, String customerName, int seatNumber) {
        this.bookingService = bookingService;
        this.customerName = customerName;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run() {
        try {
            Booking booking = bookingService.bookSeat(customerName, seatNumber);
            System.out.println("Booking is successful: "+booking);
        }catch (InvalidBookingException e){
            System.err.println("Booking failed for "+customerName+" because "+e.getMessage());
        }finally {
            System.out.println("Booking attempt ended for "+customerName);
        }
    }

    @Override
    public String toString() {
        return "BookingTask[" +
                "bookingService=" + bookingService +
                ", customerName='" + customerName + '\'' +
                ", seatNumber=" + seatNumber +
                ']';
    }
}
