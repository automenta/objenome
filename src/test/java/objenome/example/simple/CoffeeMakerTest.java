/**
 * Copyright (C) 2015 Square, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objenome.example.simple;

import objenome.O;

import objenome.in;

import objenome.out;
import objenome.the;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Singleton;

public class CoffeeMakerTest {
    @in CoffeeMaker coffeeMaker;
    @in Heater heater;

    @the(
        extend = DripCoffeeModule.class,
        in = CoffeeMakerTest.class,
        override = true
    ) public static class TestModule {
        @out @Singleton Heater heater() {
            return Mockito.mock(Heater.class);
        }
    }

    @Before
    public void setUp() {
        O.of(new TestModule()).with(this);
    }

    @Test
    public void testHeaterIsTurnedOnAndThenOff() {
        Mockito.when(heater.isHot()).thenReturn(true);
        coffeeMaker.brew();
        Mockito.verify(heater, Mockito.times(1)).on();
        Mockito.verify(heater, Mockito.times(1)).off();
    }


}
