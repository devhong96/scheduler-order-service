package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CancelOrderPayload implements EventPayload {

    private final Vendor vendor;
    private final String vendorTid;
    private final String username;
    private final String cancelReason;
    private final Integer cancelAmount;
    private final Integer taxAmount;

}
