package com.hotel.dashboard.controller;

import com.hotel.dashboard.dto.ChannelShareDto;
import com.hotel.dashboard.dto.MonthlyRevenueDto;
import com.hotel.dashboard.dto.ReservationDto;
import com.hotel.dashboard.dto.TodayStatusDto;
import com.hotel.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/today-status")
    public ResponseEntity<TodayStatusDto> getTodayStatus() {
        return ResponseEntity.ok(dashboardService.getTodayStatus());
    }

    @GetMapping("/channel-share")
    public ResponseEntity<List<ChannelShareDto>> getChannelShare() {
        return ResponseEntity.ok(dashboardService.getChannelShare());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDto>> getMonthlyRevenue() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getReservations() {
        return ResponseEntity.ok(dashboardService.getReservations());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable String id) {
        return dashboardService.getReservationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // 투숙객 전체 예약 이력 (날짜 내림차순)
    @GetMapping("/reservations/guest/{guestName}")
    public ResponseEntity<List<ReservationDto>> getGuestReservations(
            @PathVariable String guestName) {
        return ResponseEntity.ok(dashboardService.getGuestReservations(guestName));
    }
}
