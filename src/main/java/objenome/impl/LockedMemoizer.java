/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objenome.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This implementation uses a read/write lock to ensure thread safety
 */
public abstract class LockedMemoizer<K, V> implements Memoizer<K,V> {
    private final Map<K, V> map;
    private final Lock readLock;
    private final Lock writeLock;

    public LockedMemoizer() {
        this.map = new LinkedHashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    @Override public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        // check to see if we already have a value
        readLock.lock();
        try {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            readLock.unlock();
        }

        // create a new value.  this may race and we might create more than one instance, but that's ok
        V newValue = create(key);
        if (newValue == null) {
            throw new NullPointerException("create returned null");
        }

        // write the new value and return it
        writeLock.lock();
        try {
            map.put(key, newValue);
            return newValue;
        } finally {
            writeLock.unlock();
        }
    }


    @Override
    public final String toString() {
        readLock.lock();
        try {
            return map.toString();
        } finally {
            readLock.unlock();
        }
    }
}