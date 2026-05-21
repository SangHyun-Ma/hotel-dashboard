package com.hotel.dashboard.service;

import com.hotel.dashboard.dto.ChannelShareDto;
import com.hotel.dashboard.dto.MonthlyRevenueDto;
import com.hotel.dashboard.dto.ReservationDto;
import com.hotel.dashboard.dto.TodayStatusDto;
import com.hotel.dashboard.mock.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 예약번호 단건 조회 — 현재 예약 + 전체 이력 풀에서 검색
    public Optional<ReservationDto> getReservationById(String id) {
        return mockDataGenerator.generateAllReservations().stream()
            .filter(r -> r.getReservationId().equals(id))
            .findFirst();
    }

    // 투숙객 전체 예약 이력 — 날짜 내림차순 (최신순)
    public List<ReservationDto> getGuestReservations(String guestName) {
        return mockDataGenerator.generateAllReservations().stream()
            .filter(r -> r.getGuestName().equals(guestName))
            .sorted(Comparator.comparing(ReservationDto::getCheckIn).reversed())
            .collect(Collectors.toList());
    }
}
