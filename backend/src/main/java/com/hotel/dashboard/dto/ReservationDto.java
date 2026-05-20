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
}
