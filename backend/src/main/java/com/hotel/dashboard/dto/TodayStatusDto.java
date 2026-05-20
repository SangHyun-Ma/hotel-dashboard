package com.hotel.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayStatusDto {
    private int todayCheckIn;
    private int todayCheckOut;
    private int currentGuests;
    private int todayReservations;
    private int prevCheckIn;
    private int prevCheckOut;
    private int prevGuests;
    private int prevReservations;
}
