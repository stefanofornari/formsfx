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

import com.dlsc.formsfx.model.structure.StringField;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * string values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleTextControl extends SimpleControl<StringField, StackPane> {

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The editableField allows users to modify the field's value.
     * - The readOnlyLabel displays the field's value if it is not editable.
     */
    protected TextField editableField;
    protected TextArea editableArea;
    protected Label readOnlyLabel;

    /**
     * Creates a new SimpleTextControl with the default label span of 2.
     */
    public SimpleTextControl() {
        this(2);
    }

    /**
     * Creates a new SimpleTextControl with the specified label span.
     *
     * @param labelSpan the number of columns the label should span
     */
    public SimpleTextControl(int labelSpan) {
        super(labelSpan);
        getStyleClass().add("simple-text-control");

        node = new StackPane();
        node.getStyleClass().add("simple-text-control");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        ObservableList<String> fieldStyleClass = field.getStyleClass();
        node.getStyleClass().addAll(fieldStyleClass);

        editableField = new TextField(field.getValue()); editableField.getStyleClass().addAll(fieldStyleClass);
        editableArea = new TextArea(field.getValue()); editableArea.getStyleClass().addAll(fieldStyleClass);

        readOnlyLabel = new Label(field.getValue()); readOnlyLabel.getStyleClass().addAll(fieldStyleClass);
        fieldLabel = new Label(field.labelProperty().getValue()); fieldLabel.getStyleClass().addAll(fieldStyleClass);
        editableField.setPromptText(field.placeholderProperty().getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        readOnlyLabel.getStyleClass().add("read-only-label");

        readOnlyLabel.setPrefHeight(26);

        editableArea.getStyleClass().add("simple-textarea");
        editableArea.getStyleClass().addAll(field.getStyleClass());
        editableArea.setPrefRowCount(5);
        editableArea.setPrefHeight(80);
        editableArea.setWrapText(true);

        node.getChildren().addAll(editableField, editableArea, readOnlyLabel);

        node.setAlignment(Pos.CENTER_LEFT);

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

        int columns = field.getSpan();

        if (columns < 3) {
            int rowIndex = 0;
            add(fieldLabel, 0, rowIndex++, columns, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, rowIndex++, columns, 1);
            }
            add(node, 0, rowIndex++, columns, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, 0, rowIndex, columns, 1);
            }
        } else {
            add(fieldLabel, 0, 0, labelSpan, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, 1, labelSpan, 1);
            }
            add(node, labelSpan, 0, columns - labelSpan, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, labelSpan, 1, columns - labelSpan, 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        editableArea.visibleProperty().bind(Bindings.and(field.editableProperty(),
                                                        field.multilineProperty()));
        editableField.visibleProperty().bind(Bindings.and(field.editableProperty(),
                                                        field.multilineProperty().not()));
        readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

        editableField.textProperty().bindBidirectional(field.userInputProperty());
        editableArea.textProperty().bindBidirectional(field.userInputProperty());
        readOnlyLabel.textProperty().bind(field.userInputProperty());
        fieldLabel.textProperty().bind(field.labelProperty());
        editableField.promptTextProperty().bind(field.placeholderProperty());
        editableArea.promptTextProperty().bind(field.placeholderProperty());

        editableArea.managedProperty().bind(editableArea.visibleProperty());
        editableField.managedProperty().bind(editableField.visibleProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.multilineProperty().addListener((observable, oldValue, newValue) -> {
            node.setPrefHeight(newValue ? 80 : 0);
            readOnlyLabel.setPrefHeight(newValue ? 80 : 26);
        });
    }

}
