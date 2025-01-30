package com.app.orderprocessing.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Order")
public class Order {
    @Id
    private String orderId;
    private String orderStatus;
    private String customerId;

}
