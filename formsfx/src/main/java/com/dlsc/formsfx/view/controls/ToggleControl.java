package com.dlsc.formsfx.view.controls;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2019 - 2026 DLSC Software & Consulting GmbH
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
import com.dlsc.formsfx.view.util.VisibilityProperty;
import javafx.scene.control.Label;
import org.controlsfx.control.ToggleSwitch;

/**
 * Displays a control for boolean values with a toggle from ControlsFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToggleControl extends SimpleControl<BooleanField, ToggleSwitch> {
  public static final double NEGATIVE_LABEL_INSETS = -17.3;

  /**
   * Constructs a ToggleControl of {@link ToggleControl} type, with visibility condition.
   *
   * @param visibilityProperty property for control visibility of this element
   *
   * @return the constructed ToggleControl
   */
  public static ToggleControl of(VisibilityProperty visibilityProperty) {
    ToggleControl toggleControl = new ToggleControl();

    toggleControl.visibilityProperty = visibilityProperty;

    return toggleControl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    fieldLabel = new Label(field.labelProperty().getValue());

    node = new ToggleSwitch();
    node.getStyleClass().add("toggle-control");
    // is necessary to offset the control to the left, because we don't use the provided label
    node.setTranslateX(NEGATIVE_LABEL_INSETS);
    node.setSelected(field.getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();
    field.userInputProperty().addListener((observable, oldValue, newValue) -> {
      node.setSelected(Boolean.parseBoolean(field.getUserInput()));
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    node.selectedProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue));
    });
  }

}
