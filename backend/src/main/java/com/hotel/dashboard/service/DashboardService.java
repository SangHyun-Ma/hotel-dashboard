package com.hotel.dashboard.service;

import com.hotel.dashboard.dto.ChannelShareDto;
import com.hotel.dashboard.dto.MonthlyRevenueDto;
import com.hotel.dashboard.dto.ReservationDto;
import com.hotel.dashboard.dto.TodayStatusDto;
import com.hotel.dashboard.mock.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MockDataGenerator mockDataGenerator;

    public TodayStatusDto getTodayStatus() {
        return mockDataGenerator.generateTodayStatus();
    }

    public List<ChannelShareDto> getChannelShare() {
        return mockDataGenerator.generateChannelShare();
    }

    public List<MonthlyRevenueDto> getMonthlyRevenue() {
        return mockDataGenerator.generateMonthlyRevenue();
    }

    public List<ReservationDto> getReservations() {
        return mockDataGenerator.generateReservations();
    }
}
