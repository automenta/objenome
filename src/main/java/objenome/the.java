/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objenome;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class that contributes to the object graph.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface the {
    /**
     * Returns classes that object graphs created with this module must be able to
     * inject. This includes both classes passed to {@link O#a},
     * the types of instances passed {@link O#with} and
     * {@link in} annotated classes that need to be scoped to the
     * resulting object graphs.
     * <p>
     * <p>It is an error to call {@link O#a} or {@link
     * O#with} with a type that isn't listed in the {@code injects}
     * set for any of the object graph's modules. Making such a call will trigger
     * an {@code IllegalArgumentException} at runtime.
     * <p>
     * <p>Maintaining this set is onerous, but doing so provides benefits to the
     * application. This set enables dagger to perform more aggressive static
     * analysis than would be otherwise possible:
     * <ul>
     * <li><strong>Detect missing bindings.</strong> Dagger can check that all
     * injected dependencies can be satisfied. Set {@code complete=false} to
     * disable this check for the current module.
     * <li><strong>Detect unused bindings.</strong> Dagger can check that all
     * provides methods are used to satisfy injected dependencies. Set
     * {@code library=true} to disable this check for the current module.
     * </ul>
     */
    Class<?>[] in() default {};

    Class<?>[] The() default {};

    /**
     * True if {@code @Provides} methods from this module are permitted to
     * override those of other modules. This is a dangerous feature as it permits
     * binding conflicts to go unnoticed. It should only be used in test and
     * development modules.
     */
    boolean override() default false;

    /**
     * Additional {@code @Module}-annotated classes from which this module is
     * composed. The de-duplicated contributions of the modules in
     * {@code includes}, and of their inclusions recursively, are all contributed
     * to the object graph.
     */
    Class<?>[] extend() default {};

    /**
     * An optional {@code @Module}-annotated class upon which this module can be
     * {@link O#plus added} to form a complete graph.
     */
    Class<?> addsTo() default Void.class;

    /**
     * True if all of the bindings required by this module can also be satisfied
     * by this module, its {@link #extend} and its {@link #addsTo}. If a module
     * is complete it is eligible for additional static checking: tools can detect
     * if required bindings are not available. Modules that have external
     * dependencies must use {@code complete = false}.
     */
    boolean complete() default true;

    /**
     * False if all the included bindings in this module are necessary to satisfy
     * all of its {@link #in injectable types}. If a module is not a library
     * module, it is eligible for additional static checking: tools can detect if
     * included bindings are not necessary. If you provide bindings that are not
     * used by this module's graph, then you must declare {@code library = true}.
     * <p>
     * <p>This is intended to help you detect dead code.
     */
    boolean library() default false;
}
