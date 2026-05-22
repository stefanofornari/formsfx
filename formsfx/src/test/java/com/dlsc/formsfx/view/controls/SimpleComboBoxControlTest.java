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
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.stage.Stage;

import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(ApplicationExtension.class)
public class SimpleComboBoxControlTest extends AbstractFxTest {

    @Start
    public void start(Stage stage) {
        super.start(stage);
    }

    private SingleSelectionField<String> field;

    @Override
    protected void init(javafx.stage.Stage stage, BorderPane root) {
        field = Field.ofSingleSelectionType(Arrays.asList("one", "two", "three"), 1).editable(true);

        createForm(field);
    }

    @Test
    public void test_combobox_freeText_reflects_field_freeText(FxRobot robot) throws Exception {
        final SimpleComboBoxControl<String> control = (SimpleComboBoxControl<String>) field.getRenderer();
        final ComboBox<String> comboBox = (ComboBox<String>) ((StackPane) control.node()).getChildren().get(0);

        // By default freeText is false -> editor not editable
        then(comboBox.isEditable()).isFalse();

        // Enable free text -> editor should become editable
        robot.interact(() -> field.freeText(true));
        waitFor(5, TimeUnit.SECONDS, () -> comboBox.isEditable());
        then(comboBox.isEditable()).isTrue();

        // Disable free text -> editor should become non-editable
        robot.interact(() -> field.freeText(false));
        waitFor(5, TimeUnit.SECONDS, () -> !comboBox.isEditable());
        then(comboBox.isEditable()).isFalse();
    }

    @Test
    public void test_combobox_user_typing_updates_field_value(FxRobot robot) throws Exception {
        final SimpleComboBoxControl<String> control = (SimpleComboBoxControl<String>) field.getRenderer();
        final ComboBox<String> comboBox = (ComboBox<String>) ((StackPane) control.node()).getChildren().get(0);

        // Ensure initial selection is index 1 -> value "two"
        then(field.getSelection()).isEqualTo("two");

        // Enable free text so editor accepts typing
        robot.interact(() -> field.freeText(true));

        // Focus the editor and type a new value
        robot.interact(() -> comboBox.show());
        robot.clickOn(comboBox.getEditor());
        robot.eraseText(comboBox.getEditor().getText().length());
        robot.write("typed value");
        // Commit the editor content
        robot.type(javafx.scene.input.KeyCode.ENTER);

        waitFor(5, TimeUnit.SECONDS, () -> field.getSelection() != null && field.getSelection().equals("typed value"));
        then(field.getSelection()).isEqualTo("typed value");
    }

    @Test
    public void test_combobox_default_not_editable(FxRobot robot) throws Exception {
        final SingleSelectionField<String> defaultField = Field.ofSingleSelectionType(Arrays.asList("a", "b", "c"));
        robot.interact(() -> createForm(defaultField));

        waitFor(5, TimeUnit.SECONDS, () -> defaultField.getRenderer() != null);
        final SimpleComboBoxControl<String> control = (SimpleComboBoxControl<String>) defaultField.getRenderer();
        final ComboBox<String> comboBox = (ComboBox<String>) ((StackPane) control.node()).getChildren().get(0);

        then(comboBox.isEditable()).isFalse();
    }

    @Test
    public void initial_selection_field_value_cases_b_and_c(FxRobot robot) throws Exception {
        // Case B: field with no predefined selection -> should select first item
        final SingleSelectionField<String> fieldNoSelection = Field.ofSingleSelectionType(Arrays.asList("x", "y", "z"));
        robot.interact(() -> createForm(fieldNoSelection));

        waitFor(5, TimeUnit.SECONDS, () -> fieldNoSelection.getRenderer() != null);
        final SimpleComboBoxControl<String> controlB = (SimpleComboBoxControl<String>) fieldNoSelection.getRenderer();
        final ComboBox<String> comboB = (ComboBox<String>) ((StackPane) controlB.node()).getChildren().get(0);

        waitFor(5, TimeUnit.SECONDS, () -> comboB.getSelectionModel().getSelectedIndex() == 0);
        then(comboB.getSelectionModel().getSelectedIndex()).isEqualTo(0);
        then(comboB.getValue()).isEqualTo("x");

        // Case C: selectionProperty has a value not present in items -> should display it
        final SingleSelectionField<String> fieldTextSelection = Field.ofSingleSelectionType(Arrays.asList("m", "n"));
        // Create the form first
        robot.interact(() -> createForm(fieldTextSelection));

        waitFor(5, TimeUnit.SECONDS, () -> fieldTextSelection.getRenderer() != null);

        // Then set the selection property so the control's listeners pick it up
        robot.interact(() -> fieldTextSelection.selectionProperty().setValue("hello"));

        final SimpleComboBoxControl<String> controlC = (SimpleComboBoxControl<String>) fieldTextSelection.getRenderer();
        final ComboBox<String> comboC = (ComboBox<String>) ((StackPane) controlC.node()).getChildren().get(0);

        waitFor(5, TimeUnit.SECONDS, () -> "hello".equals(comboC.getValue()));
        then(comboC.getValue()).isEqualTo("hello");
    }

    @Test
    public void labelSpan_default_is_2() {
        SimpleComboBoxControl<String> control = new SimpleComboBoxControl<>();
        then(control.labelSpan()).isEqualTo(2);
    }

    @Test
    public void labelSpan_can_be_set_via_method() {
        SimpleComboBoxControl<String> control = new SimpleComboBoxControl<>();
        control.labelSpan(4);
        then(control.labelSpan()).isEqualTo(4);
    }

    @Test
    public void label_uses_labelSpan_in_layout_when_columns_geq_3() {
        SingleSelectionField<String> testField = Field.ofSingleSelectionType(Arrays.asList("one", "two")).label("Test").span(5);
        SimpleComboBoxControl<String> control = new SimpleComboBoxControl<>();
        control.labelSpan(3);
        control.setField(testField);

        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(3);
    }

    @Test
    public void label_uses_labelSpan_from_field_in_layout_when_columns_geq_3() {
        SingleSelectionField<String> testField = Field.ofSingleSelectionType(Arrays.asList("one", "two")).label("Test").span(5).labelSpan(4);
        SimpleComboBoxControl<String> control = new SimpleComboBoxControl<>();
        control.setField(testField);

        then(control.labelSpan()).isEqualTo(4);
        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(4);
    }
}
