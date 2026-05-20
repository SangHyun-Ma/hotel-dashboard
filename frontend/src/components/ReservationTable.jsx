import React from 'react';
import './ReservationTable.css';

const STATUS_MAP = {
  CONFIRMED: { label: '예약확정', className: 'badge-confirmed' },
  CHECKED_IN: { label: '체크인', className: 'badge-checkin' },
  CHECKED_OUT: { label: '체크아웃', className: 'badge-checkout' },
  CANCELLED: { label: '취소', className: 'badge-cancelled' },
};

const CHANNEL_COLORS = {
  'Booking.com': '#003580',
  'Agoda': '#c5272d',
  'Expedia': '#ffc72c',
  '자사몰': '#10b981',
  '전화예약': '#8b5cf6',
};

const ReservationTable = ({ reservations }) => {
  if (!reservations || reservations.length === 0) {
    return <div className="table-empty">예약 데이터가 없습니다.</div>;
  }

  return (
    <div className="table-scroll-wrapper">
      <table className="reservation-table">
        <thead>
          <tr>
            <th>예약번호</th>
            <th>투숙객</th>
            <th>호텔</th>
            <th>체크인</th>
            <th>체크아웃</th>
            <th>채널</th>
            <th className="col-amount">금액</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map((r, idx) => {
            const statusInfo = STATUS_MAP[r.status] || { label: r.status, className: 'badge-checkout' };
            const channelColor = CHANNEL_COLORS[r.channel];
            return (
              <tr key={r.reservationId} className={idx % 2 === 0 ? 'row-even' : 'row-odd'}>
                <td>
                  <span className="res-id">{r.reservationId}</span>
                </td>
                <td>
                  <span className="guest-name">{r.guestName}</span>
                </td>
                <td className="hotel-name">{r.hotelName}</td>
                <td className="date-cell">{r.checkIn}</td>
                <td className="date-cell">{r.checkOut}</td>
                <td>
                  <span
                    className="channel-tag"
                    style={{
                      borderLeft: `3px solid ${channelColor || '#94a3b8'}`,
                    }}
                  >
                    {r.channel}
                  </span>
                </td>
                <td className="col-amount">
                  <span className="amount-value">
                    {r.amount.toLocaleString()}
                    <span className="amount-unit">원</span>
                  </span>
                </td>
                <td>
                  <span className={`status-badge ${statusInfo.className}`}>
                    {statusInfo.label}
                  </span>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ReservationTable;
