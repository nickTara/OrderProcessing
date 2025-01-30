package com.app.orderprocessing.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private List<String> items; // this can be made a separate model class later
    private String customerId;

}
