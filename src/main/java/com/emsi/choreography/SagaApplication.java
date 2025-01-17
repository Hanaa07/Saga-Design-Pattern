package com.emsi.choreography;

public class SagaApplication {

    /**
     * main method.
     */
    public static void main(String[] args) {
        var sd = serviceDiscovery();
        var service = sd.findAny();
        var goodOrderSaga = service.execute(newSaga("good_order"));
        var badOrderSaga = service.execute(newSaga("bad_order"));
        System.out.println("orders: goodOrder is {}, badOrder is {}" +
                goodOrderSaga.getResult() + badOrderSaga.getResult());

    }


    private static Saga newSaga(Object value) {
        return Saga
                .create()
                .chapter("init an order").setInValue(value)
                .chapter("booking a Fly")
                .chapter("booking a Hotel")
                .chapter("withdrawing Money");
    }

    private static ServiceDiscoveryService serviceDiscovery() {
        var sd = new ServiceDiscoveryService();
        return sd
                .discover(new OrderService(sd))
                .discover(new FlyBookingService(sd))
                .discover(new HotelBookingService(sd))
                .discover(new WithdrawMoneyService(sd));
    }
}