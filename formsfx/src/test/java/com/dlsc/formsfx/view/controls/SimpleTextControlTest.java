/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2026 DLSC Software & Consulting
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
package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.StringField;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(ApplicationExtension.class)
public class SimpleTextControlTest extends AbstractFxTest {

    private StringField field;

    @Start
    public void start(Stage stage) {
        super.start(stage);
    }

    @Override
    protected void init(Stage stage, javafx.scene.layout.BorderPane root) {
        field = Field.ofStringType("Test tooltip").label("Test tooltip")
                                .placeholder("placeholder")
                                .required("required_error_message")
                                .tooltip("This is the tooltip")
                                .styleClass("new-style-class-1", "new-style-class-2");

        createForm(field);
    }

    @Test
    public void add_class_styles_to_field_add_the_class_styles_to_the_node() {
        StackPane p = (StackPane)field.getRenderer().node;
        then(p.getStyleClass()).contains("new-style-class-1").contains("new-style-class-2");

        for (Node n: p.getChildren()) {
            then(n.getStyleClass()).contains("new-style-class-1").contains("new-style-class-2");
        }
    }

    @Test
    public void labelSpan_default_is_2() {
        SimpleTextControl control = new SimpleTextControl();
        then(control.labelSpan()).isEqualTo(2);
    }

    @Test
    public void labelSpan_can_be_set_via_constructor() {
        SimpleTextControl control = new SimpleTextControl(3);
        then(control.labelSpan()).isEqualTo(3);
    }

    @Test
    public void labelSpan_can_be_set_via_method() {
        SimpleTextControl control = new SimpleTextControl();
        control.labelSpan(4);
        then(control.labelSpan()).isEqualTo(4);
    }

    @Test
    public void label_uses_labelSpan_in_layout_when_columns_geq_3() {
        StringField testField = Field.ofStringType("test").label("Test").span(5);
        SimpleTextControl control = new SimpleTextControl(3);
        control.setField(testField);

        // In the layout, when columns >= 3, the label should span labelSpan columns
        // The label is added at (0, 0, labelSpan, 1)
        // We can verify by checking GridPane.getColumnSpan(fieldLabel)
        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(3);
    }

    @Test
    public void label_uses_labelSpan_from_field_in_layout_when_columns_geq_3() {
        StringField testField = Field.ofStringType("test").label("Test").span(5).labelSpan(4);
        SimpleTextControl control = new SimpleTextControl(); // Default labelSpan is 2
        control.setField(testField);

        then(control.labelSpan()).isEqualTo(4);
        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(4);
    }
}
