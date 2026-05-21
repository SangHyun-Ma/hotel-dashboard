package com.hotel.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private String reservationId;
    private String guestName;
    private String hotelName;
    private String checkIn;
    private String checkOut;
    private String channel;
    private long amount;
    private String status;
    private String guestPhone;
    private String guestEmail;
    private String roomType;
    private String roomNumber;
    private String hotelAddress;
    private long pricePerNight;
    private String paymentMethod;
    private String paymentStatus;
    private String specialRequest;
    private String createdAt;
    private String updatedAt;
}
