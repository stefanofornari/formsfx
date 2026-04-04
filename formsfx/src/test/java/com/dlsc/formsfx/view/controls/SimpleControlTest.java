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
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.structure.StringField;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SimpleControlTest {

    @BeforeAll
    public static void before() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ex) {
            // JavaFX may only be initialized once.
        }
    }

    @Test
    public void itemsTest() {
        MultiSelectionField<Integer> mf = Field.ofMultiSelectionType(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));
        SingleSelectionField<Integer> sf = Field.ofSingleSelectionType(Arrays.asList(1, 2, 3), 1);

        SimpleCheckBoxControl<Integer> cb = new SimpleCheckBoxControl<>();
        cb.setField(mf);

        SimpleListViewControl<Integer> lv = new SimpleListViewControl<>();
        lv.setField(mf);

        SimpleRadioButtonControl<Integer> rb = new SimpleRadioButtonControl<>();
        rb.setField(sf);

        SimpleComboBoxControl<Integer> cmb = new SimpleComboBoxControl<>();
        cmb.setField(sf);

        then(((VBox) cb.node()).getChildren().size()).isEqualTo(3);
        then(((CheckBox) ((VBox) cb.node()).getChildren().get(1)).isSelected()).isTrue();

        then(((ListView) lv.node()).getItems().size()).isEqualTo(3);
        then(((ListView) lv.node()).getSelectionModel().isSelected(1)).isTrue();

        then(((VBox) rb.node()).getChildren().size()).isEqualTo(3);
        then(((RadioButton) ((VBox) rb.node()).getChildren().get(1)).isSelected()).isTrue();

        then(((ComboBox) ((StackPane) cmb.node()).getChildren().get(0)).getItems().size()).isEqualTo(3);
        then(((ComboBox) ((StackPane) cmb.node()).getChildren().get(0)).getSelectionModel().isSelected(1)).isTrue();

        mf.items(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(0, 3));
        sf.items(Arrays.asList(1, 2, 3, 4, 5), 3);

        then(((VBox) cb.node()).getChildren().size()).isEqualTo(5);
        then(((CheckBox) ((VBox) cb.node()).getChildren().get(0)).isSelected()).isTrue();

        then(((ListView) lv.node()).getItems().size()).isEqualTo(5);
        then(((ListView) lv.node()).getSelectionModel().isSelected(0)).isTrue();

        then(((VBox) rb.node()).getChildren().size()).isEqualTo(5);
        then(((RadioButton) ((VBox) rb.node()).getChildren().get(3)).isSelected()).isTrue();

        then(((ComboBox) ((StackPane) cmb.node()).getChildren().get(0)).getItems().size()).isEqualTo(5);
        then(((ComboBox) ((StackPane) cmb.node()).getChildren().get(0)).getSelectionModel().isSelected(3)).isTrue();
    }

    @Disabled
    public void styleTest() {
        StringField s = Field.ofStringType("test").styleClass("test");
        SimpleTextControl t = new SimpleTextControl();
        t.setField(s);

        then(t.getStyleClass().size()).isEqualTo(3);

        s.styleClass("hello", "world");
        then(t.getStyleClass().size()).isEqualTo(4);

        s.styleClass("hi", "world");
        then(t.getStyleClass().size()).isEqualTo(4);
        then(t.getStyleClass().get(3)).isEqualTo("world");
    }

}
