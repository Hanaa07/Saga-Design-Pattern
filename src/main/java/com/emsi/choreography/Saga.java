package com.emsi.choreography;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Saga representation. Saga consists of chapters. Every ChoreographyChapter is executed a certain
 * service.
 */
public class Saga {

    private final List<Chapter> chapters;
    private int pos;
    private boolean forward;
    private boolean finished;


    public static Saga create() {
        return new Saga();
    }

    /**
     * get resuzlt of saga.
     *
     * @return result of saga @see {@link SagaResult}
     */
    public SagaResult getResult() {
        if (finished) {
            return forward
                    ? SagaResult.FINISHED
                    : SagaResult.ROLLBACKED;
        }

        return SagaResult.PROGRESS;
    }

    /**
     * add chapter to saga.
     *
     * @param name chapter name
     * @return this
     */
    public Saga chapter(String name) {
        this.chapters.add(new Chapter(name));
        return this;
    }

    /**
     * set value to last chapter.
     *
     * @param value invalue
     * @return this
     */
    public Saga setInValue(Object value) {
        if (chapters.isEmpty()) {
            return this;
        }
        chapters.get(chapters.size() - 1).setInValue(value);
        return this;
    }

    /**
     * get value from current chapter.
     *
     * @return value
     */
    public Object getCurrentValue() {
        return chapters.get(pos).getInValue();
    }

    /**
     * set value to current chapter.
     *
     * @param value to set
     */
    public void setCurrentValue(Object value) {
        chapters.get(pos).setInValue(value);
    }

    /**
     * set status for current chapter.
     *
     * @param result to set
     */
    public void setCurrentStatus(ChapterResult result) {
        chapters.get(pos).setResult(result);
    }

    void setFinished(boolean finished) {
        this.finished = finished;
    }

    boolean isForward() {
        return forward;
    }

    int forward() {
        return ++pos;
    }

    int back() {
        this.forward = false;
        return --pos;
    }


    private Saga() {
        this.chapters = new ArrayList<>();
        this.pos = 0;
        this.forward = true;
        this.finished = false;
    }

    Chapter getCurrent() {
        return chapters.get(pos);
    }


    boolean isPresent() {
        return pos >= 0 && pos < chapters.size();
    }

    boolean isCurrentSuccess() {
        return chapters.get(pos).isSuccess();
    }

    /**
     * Class presents a chapter status and incoming parameters(incoming parameter transforms to
     * outcoming parameter).
     */
    public static class Chapter {
        @Getter
        private final String name;
        @Setter
        private ChapterResult result;
        @Getter
        @Setter
        private Object inValue;

        public Chapter(String name) {
            this.name = name;
            this.result = ChapterResult.INIT;
        }

        /**
         * the result for chapter is good.
         *
         * @return true if is good otherwise bad
         */
        public boolean isSuccess() {
            return result == ChapterResult.SUCCESS;
        }
    }


    /**
     * result for chapter.
     */
    public enum ChapterResult {
        INIT, SUCCESS, ROLLBACK
    }

    /**
     * result for saga.
     */
    public enum SagaResult {
        PROGRESS, FINISHED, ROLLBACKED
    }

    @Override
    public String toString() {
        return "Saga{"
                + "chapters="
                + Arrays.toString(chapters.toArray())
                + ", pos="
                + pos
                + ", forward="
                + forward
                + '}';
    }
}