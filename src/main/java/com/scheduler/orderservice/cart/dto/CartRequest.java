package com.scheduler.orderservice.cart.dto;

import com.scheduler.orderservice.cart.domain.OrderCategory;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;

public class CartRequest {

    @Getter
    @Setter
    public static class CartCreateRequest {

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private String productName;

        private Integer cost;

        private Integer quantity;

        private Integer totalPrice;

        private Boolean checked;
    }
    @Getter
    @Setter
    public static class UpdateQuantityRequest {
        private String cartId;

        private Integer quantity;

    }

    @Getter
    @Setter
    public static class UpdateCheckedRequest {
        private String cartId;

        private Boolean checked;
    }

    @Getter
    @Setter
    public static class DeleteCartRequest {
        private String cartId;

    }


}
