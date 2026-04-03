package com.dlsc.formsfx.view.util;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2019 - 2026 DLSC Software & Consulting GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

@FunctionalInterface
public interface VisibilityProperty {
    BooleanProperty get();

    /**
     * Creates a {@link VisibilityProperty} that is based on the given {@link Property}.
     *
     * The {@link VisibilityProperty} value will be set automatically when the given {@link Property} value changes.
     * The new value of the {@link VisibilityProperty} will be the result of the given {@link Function} applied to the
     * new value of the {@link Property}.
     *
     * @param property
     * @param visibilityFunc
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(ObservableValue<T> property, Function<T, Boolean> visibilityFunc) {
        return () -> {
            BooleanProperty visibilityProperty = new SimpleBooleanProperty(true);

            property.addListener((observable, oldValue, newValue) -> visibilityProperty.set(visibilityFunc.apply(newValue)));

            // set the initial value of the visibility property properly
            visibilityProperty.set(visibilityFunc.apply(property.getValue()));
            return visibilityProperty;
        };
    }

    /**
     * Simplified constructor for {@link VisibilityProperty} that is based on the given Boolean type {@link Property}
     *
     * The value of the {@link VisibilityProperty} will be set to the value of the given {@link Property}, i.e.,
     * whenever the referenced {@link Property} is true, the value of the @link VisibilityProperty} will be true as well.
     *
     * @param property
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(ObservableValue<Boolean> property) {
        return VisibilityProperty.of(property, (newValue) -> newValue);
    }

}
