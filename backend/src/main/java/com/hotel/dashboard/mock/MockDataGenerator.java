package com.hotel.dashboard.mock;

import com.hotel.dashboard.dto.ChannelShareDto;
import com.hotel.dashboard.dto.MonthlyRevenueDto;
import com.hotel.dashboard.dto.ReservationDto;
import com.hotel.dashboard.dto.TodayStatusDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
public class MockDataGenerator {

    private static final String[] HOTELS = {
        "롯데호텔서울", "그랜드하얏트서울", "조선팰리스", "파르나스호텔", "시그니엘서울"
    };

    private static final String[] HOTEL_ADDRESSES = {
        "서울특별시 중구 을지로 30",
        "서울특별시 용산구 소월로 322",
        "서울특별시 강남구 압구정로 49",
        "서울특별시 강남구 테헤란로 521",
        "서울특별시 송파구 올림픽로 300"
    };

    private static final String[] CHANNELS = {
        "Booking.com", "Agoda", "Expedia", "자사몰", "전화예약"
    };

    // 10명의 고정 투숙객 — 20건 현재 예약에서 각 2번 등장
    private static final String[] GUEST_NAMES = {
        "김민준", "이서연", "박지훈", "최수아", "정우진",
        "강하은", "조성민", "윤지아", "임도현", "한소희"
    };

    // 투숙객별 고정 연락처 — 같은 투숙객은 항상 동일한 연락처
    private static final String[] GUEST_PHONES = {
        "010-2345-6789", "010-3456-7890", "010-4567-8901", "010-5678-9012", "010-6789-0123",
        "010-7890-1234", "010-8901-2345", "010-9012-3456", "010-1234-5678", "010-2345-0987"
    };

    private static final String[] GUEST_EMAILS = {
        "guest01@hotel.co.kr", "guest02@hotel.co.kr", "guest03@hotel.co.kr",
        "guest04@hotel.co.kr", "guest05@hotel.co.kr", "guest06@hotel.co.kr",
        "guest07@hotel.co.kr", "guest08@hotel.co.kr", "guest09@hotel.co.kr",
        "guest10@hotel.co.kr"
    };

    // 투숙객별 이력 건수 (3~5건 변화)
    private static final int[] HISTORY_COUNTS = {4, 5, 3, 5, 4, 3, 5, 4, 3, 4};

    private static final String[] STATUSES = {
        "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED"
    };

    private static final int[] STATUS_WEIGHTS = {40, 30, 20, 10};

    private static final String[] ROOM_TYPES = {"스탠다드", "디럭스", "스위트", "프리미어"};

    private static final String[] PAYMENT_METHODS = {"신용카드", "법인카드", "현금"};

    private static final String[] PAYMENT_STATUSES = {
        "PAID", "PAID", "PAID", "PAID", "PENDING", "REFUNDED"
    };

    private static final String[] SPECIAL_REQUESTS = {
        "없음", "없음", "없음", "없음", "없음",
        "금연 객실 요청", "늦은 체크아웃 요청", "조식 포함 요청",
        "고층 객실 요청", "유아용 침대 요청"
    };

    // 이력 기준일 고정 — 코드 실행 시점과 무관하게 항상 동일한 과거 날짜 생성
    private static final LocalDate HISTORY_REF_DATE = LocalDate.of(2026, 5, 21);

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

            int guestIdx = i % GUEST_NAMES.length;
            String guestName = GUEST_NAMES[guestIdx];
            int hotelIdx = r.nextInt(HOTELS.length);
            String hotel = HOTELS[hotelIdx];
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

            String roomType = ROOM_TYPES[r.nextInt(ROOM_TYPES.length)];
            int floor = 10 + r.nextInt(15);
            int roomNo = 1 + r.nextInt(10);
            String roomNumber = String.format("%d%02d", floor, roomNo);
            String hotelAddress = HOTEL_ADDRESSES[hotelIdx];
            String paymentMethod = PAYMENT_METHODS[r.nextInt(PAYMENT_METHODS.length)];
            String paymentStatus = paymentStatusFor(status, r);
            String specialRequest = SPECIAL_REQUESTS[r.nextInt(SPECIAL_REQUESTS.length)];

            LocalDate createdDate = checkIn.minusDays(1 + r.nextInt(30));
            String createdAt = createdDate.format(fmt) +
                String.format(" %02d:%02d", r.nextInt(24), r.nextInt(60));
            String updatedAt = today.format(fmt) +
                String.format(" %02d:%02d", r.nextInt(24), r.nextInt(60));

