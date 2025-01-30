package com.app.orderprocessing.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Customer")


public class Customer {
    @Id
    private String customerId;
    private String customerName;

}
