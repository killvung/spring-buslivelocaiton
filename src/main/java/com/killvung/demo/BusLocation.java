package com.killvung.demo;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "bus_locations")
public record BusLocation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @GenericGenerator(name = "native")
        Long vehicleId,
        String routeTag,
        String dirTag,
        Double latitude,
        Double longitude,
        Integer secsSinceReport,
        Boolean predictable,
        Integer heading,
        Double speedKmHr,
        @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
        LocalDateTime createdAt
) {}

