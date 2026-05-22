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

import com.dlsc.formsfx.model.structure.SingleSelectionField;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * combobox values.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SimpleComboBoxControl<V> extends SimpleControl<SingleSelectionField<V>, StackPane> {

    /**
     * - The comboBox is the container that displays the values in the
     *   ComboBox.
     * - The readOnlyLabel is used to show the current selection in read only.
     */
    protected ComboBox<V> comboBox;
    protected Label readOnlyLabel;

    /**
     * Creates a new SimpleComboBoxControl with the default label span of 2.
     */
    public SimpleComboBoxControl() {
        this(2);
    }

    /**
     * Creates a new SimpleComboBoxControl with the specified label span.
     *
     * @param labelSpan the number of columns the label should span
     */
    public SimpleComboBoxControl(int labelSpan) {
        super(labelSpan);
        getStyleClass().add("simple-select-control");

        node = new StackPane();
        node.getStyleClass().add("simple-select-control");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        fieldLabel = new Label(field.labelProperty().getValue());
        readOnlyLabel = new Label();

        comboBox = new ComboBox<>(field.getItems());

        // If the field already has a selection object, show it (even if it's not in the list).
        if (field.getSelection() != null) {
            comboBox.setValue(field.getSelection());
        } else if (!field.getItems().isEmpty()) {
            // If no selection provided, set model selection to first item so control and field stay in sync
            field.select(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        int columns = field.getSpan();
        readOnlyLabel.getStyleClass().add("read-only-label");

        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setVisibleRowCount(4);

        node.setAlignment(Pos.CENTER_LEFT);
        node.getChildren().addAll(comboBox, readOnlyLabel);

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

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

        fieldLabel.textProperty().bind(field.labelProperty());
        comboBox.visibleProperty().bind(field.editableProperty());
        // Make the underlying ComboBox editable when the field.freeText is true
        if (field instanceof com.dlsc.formsfx.model.structure.SingleSelectionField) {
            // bind to freeText property on single-selection fields
            comboBox.editableProperty().bind(((com.dlsc.formsfx.model.structure.SingleSelectionField<V>) field).freeTextProperty());
        } else {
            comboBox.editableProperty().bind(field.editableProperty());
        }
        readOnlyLabel.visibleProperty().bind(field.editableProperty().not());
        readOnlyLabel.textProperty().bind(comboBox.valueProperty().asString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.itemsProperty().addListener((observable, oldValue, newValue) -> comboBox.setItems(field.getItems()));

        // Also listen for content changes on the underlying list (e.g. setAll)
        field.getItems().addListener((ListChangeListener<? super V>) change -> comboBox.setItems(field.getItems()));

        field.selectionProperty().addListener((observable, oldValue, newValue) -> {
            if (field.getSelection() != null) {
                comboBox.getSelectionModel().select(field.getItems().indexOf(field.getSelection()));
            } else {
                comboBox.getSelectionModel().clearSelection();
            }
        });

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(comboBox));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(comboBox));
        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(comboBox));

        // Ensure control reflects current field selection at initialization (covers when selection was set before listener attachment)
        if (field.getSelection() != null) {
            if (field.getItems().contains(field.getSelection())) {
                comboBox.getSelectionModel().select(field.getSelection());
            } else {
                comboBox.setValue(field.getSelection());
            }
        } else if (!field.getItems().isEmpty()) {
            // If field has no selection, ensure combo shows first item
            comboBox.getSelectionModel().select(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        comboBox.setOnMouseEntered(event -> toggleTooltip(comboBox));
        comboBox.setOnMouseExited(event -> toggleTooltip(comboBox));

        // Commit editor text on action (e.g., Enter key)
        comboBox.getEditor().setOnAction(event -> {
            if (comboBox.isEditable()) {
                @SuppressWarnings("unchecked")
                V typed = (V) comboBox.getEditor().getText();
                field.selectionProperty().setValue(typed);
            }
        });

        // Bind combo value to field selection so both stay in sync (covers initial value and updates)
        comboBox.valueProperty().bindBidirectional(field.selectionProperty());

    }

}
