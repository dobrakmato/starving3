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
package eu.matejkormuth.starving.zombies.spawning;

public class IntStack {
    private int[] stack;
    private int top;

    /**
     * Creates a new IntStack with default starting capacity (16).
     */
    public IntStack() {
        this(16);
    }

    /**
     * Creates a new IntStack with specified starting capacity.
     * 
     * @param size
     *            starting capacity of stack
     */
    public IntStack(int size) {
        this.stack = new int[size];
        this.top = 0;
    }

    public void push(int obj) {
        if (this.top == stack.length) {
            this.doubleSize();
        }

        this.stack[this.top++] = obj;
    }

    public int pop() {
        return this.stack[--this.top];
    }

    public int[] exposeBackingArray() {
        return this.stack;
    }

    public int[] toArray() {
        int[] array = new int[this.top];
        System.arraycopy(this.stack, 0, array, 0, this.top);
        return array;
    }

    public int size() {
        return this.top;
    }
    
    public boolean isEmpty() {
        return this.top == 0;
    }

    /**
     * Doubles the size of backing array.
     */
    private void doubleSize() {
        int[] newStack = new int[this.stack.length * 2];
        System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
        this.stack = newStack;
    }
}