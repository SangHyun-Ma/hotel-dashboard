import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { fetchReservationDetail, fetchGuestReservations } from '../api/dashboardApi';
import './ReservationDetail.css';

const STATUS_MAP = {
  CONFIRMED:   { label: '예약확정',  className: 'badge-confirmed' },
  CHECKED_IN:  { label: '체크인',    className: 'badge-checkin'   },
  CHECKED_OUT: { label: '체크아웃',  className: 'badge-checkout'  },
  CANCELLED:   { label: '취소',      className: 'badge-cancelled' },
};

const PAYMENT_STATUS_MAP = {
  PAID:     { label: '결제완료', className: 'pay-paid'     },
  PENDING:  { label: '결제대기', className: 'pay-pending'  },
  REFUNDED: { label: '환불완료', className: 'pay-refunded' },
};

const CHANNEL_COLORS = {
  'Booking.com': '#003580',
  'Agoda':       '#c5272d',
  'Expedia':     '#ffc72c',
  '자사몰':       '#10b981',
  '전화예약':     '#8b5cf6',
};

const ReservationDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [reservation, setReservation]         = useState(null);
  const [loading, setLoading]                 = useState(true);
  const [notFound, setNotFound]               = useState(false);
  const [guestHistory, setGuestHistory]       = useState([]);
  const [historyLoading, setHistoryLoading]   = useState(false);

  useEffect(() => {
    let cancelled = false;

    const load = async () => {
      setLoading(true);
      setNotFound(false);
      setReservation(null);
      setGuestHistory([]);
      setHistoryLoading(false);

      try {
        const detailRes = await fetchReservationDetail(id);
        if (cancelled) return;
        setReservation(detailRes.data);
        setLoading(false);

        // 이력은 상세 카드 렌더 후 비동기 로드 — 별도 스피너 표시
        setHistoryLoading(true);
        try {
          const histRes = await fetchGuestReservations(detailRes.data.guestName);
          if (cancelled) return;
          setGuestHistory(histRes.data);
        } catch (_) {
          // 이력 로드 실패 시 빈 목록 유지
        } finally {
          if (!cancelled) setHistoryLoading(false);
        }
      } catch (err) {
        if (cancelled) return;
        if (err.response?.status === 404) setNotFound(true);
        setLoading(false);
        setHistoryLoading(false);
      }
    };

    load();
    return () => { cancelled = true; };
  }, [id]);

  const nights = reservation
    ? Math.max(1, (new Date(reservation.checkOut) - new Date(reservation.checkIn)) / 86400000)
    : 0;

  const statusInfo = reservation
    ? (STATUS_MAP[reservation.status] ?? { label: reservation.status, className: 'badge-checkout' })
    : null;

  const payInfo = reservation
    ? (PAYMENT_STATUS_MAP[reservation.paymentStatus] ?? { label: reservation.paymentStatus, className: 'pay-paid' })
    : null;

  return (
    <div className="app">
      <header className="header">
        <div className="header-left">
          <span className="header-logo">🏨 Hotel Dashboard</span>
          <div className="header-divider" />
          <span className="header-subtitle">예약 상세</span>
        </div>
      </header>

      <main className="main-content">
        <div className="detail-back-row">
          <button className="back-btn" onClick={() => navigate('/')}>
            ← 예약 목록으로
          </button>
        </div>

        {/* ── 전체 로딩 ───────────────────────────────────── */}
        {loading && (
          <div className="detail-loading">
            <div className="spinner" />
            <p className="loading-text">예약 정보를 불러오는 중...</p>
          </div>
        )}

        {/* ── 404 ─────────────────────────────────────────── */}
        {!loading && notFound && (
          <div className="detail-not-found">
            <div className="not-found-icon">🔍</div>
            <h2 className="not-found-title">예약을 찾을 수 없습니다</h2>
            <p className="not-found-desc">
              예약번호 <strong>{id}</strong>에 해당하는 예약 정보가 없습니다.
            </p>
            <button className="back-btn" onClick={() => navigate('/')}>
              대시보드로 돌아가기
            </button>
          </div>
        )}

        {/* ── 상세 내용 ─────────────────────────────────── */}
        {!loading && reservation && (
          <>
            <div className="detail-title-row">
              <h1 className="detail-res-id">{reservation.reservationId}</h1>
              <span className={`status-badge ${statusInfo.className}`}>
                {statusInfo.label}
              </span>
            </div>

            <div className="detail-grid">
              {/* 예약 기본 정보 */}
              <div className="detail-card">
                <h2 className="detail-card-title">예약 기본 정보</h2>
                <div className="info-grid">
                  <span className="info-label">투숙객</span>
                  <span className="info-value info-bold">{reservation.guestName}</span>
                  <span className="info-label">연락처</span>
                  <span className="info-value">{reservation.guestPhone}</span>
                  <span className="info-label">이메일</span>
                  <span className="info-value">{reservation.guestEmail}</span>
                  <span className="info-label">체크인</span>
                  <span className="info-value">{reservation.checkIn}</span>
                  <span className="info-label">체크아웃</span>
                  <span className="info-value">{reservation.checkOut}</span>
                  <span className="info-label">박수</span>
                  <span className="info-value info-accent">{nights}박</span>
                </div>
              </div>

              {/* 호텔 정보 */}
              <div className="detail-card">
                <h2 className="detail-card-title">호텔 정보</h2>
                <div className="info-grid">
                  <span className="info-label">호텔명</span>
                  <span className="info-value info-bold">{reservation.hotelName}</span>
                  <span className="info-label">주소</span>
                  <span className="info-value">{reservation.hotelAddress}</span>
                  <span className="info-label">객실 타입</span>
                  <span className="info-value">{reservation.roomType}</span>
                  <span className="info-label">객실 번호</span>
                  <span className="info-value">{reservation.roomNumber}호</span>
                </div>
              </div>

              {/* 결제 정보 */}
              <div className="detail-card">
                <h2 className="detail-card-title">결제 정보</h2>
                <div className="info-grid">
                  <span className="info-label">1박 요금</span>
                  <span className="info-value">
                    {reservation.pricePerNight.toLocaleString()}원
                  </span>
                  <span className="info-label">총 요금</span>
                  <span className="info-value info-accent">
                    {reservation.amount.toLocaleString()}원
                  </span>
                  <span className="info-label">예약 채널</span>
                  <span className="info-value">{reservation.channel}</span>
                  <span className="info-label">결제 수단</span>
                  <span className="info-value">{reservation.paymentMethod}</span>
                  <span className="info-label">결제 상태</span>
                  <span className="info-value">
                    <span className={`pay-badge ${payInfo.className}`}>
                      {payInfo.label}
                    </span>
                  </span>
                </div>
              </div>

              {/* 특이사항 */}
              <div className="detail-card">
                <h2 className="detail-card-title">특이사항</h2>
                <div className="info-grid">
                  <span className="info-label">요청사항</span>
                  <span className="info-value">{reservation.specialRequest}</span>
                  <span className="info-label">예약 생성</span>
                  <span className="info-value">{reservation.createdAt}</span>
                  <span className="info-label">최종 수정</span>
                  <span className="info-value">{reservation.updatedAt}</span>
                </div>
              </div>
            </div>

            {/* ── 투숙객 예약 이력 ─────────────────────── */}
            <div className="history-section">
              <div className="history-header">
                <h2 className="history-title">
                  {reservation.guestName}님의 예약 이력
                </h2>
                {!historyLoading && guestHistory.length > 0 && (
                  <span className="history-count">총 {guestHistory.length}건</span>
                )}
              </div>

              {historyLoading ? (
                <div className="history-loading">
                  <div className="history-spinner" />
                </div>
              ) : guestHistory.length > 0 ? (
                <div className="table-scroll-wrapper">
                  <table className="history-table">
                    <thead>
                      <tr>
                        <th>예약번호</th>
                        <th>호텔명</th>
                        <th>체크인</th>
                        <th>체크아웃</th>
                        <th>채널</th>
                        <th className="col-right">금액</th>
                        <th>상태</th>
                      </tr>
                    </thead>
                    <tbody>
                      {guestHistory.map(h => {
                        const isCurrent = h.reservationId === reservation.reservationId;
                        const sInfo = STATUS_MAP[h.status] ?? { label: h.status, className: 'badge-checkout' };
                        const nights_h = Math.max(1,
                          (new Date(h.checkOut) - new Date(h.checkIn)) / 86400000);
                        return (
                          <tr
                            key={h.reservationId}
                            className={isCurrent ? 'history-row-current' : 'history-row'}
                          >
                            <td>
                              {isCurrent ? (
                                <span className="history-id-current">
                                  {h.reservationId}
                                  <span className="badge-now">현재</span>
                                </span>
                              ) : (
                                <Link
                                  to={`/reservation/${h.reservationId}`}
                                  className="history-id-link"
                                >
                                  {h.reservationId}
                                </Link>
                              )}
                            </td>
                            <td className="history-hotel">{h.hotelName}</td>
                            <td className="history-date">{h.checkIn}</td>
                            <td className="history-date">{h.checkOut}</td>
                            <td>
                              <span
                                className="history-channel"
                                style={{ borderLeft: `3px solid ${CHANNEL_COLORS[h.channel] ?? '#94a3b8'}` }}
                              >
                                {h.channel}
                              </span>
                            </td>
                            <td className="col-right">
                              <span className="history-amount">
                                {h.amount.toLocaleString()}
                                <span className="history-amount-unit">원</span>
                              </span>
                              <span className="history-nights">{nights_h}박</span>
                            </td>
                            <td>
                              <span className={`status-badge ${sInfo.className}`}>
                                {sInfo.label}
                              </span>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              ) : (
                <p className="history-empty">예약 이력이 없습니다.</p>
              )}
            </div>
          </>
        )}
      </main>
    </div>
  );
};

export default ReservationDetail;
