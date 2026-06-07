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
import com.dlsc.formsfx.model.structure.IntegerField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(ApplicationExtension.class)
public class SimpleIntegerControlTest extends AbstractFxTest {

    private IntegerField field;

    @Start
    public void start(Stage stage) {
        super.start(stage);
    }

    @Override
    protected void init(Stage stage, javafx.scene.layout.BorderPane root) {
        field = Field.ofIntegerType(10).label("Test label");
        createForm(field);
    }
    
    @Test
    public void labelSpan_default_is_2() {
        SimpleIntegerControl control = new SimpleIntegerControl();
        then(control.labelSpan()).isEqualTo(2);
    }

    @Test
    public void labelSpan_can_be_set_via_method() {
        SimpleIntegerControl control = new SimpleIntegerControl();
        control.labelSpan(4);
        then(control.labelSpan()).isEqualTo(4);
    }

    @Test
    public void label_uses_labelSpan_in_layout_when_columns_geq_3() {
        IntegerField testField = Field.ofIntegerType(10).label("Test").span(5);
        SimpleIntegerControl control = new SimpleIntegerControl();
        control.labelSpan(3);
        control.setField(testField);

        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(3);
    }

    @Test
    public void label_uses_labelSpan_from_field_in_layout_when_columns_geq_3() {
        IntegerField testField = Field.ofIntegerType(10).label("Test").span(5).labelSpan(4);
        SimpleIntegerControl control = new SimpleIntegerControl();
        control.setField(testField);

        then(control.labelSpan()).isEqualTo(4);
        then(GridPane.getColumnSpan(control.fieldLabel())).isEqualTo(4);
    }
}
