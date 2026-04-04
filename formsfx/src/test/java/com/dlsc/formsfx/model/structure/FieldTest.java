package com.dlsc.formsfx.model.structure;

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

import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.util.ColSpan;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import javafx.util.StringConverter;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
//@Ignore
public class FieldTest {

    @BeforeAll
    public static void beforeClass() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ex) {
            // JavaFX may only be initialized once.
        }
    }

    @Test
    public void validTest() {
        StringField s = Field.ofStringType("test");

        final int[] changes = {0};

        s.validProperty().addListener((observable, oldValue, newValue) -> changes[0] += 1);

        s.required("This field is required.").validate(StringLengthValidator.atLeast(6, "test"));

        then(s.isValid()).isFalse();

        s.validate(StringLengthValidator.upTo(6, "test"));

        then(changes[0]).isEqualTo(2);
        then(s.isValid()).isTrue();
    }

    @Test
    public void singleSelectionTest() {
        SingleSelectionField<String> s = Field.ofSingleSelectionType(Arrays.asList("Test", "Test 1", "Test 2"), 0);

        then(s.getSelection()).isEqualTo("Test");

        s.select(2);
        then(s.getSelection()).isEqualTo("Test 2");

        s.select(4);
        then(s.getSelection()).isEqualTo("Test 2");

        s.deselect();
        then(s.getSelection()).isNull();

        s.select(2);
        then(s.getSelection()).isEqualTo("Test 2");

        s.select(-1);
        then(s.getSelection()).isNull();
    }

    @Test
    public void multiSelectionTest() {
        MultiSelectionField<String> s = Field.ofMultiSelectionType(Arrays.asList("Test", "Test 1", "Test 2"), Arrays.asList(0, 3));

        then(s.getSelection().size()).isEqualTo(1);

        s.select(2);
        then(s.getSelection().size()).isEqualTo(2);

        s.select(4);
        then(s.getSelection().size()).isEqualTo(2);

        s.deselect(1);
        then(s.getSelection().size()).isEqualTo(2);

        s.deselect(0);
        then(s.getSelection().size()).isEqualTo(1);
    }

    @Test
    public void multilineTest() {
        StringField s = Field.ofStringType("test").multiline(true);

        then(s.isMultiline()).isTrue();

        s.multiline(false);
        then(s.multilineProperty().getValue()).isFalse();
    }

    @Test
    public void dataBindingTest() {
        StringProperty s = new SimpleStringProperty("test");
        DoubleProperty d = new SimpleDoubleProperty(1.0);
        IntegerProperty i = new SimpleIntegerProperty(3);
        BooleanProperty b = new SimpleBooleanProperty(false);

        StringField sf = Field.ofStringType(s);
        DoubleField df = Field.ofDoubleType(d);
        IntegerField inf = Field.ofIntegerType(i);
        BooleanField bf = Field.ofBooleanType(b);

        sf.userInputProperty().setValue("test 2");
        then(sf.persistentValue.getValue()).isEqualTo("test");
        sf.persist();
        then(s.getValue()).isEqualTo("test 2");

        df.userInputProperty().setValue("2");
        then(df.persistentValue.get()).isEqualTo(1.0);
        df.persist();
        then(d.get()).isEqualTo(2.0);

        inf.userInputProperty().setValue("5");
        then(inf.persistentValue.get()).isEqualTo(3);
        inf.persist();
        then(i.get()).isEqualTo(5);

        bf.userInputProperty().setValue("true");
        then(bf.persistentValue.get()).isFalse();
        bf.persist();
        then(b.get()).isTrue();

        s.setValue("test 3");
        then(sf.getValue()).isEqualTo("test 3");
        then(sf.getValue()).isEqualTo("test 3");
    }

    @Test
    public void selectionBindingTest() {
        ObjectProperty<String> o1 = new SimpleObjectProperty<>("hello");
        ListProperty<String> l1 = new SimpleListProperty<>(FXCollections.observableArrayList("hello", "world"));
        ListProperty<String> l2 = new SimpleListProperty<>(FXCollections.observableArrayList("hi", "there"));
        ListProperty<String> l3 = new SimpleListProperty<>(FXCollections.observableArrayList("hi"));

        SingleSelectionField<String> sif = Field.ofSingleSelectionType(l1, o1);
        MultiSelectionField<String> mf = Field.ofMultiSelectionType(l2, l3);

        then(sif.getSelection()).isEqualTo("hello");
        then(mf.getSelection().size()).isEqualTo(1);

        l1.setValue(FXCollections.observableArrayList("oh", "wonder"));

        then(sif.getSelection()).isNull();

        mf.select(1);

        then(mf.getSelection().size()).isEqualTo(2);
        then(mf.persistentSelection.size()).isEqualTo(1);

        mf.persist();

        then(l3.size()).isEqualTo(2);
    }

    @Test
    public void propertiesTest() {
        StringProperty sp = new SimpleStringProperty("test 3");

        StringField s = Field.ofStringType("test")
                .multiline(true)
                .editable(true)
                .format(new StringConverter() {
                    @Override
                    public String toString(final Object o) {
                        return String.valueOf(o);
                    }

                    @Override
                    public Object fromString(final String s) {
                        return s;
                    }
                })
                .styleClass("class", "thing")
                .id("element")
                .placeholder("temp")
                .label("Field")
                .required("error")
                .span(6)
                .span(ColSpan.HALF);

        then(s.getValue()).isEqualTo("test");
        then(s.getSpan()).isEqualTo(6);
        then(s.getStyleClass().size()).isEqualTo(2);
        then(s.getLabel()).isEqualTo("Field");
        then(s.getPlaceholder()).isEqualTo("temp");

        s.bind(sp);

        then(s.persistentValue.getValue()).isEqualTo("test 3");
    }

}
