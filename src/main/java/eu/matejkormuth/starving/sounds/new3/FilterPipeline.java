/**
 * Starving - Bukkit API server mod with Zombies.
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.starving.sounds.new3;

import javax.annotation.Nonnull;

/**
 * Represents pipeline of filters linked
 */
public class FilterPipeline implements Listener {

    /**
     * First node in pipeline.
     */
    private FilterNode pipeline;

    /**
     * Output of this pipeline.
     */
    private Listener output;

    @Override
    public void listen(@Nonnull SoundEvent event) {
        // Apply filters in pipeline.
        pipeline.apply(event);
    }

    /**
     * Adds specified filter to the end of this pipeline.
     *
     * @param filter filter to add
     * @return instance of self
     */
    public FilterPipeline filter(@Nonnull Filter filter) {
        if (pipeline == null) {
            pipeline = new FilterNode(filter);
        } else {
            pipeline.addLast(new FilterNode(filter));
        }
        return this;
    }

    /**
     * Sets output (target) of this pipeline. This is last operation
     * executed in pipeline, so the method does not return instance
     * of this FilterPipeline.
     *
     * @param output output to set this pipeline to
     */
    public void output(@Nonnull Listener output) {
        this.output = output;
    }

    /**
     * Represents node in pipeline linked list.
     */
    private static class FilterNode {
        private FilterNode next;
        private Filter filter;

        public FilterNode(Filter filter) {
            this.filter = filter;
        }

        /**
         * Adds specified node to the end of linked list by traversing
         * the whole list.
         *
         * @param node node to add
         */
        public void addLast(@Nonnull FilterNode node) {
            if (this.next == null) {
                // Set this node's next as last.
                this.next = node;
            } else {
                // Traverse deeper.
                this.next.addLast(node);
            }
        }

        /**
         * Applies all filters in this linked list in order they were
         * inserted to the list.
         *
         * @param event event to filter
         */
        public void apply(@Nonnull SoundEvent event) {
            this.filter.apply(event);

            if (this.next != null) {
                this.next.apply(event);
            }
        }
    }
}