            result.add(new ReservationDto(
                reservationId, guestName, hotel,
                checkIn.format(fmt), checkOut.format(fmt),
                channel, amount, status,
                GUEST_PHONES[guestIdx], GUEST_EMAILS[guestIdx],
                roomType, roomNumber, hotelAddress,
                pricePerNight, paymentMethod, paymentStatus,
                specialRequest, createdAt, updatedAt
            ));
        }
        return result;
    }

    /**
     * 특정 투숙객의 과거 체류 이력 생성.
     * HISTORY_REF_DATE 기준 고정 날짜 사용 → 실행 시점과 무관하게 항상 동일한 데이터 반환.
     */
    public List<ReservationDto> generateGuestHistory(String guestName) {
        int guestIdx = findGuestIndex(guestName);
        if (guestIdx < 0) return Collections.emptyList();

        // 투숙객 인덱스 기반 고정 시드 — 날짜 비의존
        Random r = new Random(guestIdx * 7919L);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int count = HISTORY_COUNTS[guestIdx];
        int[] monthsAgo;
        if (count == 3)      monthsAgo = new int[]{4, 8, 12};
        else if (count == 4) monthsAgo = new int[]{2, 5, 8, 11};
        else                 monthsAgo = new int[]{2, 4, 7, 9, 12};

        List<ReservationDto> history = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            // HISTORY_REF_DATE에서 역산 → 항상 동일한 과거 날짜
            LocalDate checkIn = HISTORY_REF_DATE.minusMonths(monthsAgo[i])
                .minusDays(r.nextInt(10));
            LocalDate checkOut = checkIn.plusDays(1 + r.nextInt(4));

            int hotelIdx = r.nextInt(HOTELS.length);
            String channel = CHANNELS[r.nextInt(CHANNELS.length)];
            long pricePerNight = 80_000L + (long) (r.nextInt(53)) * 10_000L;
            long nights = Math.max(1, checkOut.toEpochDay() - checkIn.toEpochDay());
            long amount = pricePerNight * nights;

            String roomType = ROOM_TYPES[r.nextInt(ROOM_TYPES.length)];
            int floor = 10 + r.nextInt(15);
            int roomNo = 1 + r.nextInt(10);
            String roomNumber = String.format("%d%02d", floor, roomNo);
            String paymentMethod = PAYMENT_METHODS[r.nextInt(PAYMENT_METHODS.length)];
            String specialRequest = SPECIAL_REQUESTS[r.nextInt(SPECIAL_REQUESTS.length)];

            String histId = String.format("HTL-%s-%04d",
                checkIn.format(DateTimeFormatter.ofPattern("yyMM")),
                1000 + guestIdx * 10 + i);

            String createdAt = checkIn.minusDays(1 + r.nextInt(20)).format(fmt) +
                String.format(" %02d:%02d", r.nextInt(24), r.nextInt(60));
            String updatedAt = checkOut.format(fmt) +
                String.format(" %02d:%02d", r.nextInt(24), r.nextInt(60));

            history.add(new ReservationDto(
                histId, guestName, HOTELS[hotelIdx],
                checkIn.format(fmt), checkOut.format(fmt),
                channel, amount, "CHECKED_OUT",
                GUEST_PHONES[guestIdx], GUEST_EMAILS[guestIdx],
                roomType, roomNumber, HOTEL_ADDRESSES[hotelIdx],
                pricePerNight, paymentMethod, "PAID",
                specialRequest, createdAt, updatedAt
            ));
        }
        return history;
    }

    // 현재 예약 20건 + 전체 투숙객 이력 — 예약번호 단건 조회에 사용
    public List<ReservationDto> generateAllReservations() {
        List<ReservationDto> all = new ArrayList<>(generateReservations());
        for (String guestName : GUEST_NAMES) {
            all.addAll(generateGuestHistory(guestName));
        }
        return all;
    }

    private int findGuestIndex(String guestName) {
        for (int i = 0; i < GUEST_NAMES.length; i++) {
            if (GUEST_NAMES[i].equals(guestName)) return i;
        }
        return -1;
    }

    private String paymentStatusFor(String reservationStatus, Random r) {
        if ("CONFIRMED".equals(reservationStatus)) {
            return r.nextInt(10) < 7 ? "PAID" : "PENDING";
        } else if ("CHECKED_IN".equals(reservationStatus) || "CHECKED_OUT".equals(reservationStatus)) {
            return "PAID";
        } else if ("CANCELLED".equals(reservationStatus)) {
            return r.nextInt(10) < 7 ? "REFUNDED" : "PAID";
        }
        return "PAID";
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
