import React, { useState } from 'react';
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#ef4444'];

const CustomTooltip = ({ active, payload }) => {
  if (active && payload && payload.length) {
    const d = payload[0].payload;
    return (
      <div className="custom-tooltip">
        <p className="tooltip-label">{d.channel}</p>
        <p className="tooltip-value">{d.count.toLocaleString()}건</p>
        <p className="tooltip-sub">{d.percentage}%</p>
      </div>
    );
  }
  return null;
};

const RADIAN = Math.PI / 180;
const renderCustomLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percentage }) => {
  if (percentage < 8) return null;
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);
  return (
    <text x={x} y={y} fill="white" textAnchor="middle" dominantBaseline="central" fontSize={12} fontWeight={700}>
      {`${percentage}%`}
    </text>
  );
};

const ChannelPieChart = ({ data }) => {
  const [activeIndex, setActiveIndex] = useState(null);

  return (
    <ResponsiveContainer width="100%" height={280} debounce={1}>
      <PieChart margin={{ top: 0, right: 0, bottom: 0, left: 0 }}>
        <Pie
          data={data}
          cx="50%"
          cy="46%"
          innerRadius={60}
          outerRadius={95}
          paddingAngle={3}
          dataKey="count"
          nameKey="channel"
          labelLine={false}
          label={renderCustomLabel}
          onMouseEnter={(_, index) => setActiveIndex(index)}
          onMouseLeave={() => setActiveIndex(null)}
        >
          {data.map((entry, index) => (
            <Cell
              key={`cell-${index}`}
              fill={COLORS[index % COLORS.length]}
              opacity={activeIndex === null || activeIndex === index ? 1 : 0.55}
              stroke="white"
              strokeWidth={2}
            />
          ))}
        </Pie>
        <Tooltip content={<CustomTooltip />} />
        <Legend
          iconType="circle"
          iconSize={9}
          wrapperStyle={{ fontSize: '12px', paddingTop: '8px' }}
          formatter={(value) => (
            <span style={{ color: '#374151', fontSize: '12px' }}>{value}</span>
          )}
        />
      </PieChart>
    </ResponsiveContainer>
  );
};

export default ChannelPieChart;
