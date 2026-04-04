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

import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.formsfx.view.util.VisibilityProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Provides an implementation of a slider control for an {@link IntegerField}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class IntegerSliderControl extends SimpleControl<IntegerField, HBox> {
  public static final int VALUE_LABEL_PADDING = 25;
  /**
   * - slider is the control to change the value.
   */

  protected Slider slider;
  protected Label valueLabel;

  final private int min;
  final private int max;

  /**
   * Constructs a IntegerSliderControl of {@link IntegerSliderControl} type, with visibility condition.
   *
   * @param min minimum slider value
   * @param max maximum slider value
   * @param visibilityProperty property for control visibility of this element
   *
   * @return the constructed IntegerSliderControl
   */
  public static IntegerSliderControl of(int min, int max, VisibilityProperty visibilityProperty) {
    IntegerSliderControl integerSliderControl = new IntegerSliderControl(min, max);

    integerSliderControl.visibilityProperty = visibilityProperty;

    return integerSliderControl;
  }

  /**
   * Creates a slider for integer values.
   *
   * @param min minimum slider value
   * @param max maximum slider value
   */
  public IntegerSliderControl(int min, int max) {
    super();
    this.min = min;
    this.max = max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    fieldLabel = new Label(field.labelProperty().getValue());

    valueLabel = new Label(String.valueOf(field.getValue().intValue()));

    slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);
    slider.setShowTickLabels(false);
    slider.setShowTickMarks(false);
    slider.setValue(field.getValue());

    node = new HBox();
    node.getStyleClass().add("integer-slider-control");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.getChildren().addAll(slider, valueLabel);
    HBox.setHgrow(slider, Priority.ALWAYS);
    valueLabel.setAlignment(Pos.CENTER);
    valueLabel.setMinWidth(VALUE_LABEL_PADDING);
    node.setSpacing(VALUE_LABEL_PADDING);
    HBox.setMargin(valueLabel, new Insets(0, VALUE_LABEL_PADDING, 0, 0));
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
      int sliderValue = Integer.parseInt(field.getUserInput());
      slider.setValue(sliderValue);
      valueLabel.setText(String.valueOf(sliderValue));
    });

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));
    field.tooltipProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));

    slider.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue.intValue()));
    });
  }

}
