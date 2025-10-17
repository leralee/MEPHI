package com.leralee.hotelservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

}
