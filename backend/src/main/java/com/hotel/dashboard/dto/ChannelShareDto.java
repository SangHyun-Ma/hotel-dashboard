package com.hotel.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelShareDto {
    private String channel;
    private int count;
    private double percentage;
}
