package com.dlsc.formsfx.view.controls;

/* -
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

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


import static org.assertj.core.api.BDDAssertions.then;

/**
 * TestFX test class to verify that tooltips show up when hovering over child elements
 * of SimpleControl implementations using actual mouse hover simulation.
 */
@ExtendWith(ApplicationExtension.class)
public class SimpleControlTooltipTest {

    private Form form;
    private StringField field;

    @Start
    public void start(Stage stage) {
        field = Field.ofStringType("Test tooltip").label("Test tooltip")
                                .placeholder("placeholder")
                                .required("required_error_message")
                                .tooltip("This is the tooltip");

        form = Form.of(
            Group.of(field)
        ).title("Test");

        final BorderPane root = new BorderPane();
        root.setCenter(new FormRenderer(form));

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test_tooltip_shows_on_child_elements_hover(FxRobot robot) {
        final SimpleControl control = field.getRenderer();

        robot.moveTo(control.node);
        robot.sleep(600); // Wait for tooltip to appear (500ms delay + buffer)

        // Verify the tooltip is installed and has correct text
        then(control.tooltip.isShowing()).isTrue();
        then(control.tooltip.getText()).isEqualToIgnoringNewLines("This is the tooltip");
    }
/*
    @Test
    public void test_tooltip_shows_on_main_node_hover(FxRobot robot) {
        // Hover over the main control node
        robot.moveTo(control);
        robot.sleep(600); // Wait for tooltip to appear

        // Get the actual tooltip text
        Tooltip tooltip = control.tooltip;
        then(tooltip).isNotNull();
        then(tooltip.getText()).contains("Test tooltip for checkboxes");
    }

    @Test
    public void test_tooltip_with_error_messages(FxRobot robot) {
        // Add error messages to the field
        control.getField().errorMessagesProperty().addAll("Error 1", "Error 2");

        // Trigger tooltip text update
        control.tooltipText();

        // Verify tooltip contains both tooltip text and error messages
        Tooltip tooltip = control.tooltip;
        then(tooltip.getText()).contains("Test tooltip for checkboxes");
        then(tooltip.getText()).contains("Error 1");
        then(tooltip.getText()).contains("Error 2");
    }
*/
}
