/*
 * Copyright (c) 2015-2017 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package com.tapinfluence.tracker.events;

import com.tapinfluence.tracker.constants.Parameters;
import com.tapinfluence.tracker.constants.TrackerConstants;
import com.tapinfluence.tracker.utils.Preconditions;
import com.tapinfluence.tracker.payload.TrackerPayload;

/**
 * Constructs a Structured event object.
 */
public class Structured extends AbstractEvent {

    private final String category;
    private final String action;
    private final String label;
    private final String property;
    private final Double value;

    public static abstract class Builder<T extends Builder<T>> extends AbstractEvent.Builder<T> {

        private String category;
        private String action;
        private String label;
        private String property;
        private Double value;

        /**
         * @param category Category of the event
         * @return itself
         */
        public T category(String category) {
            this.category = category;
            return self();
        }

        /**
         * @param action The event itself
         * @return itself
         */
        public T action(String action) {
            this.action = action;
            return self();
        }

        /**
         * @param label Refer to the object the action is performed on
         * @return itself
         */
        public T label(String label) {
            this.label = label;
            return self();
        }

        /**
         * @param property Property associated with either the action or the object
         * @return itself
         */
        public T property(String property) {
            this.property = property;
            return self();
        }

        /**
         * @param value A value associated with the user action
         * @return itself
         */
        public T value(Double value) {
            this.value = value;
            return self();
        }

        public Structured build() {
            return new Structured(this);
        }
    }

    private static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    protected Structured(Builder<?> builder) {
        super(builder);

        // Precondition checks
        Preconditions.checkNotNull(builder.category);
        Preconditions.checkNotNull(builder.action);
        Preconditions.checkArgument(!builder.category.isEmpty(), "category cannot be empty");
        Preconditions.checkArgument(!builder.action.isEmpty(), "action cannot be empty");

        this.category = builder.category;
        this.action = builder.action;
        this.label = builder.label;
        this.property = builder.property;
        this.value = builder.value;
    }

    /**
     * Returns a TrackerPayload which can be stored into
     * the local database.
     *
     * @return the payload to be sent.
     */
    public TrackerPayload getPayload() {
        TrackerPayload payload = new TrackerPayload();
        payload.add(Parameters.EVENT, TrackerConstants.EVENT_STRUCTURED);
        payload.add(Parameters.SE_CATEGORY, this.category);
        payload.add(Parameters.SE_ACTION, this.action);
        payload.add(Parameters.SE_LABEL, this.label);
        payload.add(Parameters.SE_PROPERTY, this.property);
        payload.add(Parameters.SE_VALUE,
                this.value != null ? Double.toString(this.value) : null);
        return putDefaultParams(payload);
    }
}
