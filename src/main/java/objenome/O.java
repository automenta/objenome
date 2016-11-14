/*
 * Copyright (C) 2012 Square, Inc.
 * Copyright (C) 2012 Google, Inc.
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

import objenome.impl.*;
import objenome.impl.loaders.DynamicLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Originally named: "ObjectGraph" but this is too verbose for the expected level of ubiquity it could demonstrate
 *
 * A graph of objects linked by their dependencies.
 * <p>
 * <p>The following injection features are supported:
 * <ul>
 * <li>Field injection. A class may have any number of field injections, and
 * fields may be of any visibility. Static fields will be injected each
 * time an instance is injected.
 * <li>Constructor injection. A class may have a single
 * {@code @Inject}-annotated constructor. Classes that have fields
 * injected may omit the {@code @Inject} annotation if they have a public
 * no-arguments constructor.
 * <li>Injection of {@code @Provides} method parameters.
 * <li>{@code @Provides} methods annotated {@code @Singleton}.
 * <li>Constructor-injected classes annotated {@code @Singleton}.
 * <li>Injection of {@code Provider}s.
 * <li>Injection of {@code MembersInjector}s.
 * <li>Qualifier annotations on injected parameters and fields.
 * <li>JSR 330 annotations.
 * </ul>
 * <p>
 * <p>The following injection features are not currently supported:
 * <ul>
 * <li>Method injection.</li>
 * <li>Circular dependencies.</li>
 * </ul>
 */
public abstract class O {
    protected O() {
    }

    /**
     * Returns an instance of {@code type}.
     * As in, "a"/"an".
     *
     * @throws IllegalArgumentException if {@code type} is not one of this object
     *                                  graph's {@link the#in injectable types}.
     */
    public abstract <T> T a(Class<T> type);

    /**
     * Injects the members of {@code instance}, including injectable members
     * inherited from its supertypes.
     *
     * @throws IllegalArgumentException if the runtime type of {@code instance} is
     *                                  not one of this object graph's {@link the#in injectable types}.
     */
    public abstract <T> T with(T instance);

    /**
     * Returns a new object graph that includes all of the objects in this graph,
     * plus additional objects in the {@literal @}{@link the}-annotated
     * modules. This graph is a subgraph of the returned graph.
     * <p>
     * <p>The current graph is not modified by this operation: its objects and the
     * dependency links between them are unchanged. But this graph's objects may
     * be shared by both graphs. For example, the singletons of this graph may be
     * injected and used by the returned graph.
     * <p>
     * <p>This <strong>does not</strong> inject any members or validate the graph.
     * See {@link #of} for guidance on injection and validation.
     */
    public abstract O plus(Object... modules);

    /**
     * Do runtime graph problem detection. For fastest graph creation, rely on
     * build time tools for graph validation.
     *
     * @throws IllegalStateException if this graph has problems.
     */
    public abstract void validate();

    /**
     * Injects the static fields of the classes listed in the object graph's
     * {@code staticInjections} property.
     */
    public abstract void injectStatics();

    /**
     * Returns a new dependency graph using the {@literal @}{@link
     * the}-annotated modules.
     * <p>
     * <p>This <strong>does not</strong> inject any members. Most applications
     * should call {@link #injectStatics} to inject static members and {@link
     * #with} or get {@link #a(Class)} to inject instance members when this
     * method has returned.
     * <p>
     * <p>This <strong>does not</strong> validate the graph. Rely on build time
     * tools for graph validation, or call {@link #validate} to find problems in
     * the graph at runtime.
     */
    public static O of(Object... modules) {
        //return DaggerObjectGraph.makeGraph(null, new FailoverLoader(), modules);
        return load(
                //new FailoverLoader(),
                new DynamicLoader(),
                modules);
    }

    // visible for testing
    public static O load(Loader loader, Object... modules) {
        return DaggerObjectGraph.newGraph(null, loader, modules);
    }


    /**
     * A BindingsGroup which fails when existing values are clobbered and sets aside
     * {@link SetBinding}.
     */
    protected static final class StandardBindings extends BindingsGroup {
        public final List<SetBinding<?>> setBindings;

        public StandardBindings() {
            setBindings = new ArrayList<>();
        }

        public StandardBindings(List<SetBinding<?>> baseSetBindings) {
            setBindings = new ArrayList<>(baseSetBindings.size());
            for (SetBinding<?> sb : baseSetBindings) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                SetBinding<?> child = new SetBinding(sb);
                setBindings.add(child);
                put(child.provideKey, child);
            }
        }

        @Override
        public void contributeSetBinding(String key, SetBinding<?> value) {
            setBindings.add(value);
            super.put(key, value);
        }
    }

    /**
     * A BindingsGroup which throws an {@link IllegalArgumentException} when a
     * {@link SetBinding} is contributed, since overrides modules cannot contribute such
     * bindings.
     */
    protected static final class OverridesBindings extends BindingsGroup {
        public OverridesBindings() {
        }

        @Override
        public void contributeSetBinding(String key, SetBinding<?> value) {
            throw new IllegalArgumentException("Module overrides cannot contribute set bindings.");
        }
    }
}
