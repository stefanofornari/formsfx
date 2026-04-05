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

import com.dlsc.formsfx.model.structure.BooleanField;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * This class provides the base implementation for a simple control to edit
 * boolean values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleBooleanControl extends SimpleControl<BooleanField, VBox> {

    /**
     * checkBox is the editable checkbox to set user input.
     */
    protected CheckBox checkBox;

    public SimpleBooleanControl() {
        getStyleClass().add("simple-boolean-control");
        
        node = new VBox();
        node.getStyleClass().add("simple-boolean-control");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        fieldLabel = new Label(field.labelProperty().getValue());
        checkBox = new CheckBox();
        checkBox.setSelected(field.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        int columns = field.getSpan();
        node.getChildren().add(checkBox);

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

        add(fieldLabel, 0, 0, 2, 1);
        if (labelDescription != null) {
            GridPane.setValignment(labelDescription, VPos.TOP);
            add(labelDescription, 0, 1, 2, 1);
        }
        add(node, 2, 0, columns - 2, 1);
        if (valueDescription != null) {
            GridPane.setValignment(valueDescription, VPos.TOP);
            add(valueDescription, 2, 1, columns - 2, 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        checkBox.disableProperty().bind(field.editableProperty().not());
        fieldLabel.textProperty().bind(field.labelProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();
        field.userInputProperty().addListener((observable, oldValue, newValue) -> checkBox.setSelected(Boolean.parseBoolean(field.getUserInput())));

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));

        checkBox.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        setOnMouseEntered(event -> toggleTooltip(checkBox));
        setOnMouseExited(event -> toggleTooltip(checkBox));

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> field.userInputProperty().setValue(String.valueOf(newValue)));
    }

}
