/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
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
package com.dlsc.formsfx.view.util;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;


/**
 *
 */
public class FieldTooltip extends Tooltip {

    public FieldTooltip(final String tooltip) {
        super(tooltip);
        getStyleClass().add("simple-tooltip");
        setShowDelay(Duration.millis(500));
        setMaxWidth(300);
        setWrapText(true);
    }

    public FieldTooltip() {
        this(null);
    }

}
