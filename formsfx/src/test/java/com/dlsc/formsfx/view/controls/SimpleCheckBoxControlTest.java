package com.dlsc.formsfx.view.controls;

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

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(ApplicationExtension.class)
public class SimpleCheckBoxControlTest extends AbstractFxTest {

    @Start
    public void start(Stage stage) {
        super.start(stage);
    }

    private Form form;
    private MultiSelectionField<Integer> field;

    @Override
    protected void init(javafx.stage.Stage stage, BorderPane root) {
        field = Field.ofMultiSelectionType(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));

        // Create the control directly instead of via FormRenderer to test the control's reaction
        final SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        control.setField(field);

        root.setCenter(control);
    }

    @Test
    public void test_checkboxes_update_when_items_setAll(FxRobot robot) throws Exception {
        final SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        robot.interact(() -> control.setField(field));

        // initial count
        then(((javafx.scene.layout.VBox) control.node()).getChildren().size()).isEqualTo(3);

        // Mutate items via setAll (same backing list) which previously didn't trigger rebuild
        robot.interact(() -> field.items(Arrays.asList(1,2,3,4,5), Arrays.asList(0,3)));

        waitFor(5, TimeUnit.SECONDS, () -> ((javafx.scene.layout.VBox) control.node()).getChildren().size() == 5);

        then(((javafx.scene.layout.VBox) control.node()).getChildren().size()).isEqualTo(5);
        // ensure first checkbox is selected (index 0 in new selection)
        then(((javafx.scene.control.CheckBox) ((javafx.scene.layout.VBox) control.node()).getChildren().get(0)).isSelected()).isTrue();
    }

    @Test
    public void labelSpan_default_is_2() {
        SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        then(control.labelSpan()).isEqualTo(2);
    }

    @Test
    public void labelSpan_can_be_set_via_method() {
        SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        control.labelSpan(4);
        then(control.labelSpan()).isEqualTo(4);
    }

    @Test
    public void label_uses_labelSpan_in_layout_when_columns_geq_3() {
        MultiSelectionField<Integer> testField = Field.ofMultiSelectionType(Arrays.asList(1, 2, 3)).label("Test").span(5);
        SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        control.labelSpan(3);
        control.setField(testField);

        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(3);
    }

    @Test
    public void label_uses_labelSpan_from_field_in_layout_when_columns_geq_3() {
        MultiSelectionField<Integer> testField = Field.ofMultiSelectionType(Arrays.asList(1, 2, 3)).label("Test").span(5).labelSpan(4);
        SimpleCheckBoxControl<Integer> control = new SimpleCheckBoxControl<>();
        control.setField(testField);

        then(control.labelSpan()).isEqualTo(4);
        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(4);
    }
}
