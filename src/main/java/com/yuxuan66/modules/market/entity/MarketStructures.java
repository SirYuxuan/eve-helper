package com.yuxuan66.modules.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author Sir丶雨轩
 * @since 6/23/2022
 */
@Setter
@Getter
public class MarketStructures {

    private int duration;
    private int minVolume;
    private boolean isBuyOrder;
    private long price;
    private int typeId;
    private String range;
    private long volumeTotal;
    private Timestamp issued;
    private long orderId;
    private long volumeRemain;
    private long locationId;

}
