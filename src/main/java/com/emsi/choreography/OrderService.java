package com.emsi.choreography;

/**
 * Class representing a service to init a new order.
 */
public class OrderService extends Service {

    public OrderService(ServiceDiscoveryService service) {
        super(service);
    }

    @Override
    public String getName() {
        return "init an order";
    }
}