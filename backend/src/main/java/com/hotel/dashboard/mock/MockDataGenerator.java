package com.hotel.dashboard.mock;

import com.hotel.dashboard.dto.ChannelShareDto;
import com.hotel.dashboard.dto.MonthlyRevenueDto;
import com.hotel.dashboard.dto.ReservationDto;
import com.hotel.dashboard.dto.TodayStatusDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class MockDataGenerator {

    private static final String[] HOTELS = {
        "롯데호텔서울", "그랜드하얏트서울", "조선팰리스", "파르나스호텔", "시그니엘서울"
    };

    private static final String[] CHANNELS = {
        "Booking.com", "Agoda", "Expedia", "자사몰", "전화예약"
    };

    private static final String[] GUEST_NAMES = {
        "김민준", "이서연", "박지훈", "최수아", "정우진",
        "강하은", "조성민", "윤지아", "임도현", "한소희",
        "오준혁", "신예린", "권태양", "유나현", "배찬호",
        "송민지", "홍준서", "문서아", "류준혁", "노지은"
    };

    private static final String[] STATUSES = {
        "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED"
    };

    private static final int[] STATUS_WEIGHTS = {40, 30, 20, 10};

    private Random newRandom() {
        return new Random(LocalDate.now().toEpochDay());
    }

    public TodayStatusDto generateTodayStatus() {
        Random r = newRandom();

        int todayCheckIn = 18 + r.nextInt(18);
        int todayCheckOut = 14 + r.nextInt(15);
        int currentGuests = 145 + r.nextInt(75);
        int todayReservations = 10 + r.nextInt(16);

        int prevCheckIn = todayCheckIn + (r.nextBoolean() ? 1 : -1) * (1 + r.nextInt(6));
        int prevCheckOut = todayCheckOut + (r.nextBoolean() ? 1 : -1) * (1 + r.nextInt(5));
        int prevGuests = currentGuests + (r.nextBoolean() ? 1 : -1) * (3 + r.nextInt(18));
        int prevReservations = todayReservations + (r.nextBoolean() ? 1 : -1) * (1 + r.nextInt(5));

        return new TodayStatusDto(
            todayCheckIn, todayCheckOut, currentGuests, todayReservations,
            Math.max(0, prevCheckIn), Math.max(0, prevCheckOut),
            Math.max(0, prevGuests), Math.max(0, prevReservations)
        );
    }

    public List<ChannelShareDto> generateChannelShare() {
        Random r = newRandom();

        int[] baseCounts = {45, 32, 28, 38, 22};
        int total = 0;
        int[] counts = new int[CHANNELS.length];

        for (int i = 0; i < CHANNELS.length; i++) {
            counts[i] = baseCounts[i] + r.nextInt(12);
            total += counts[i];
        }

        List<ChannelShareDto> result = new ArrayList<>();
        for (int i = 0; i < CHANNELS.length; i++) {
            double percentage = Math.round((double) counts[i] / total * 1000.0) / 10.0;
            result.add(new ChannelShareDto(CHANNELS[i], counts[i], percentage));
        }
        return result;
    }

    public List<MonthlyRevenueDto> generateMonthlyRevenue() {
        Random r = newRandom();
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd");

        List<MonthlyRevenueDto> result = new ArrayList<>();
        LocalDate date = start;
        while (!date.isAfter(today)) {
            boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
            long base = isWeekend ? 18_000_000L : 10_000_000L;
            long variance = (long) (r.nextInt(12_000_000));
            long revenue = base + variance;
            result.add(new MonthlyRevenueDto(date.format(fmt), revenue));
            date = date.plusDays(1);
        }
        return result;
    }

    public List<ReservationDto> generateReservations() {
        Random r = newRandom();
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<ReservationDto> result = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String reservationId = String.format("HTL-%s-%04d",
                today.format(DateTimeFormatter.ofPattern("yyMM")), 1000 + i);

            String guestName = GUEST_NAMES[i % GUEST_NAMES.length];
            String hotel = HOTELS[r.nextInt(HOTELS.length)];
            String channel = CHANNELS[r.nextInt(CHANNELS.length)];

            LocalDate checkIn;
            LocalDate checkOut;
            String status = weightedStatus(r);

            if ("CHECKED_IN".equals(status)) {
                checkIn = today.minusDays(r.nextInt(3));
                checkOut = today.plusDays(1 + r.nextInt(4));
            } else if ("CHECKED_OUT".equals(status)) {
                checkOut = today.minusDays(r.nextInt(5));
                checkIn = checkOut.minusDays(1 + r.nextInt(4));
            } else if ("CONFIRMED".equals(status)) {
                checkIn = today.plusDays(1 + r.nextInt(14));
                checkOut = checkIn.plusDays(1 + r.nextInt(5));
            } else {
                checkIn = today.minusDays(r.nextInt(10)).plusDays(r.nextInt(7));
                checkOut = checkIn.plusDays(1 + r.nextInt(4));
            }

            long nights = Math.max(1, checkOut.toEpochDay() - checkIn.toEpochDay());
            long pricePerNight = 80_000L + (long) (r.nextInt(53)) * 10_000L;
            long amount = pricePerNight * nights;

            result.add(new ReservationDto(
                reservationId, guestName, hotel,
                checkIn.format(fmt), checkOut.format(fmt),
                channel, amount, status
            ));
        }
        return result;
    }

    private String weightedStatus(Random r) {
        int roll = r.nextInt(100);
        int cumulative = 0;
        for (int i = 0; i < STATUS_WEIGHTS.length; i++) {
            cumulative += STATUS_WEIGHTS[i];
            if (roll < cumulative) return STATUSES[i];
        }
        return STATUSES[0];
    }
}
