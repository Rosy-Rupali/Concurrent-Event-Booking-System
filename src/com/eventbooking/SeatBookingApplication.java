package com.eventbooking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeatBookingApplication {
    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("        CONCURRENT EVENT SEAT BOOKING SYSTEM (INTERACTIVE)       ");
        System.out.println("=================================================================");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();
        if (eventName.isEmpty()) {
            eventName = "Tech MeetUp 2026";
        }

        System.out.print("Enter Seat Input: ");
        String seatInput = scanner.nextLine();
        int totalSeats = 10;
        if (!seatInput.isEmpty()) {
            try {
                totalSeats = Integer.parseInt(seatInput);
            } catch (NumberFormatException e) {
                System.out.println("[WARNING] Invalid input. Defaulting to 10 seats.");
            }
        }
        Event event = new Event(eventName, totalSeats);
        BookingService bookingService = new BookingService(event);
        System.out.printf("[OK] Created Event '%s' with %d seats (Seats 1 to %d).%n", eventName, totalSeats, totalSeats);
        StatusMonitor statusMonitor = new StatusMonitor(5000);
        Thread monitorThread = new Thread(statusMonitor);
        monitorThread.start();

        ExecutorService executor = Executors.newFixedThreadPool(3);
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Book Single Seat");
            System.out.println("2. Submit Multiple Concurrent Bookings");
            System.out.println("3. View Available Seats");
            System.out.println("4. View Confirmed Bookings");
            System.out.println("5. Exit");
            System.out.print("Select option (1-5): ");
            String optionStr = scanner.nextLine();
            switch (optionStr) {
                case "1":
                    System.out.print("Customer Name: ");
                    String custName = scanner.nextLine();
                    System.out.print("Seat Number: ");
                    try {
                        int seatNum = Integer.parseInt(scanner.nextLine());
                        executor.submit(new BookingTask(bookingService, custName, seatNum));
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid seat number format.");
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }
                    break;

                case "2":
                    System.out.print("How many concurrent booking requests? ");
                    try {
                        int count = Integer.parseInt(scanner.nextLine());
                        List<BookingTask> tasks = new ArrayList<>();
                        for (int i = 1; i <= count; i++) {
                            System.out.printf("  Request %d - Customer Name: ", i);
                            String cName = scanner.nextLine();
                            System.out.printf("  Request %d - Seat Number: ", i);
                            int sNum = Integer.parseInt(scanner.nextLine().trim());
                            tasks.add(new BookingTask(bookingService, cName, sNum));
                        }
                        System.out.println("[APPLICATION] Launching " + count + " requests SIMULTANEOUSLY across thread pool...");
                        for (BookingTask task : tasks) {
                            executor.submit(task);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid numeric input.");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                    break;

                case "3":
                    System.out.println("[AVAILABLE SEATS]: " + bookingService.getAvailableSeats());
                    break;

                case "4":
                    System.out.println("[CONFIRMED BOOKINGS]:");
                    List<Booking> list = bookingService.getBookings();
                    if (list.isEmpty()) {
                        System.out.println("  (No bookings yet)");
                    } else {
                        for (Booking b : list) {
                            System.out.println("  - " + b);
                        }
                    }
                    break;

                case "5":
                    running = false;
                    break;

                default:
                    System.out.println("[ERROR] Invalid option. Please choose between 1 and 5.");
            }
        }

        System.out.println("[APPLICATION] Shutting down thread pool executor...");
        executor.shutdown();

        System.out.println("[APPLICATION] Stopping background status monitor...");
        statusMonitor.stopMonitoring();
        try {
            monitorThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final Summary Display (pulling data from the Event and BookingService)
        System.out.println("==================== BOOKING SUMMARY ====================");
        System.out.printf("Event: %s | Total: %d | Booked: %d | Remaining: %d%n",
                bookingService.getEvent().getEventName(),
                bookingService.getEvent().getTotalSeats(),
                bookingService.getBookedCount(),
                bookingService.getAvailableSeats().size());
        System.out.println("=========================================================");

        scanner.close();
    }
}
