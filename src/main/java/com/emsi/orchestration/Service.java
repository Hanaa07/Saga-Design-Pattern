package com.emsi.orchestration;

/**
 * Common abstraction class representing services. implementing a general contract @see {@link
 * OrchestrationChapter}
 *
         * @param <K> type of incoming param
 */
public abstract class Service<K> implements OrchestrationChapter<K> {

    @Override
    public abstract String getName();


    @Override
    public ChapterResult<K> process(K value) {
        System.out.println("The chapter '{}' has been started. "
                        + "The data {} has been stored or calculated successfully" +
                getName() + value);
        return ChapterResult.success(value);
    }

    @Override
    public ChapterResult<K> rollback(K value) {
        System.out.println("The Rollback for a chapter '{}' has been started. "
                        + "The data {} has been rollbacked successfully" +
                getName() + value);
        return ChapterResult.success(value);
    }


}