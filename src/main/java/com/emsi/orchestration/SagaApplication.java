package com.emsi.orchestration;

public class SagaApplication {

    /**
     * method to show common saga logic.
     */
    public static void main(String[] args) {
        var sagaOrchestrator = new SagaOrchestrator(newSaga(), serviceDiscovery());

        Saga.Result goodOrder = sagaOrchestrator.execute("good_order");
        Saga.Result badOrder = sagaOrchestrator.execute("bad_order");
        Saga.Result crashedOrder = sagaOrchestrator.execute("crashed_order");

        System.out.println("orders: goodOrder is {}, badOrder is {},crashedOrder is {}" +
                goodOrder + badOrder + crashedOrder);
    }


    private static Saga newSaga() {
        return Saga
                .create()
                .chapter("init an order")
                .chapter("booking a Fly")
                .chapter("booking a Hotel")
                .chapter("withdrawing Money");
    }

    private static ServiceDiscoveryService serviceDiscovery() {
        return new ServiceDiscoveryService()
                .discover(new OrderService())
                .discover(new FlyBookingService())
                .discover(new HotelBookingService())
                .discover(new WithdrawMoneyService());
    }
}