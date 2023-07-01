package com.killvung.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "bus_locations")
public record BusLocation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long vehicleId,
        String routeTag,
        String dirTag,
        Double latitude,
        Double longitude,
        Integer secsSinceReport,
        Boolean predictable,
        Integer heading,
        Double speedKmHr
) {}

