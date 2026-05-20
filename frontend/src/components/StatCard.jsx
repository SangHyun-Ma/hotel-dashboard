import React from 'react';
import './StatCard.css';

const StatCard = ({ title, value, prevValue, color, bgColor, icon, unit }) => {
  const change = value - prevValue;
  const changePct = prevValue > 0 ? Math.abs(((change / prevValue) * 100)).toFixed(1) : '0.0';
  const isPositive = change >= 0;
  const isZero = change === 0;

  return (
    <div className="stat-card">
      <div className="stat-card-top">
        <div className="stat-icon-wrap" style={{ backgroundColor: bgColor, color }}>
          <span className="stat-icon">{icon}</span>
        </div>
        <div
          className={`stat-badge ${isZero ? 'zero' : isPositive ? 'up' : 'down'}`}
        >
          {isZero ? '─' : isPositive ? '▲' : '▼'}&nbsp;{changePct}%
        </div>
      </div>

      <div className="stat-value" style={{ color: '#111827' }}>
        {value.toLocaleString()}
        <span className="stat-unit">{unit}</span>
      </div>

      <div className="stat-title">{title}</div>

      <div className="stat-compare">
        전일 대비&nbsp;
        <span style={{ color: isZero ? '#9ca3af' : isPositive ? '#059669' : '#dc2626', fontWeight: 600 }}>
          {isZero ? '변동없음' : `${isPositive ? '+' : ''}${change}${unit}`}
        </span>
      </div>
    </div>
  );
};

export default StatCard;
