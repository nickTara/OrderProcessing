package com.app.orderprocessing.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {

    private String customerId;
    private List<String> items;

}
