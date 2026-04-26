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

import javafx.application.Platform;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Tests for {@link FieldTooltip}.
 */
public class FieldTooltipTest {

    @BeforeAll
    public static void before() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ex) {
            // JavaFX may only be initialized once.
        }
    }

    @Test
    public void constructor_with_string_sets_properties() {
        String text = "Test Tooltip";
        FieldTooltip tooltip = new FieldTooltip(text);

        then(tooltip.getText()).isEqualTo(text);
        then(tooltip.getStyleClass()).contains("simple-tooltip");
        then(tooltip.getShowDelay()).isEqualTo(Duration.millis(500));
        then(tooltip.getMaxWidth()).isEqualTo(300.0);
        then(tooltip.isWrapText()).isTrue();
    }

    @Test
    public void default_constructor_sets_properties() {
        FieldTooltip tooltip = new FieldTooltip();

        then(tooltip.getText()).isEmpty();
        then(tooltip.getStyleClass()).contains("simple-tooltip");
        then(tooltip.getShowDelay()).isEqualTo(Duration.millis(500));
        then(tooltip.getMaxWidth()).isEqualTo(300.0);
        then(tooltip.isWrapText()).isTrue();
    }
}
