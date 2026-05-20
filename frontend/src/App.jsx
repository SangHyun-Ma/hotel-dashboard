import React, { useState, useEffect, useCallback } from 'react';
import './App.css';
import StatCard from './components/StatCard';
import RevenueLineChart from './components/LineChart';
import ChannelPieChart from './components/PieChart';
import ReservationTable from './components/ReservationTable';
import {
  fetchTodayStatus,
  fetchChannelShare,
  fetchMonthlyRevenue,
  fetchReservations,
} from './api/dashboardApi';

const REFRESH_INTERVAL = 5000;

function App() {
  const [todayStatus, setTodayStatus] = useState(null);
  const [channelShare, setChannelShare] = useState([]);
  const [monthlyRevenue, setMonthlyRevenue] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [lastUpdated, setLastUpdated] = useState(null);

  const fetchAll = useCallback(async () => {
    try {
      const [statusRes, channelRes, revenueRes, reservationRes] = await Promise.all([
        fetchTodayStatus(),
        fetchChannelShare(),
        fetchMonthlyRevenue(),
        fetchReservations(),
      ]);
      setTodayStatus(statusRes.data);
      setChannelShare(channelRes.data);
      setMonthlyRevenue(revenueRes.data);
      setReservations(reservationRes.data);
      setLastUpdated(new Date());
      setError(null);
    } catch (err) {
      setError('백엔드 서버에 연결할 수 없습니다. http://localhost:8080 이 실행 중인지 확인하세요.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchAll();
    const interval = setInterval(fetchAll, REFRESH_INTERVAL);
    return () => clearInterval(interval);
  }, [fetchAll]);

  useEffect(() => {
    const interval = setInterval(() => setCurrentTime(new Date()), 1000);
    return () => clearInterval(interval);
  }, []);

  const formatDateTime = (date) =>
    date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      weekday: 'short',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
    });

  if (loading) {
    return (
      <div className="loading-screen">
        <div className="loading-logo">🏨 Hotel Dashboard</div>
        <div className="spinner" />
        <p className="loading-text">데이터를 불러오는 중...</p>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="header">
        <div className="header-left">
          <span className="header-logo">🏨 Hotel Dashboard</span>
          <div className="header-divider" />
          <span className="header-subtitle">실시간 예약 현황</span>
        </div>
        <div className="header-right">
          <span className="current-time">{formatDateTime(currentTime)}</span>
          {lastUpdated && (
            <span className="last-updated">
              마지막 업데이트: {lastUpdated.toLocaleTimeString('ko-KR')}
            </span>
          )}
        </div>
      </header>

      <main className="main-content">
        {error && (
          <div className="error-banner">
            ⚠️ {error}
          </div>
        )}

        <section className="kpi-section">
          <StatCard
            title="오늘 체크인"
            value={todayStatus?.todayCheckIn ?? 0}
            prevValue={todayStatus?.prevCheckIn ?? 0}
            color="#3b82f6"
            bgColor="#eff6ff"
            icon="🏃"
            unit="명"
          />
          <StatCard
            title="오늘 체크아웃"
            value={todayStatus?.todayCheckOut ?? 0}
            prevValue={todayStatus?.prevCheckOut ?? 0}
            color="#10b981"
            bgColor="#ecfdf5"
            icon="👋"
            unit="명"
          />
          <StatCard
            title="현재 투숙"
            value={todayStatus?.currentGuests ?? 0}
            prevValue={todayStatus?.prevGuests ?? 0}
            color="#f59e0b"
            bgColor="#fffbeb"
            icon="🛏️"
            unit="명"
          />
          <StatCard
            title="오늘 예약"
            value={todayStatus?.todayReservations ?? 0}
            prevValue={todayStatus?.prevReservations ?? 0}
            color="#8b5cf6"
            bgColor="#f5f3ff"
            icon="📋"
            unit="건"
          />
        </section>

        <section className="charts-section">
          <div className="chart-card">
            <h2 className="chart-title">이번 달 매출 트렌드</h2>
            <p className="chart-subtitle">일별 객실 매출 (단위: 만원)</p>
            <RevenueLineChart data={monthlyRevenue} />
          </div>
          <div className="chart-card">
            <h2 className="chart-title">채널별 예약 비율</h2>
            <p className="chart-subtitle">OTA 및 직접 채널 분포</p>
            <ChannelPieChart data={channelShare} />
          </div>
        </section>

        <section className="table-section">
          <div className="table-card">
            <div className="table-header">
              <div className="table-meta">
                <h2 className="chart-title">최근 예약 현황</h2>
                <p className="chart-subtitle" style={{ marginBottom: 0 }}>최근 20건</p>
              </div>
              <div className="refresh-indicator">
                <span className="refresh-dot" />
                5초마다 자동 갱신
              </div>
            </div>
            <ReservationTable reservations={reservations} />
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;
