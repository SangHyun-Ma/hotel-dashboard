import React from 'react';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  ReferenceLine,
} from 'recharts';

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    const value = payload[0].value;
    return (
      <div className="custom-tooltip">
        <p className="tooltip-label">{label}</p>
        <p className="tooltip-value">{value.toLocaleString()}만원</p>
        <p className="tooltip-sub">({(value * 10000).toLocaleString()}원)</p>
      </div>
    );
  }
  return null;
};

const RevenueLineChart = ({ data }) => {
  const chartData = data.map((item) => ({
    ...item,
    revenueMan: Math.round(item.revenue / 10000),
  }));

  const avg =
    chartData.length > 0
      ? Math.round(chartData.reduce((s, d) => s + d.revenueMan, 0) / chartData.length)
      : 0;

  const formatYAxis = (v) => v === 0 ? '0' : `${(v / 100).toFixed(0)}백만`;

  return (
    <ResponsiveContainer width="100%" height={270}>
      <AreaChart data={chartData} margin={{ top: 8, right: 16, left: 8, bottom: 0 }}>
        <defs>
          <linearGradient id="revenueGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.25} />
            <stop offset="95%" stopColor="#3b82f6" stopOpacity={0.02} />
          </linearGradient>
        </defs>
        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" vertical={false} />
        <XAxis
          dataKey="date"
          tick={{ fontSize: 11, fill: '#9ca3af' }}
          axisLine={false}
          tickLine={false}
          interval="preserveStartEnd"
        />
        <YAxis
          tick={{ fontSize: 11, fill: '#9ca3af' }}
          axisLine={false}
          tickLine={false}
          tickFormatter={formatYAxis}
          width={56}
        />
        <Tooltip content={<CustomTooltip />} cursor={{ stroke: '#e5e7eb', strokeWidth: 1 }} />
        {avg > 0 && (
          <ReferenceLine
            y={avg}
            stroke="#94a3b8"
            strokeDasharray="4 4"
            label={{
              value: `평균 ${avg.toLocaleString()}만`,
              position: 'insideTopRight',
              fontSize: 10,
              fill: '#94a3b8',
            }}
          />
        )}
        <Area
          type="monotone"
          dataKey="revenueMan"
          stroke="#3b82f6"
          strokeWidth={2.5}
          fill="url(#revenueGrad)"
          dot={false}
          activeDot={{ r: 5, fill: '#1d4ed8', strokeWidth: 2, stroke: '#fff' }}
        />
      </AreaChart>
    </ResponsiveContainer>
  );
};

export default RevenueLineChart;
