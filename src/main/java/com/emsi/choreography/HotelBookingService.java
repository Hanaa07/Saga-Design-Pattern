package com.emsi.choreography;

public class HotelBookingService extends Service {
    public HotelBookingService(ServiceDiscoveryService service) {
        super(service);
    }

    @Override
    public String getName() {
        return "booking a Hotel";
    }


}