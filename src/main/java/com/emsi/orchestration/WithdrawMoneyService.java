package com.emsi.orchestration;

/**
 * Class representing a service to withdraw a money.
 */
public class WithdrawMoneyService extends Service<String> {
    @Override
    public String getName() {
        return "withdrawing Money";
    }

    @Override
    public ChapterResult<String> process(String value) {
        if (value.equals("bad_order") || value.equals("crashed_order")) {
            System.out.println("The chapter '{}' has been started. But the exception has been raised."
                            + "The rollback is about to start" +
                    getName());
            return ChapterResult.failure(value);
        }
        return super.process(value);
    }
}