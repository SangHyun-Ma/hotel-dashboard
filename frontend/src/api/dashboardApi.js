import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/dashboard',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const fetchTodayStatus = () => api.get('/today-status');
export const fetchChannelShare = () => api.get('/channel-share');
export const fetchMonthlyRevenue = () => api.get('/monthly-revenue');
export const fetchReservations = () => api.get('/reservations');
export const fetchReservationDetail = (id) => api.get(`/reservations/${id}`);
export const fetchGuestReservations = (guestName) =>
  api.get(`/reservations/guest/${encodeURIComponent(guestName)}`);
