package com.dlsc.formsfx.view.controls;
/* -
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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.util.VisibilityProperty;

/**
 * This class provides the base implementation for a simple control to select or enter a directory
 * path.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 * @author Arvid Nyström
 */
public class SimpleChooserControl extends SimpleControl<StringField, StackPane> {

  /**
   * - The fieldLabel is the container that displays the label property of the field. - The
   * editableField allows users to modify the field's value. - The readOnlyLabel displays the
   * field's value if it is not editable.
   */
  protected TextField editableField;
  protected TextArea editableArea;
  protected Label readOnlyLabel;

  final private Button chooserButton = new Button();
  final private HBox contentBox = new HBox();
  final private String buttonText;
  final private File initialDirectory;
  final private boolean directory;

  /**
   * Constructs a SimpleChooserControl of {@link SimpleChooserControl} type, with visibility condition.
   *
   * @param visibilityProperty property for control visibility of this element
   *
   * @return the constructed SimpleChooserControl
   */
  public static SimpleChooserControl of(String buttonText,
                                        File initialDirectory,
                                        boolean directory,
                                        VisibilityProperty visibilityProperty) {
    SimpleChooserControl simpleChooserControl = new SimpleChooserControl(buttonText, initialDirectory, directory);

    simpleChooserControl.visibilityProperty = visibilityProperty;

    return simpleChooserControl;
  }

  /**
   * Create a new SimpleChooserControl.
   */
  public SimpleChooserControl() {
    this("Browse", null, false);
  }

  /**
   * Create a new SimpleChooserControl.
   *
   * @param labelSpan the number of columns the label should span
   */
  public SimpleChooserControl(int labelSpan) {
    this("Browse", null, false, labelSpan);
  }

  /**
   * Create a new SimpleChooserControl.
   *
   * @param buttonText       Text for the button to show, e.g. "Browse"
   * @param initialDirectory An optional initial path, can be null.
   *                         If null, will use the path from the previously
   *                         chosen file if present.
   * @param directory        true, if only directories are allowed
   */
  public SimpleChooserControl(String buttonText,
                              File initialDirectory,
                              boolean directory) {
    this(buttonText, initialDirectory, directory, 2);
  }

  /**
   * Create a new SimpleChooserControl.
   *
   * @param buttonText       Text for the button to show, e.g. "Browse"
   * @param initialDirectory An optional initial path, can be null.
   *                         If null, will use the path from the previously
   *                         chosen file if present.
   * @param directory        true, if only directories are allowed
   * @param labelSpan        the number of columns the label should span
   */
  public SimpleChooserControl(String buttonText,
                              File initialDirectory,
                              boolean directory,
                              int labelSpan) {
    super(labelSpan);
    getStyleClass().add("simple-chooser-control");
    node = new StackPane();
    node.getStyleClass().add("simple-chooser-control");

    this.buttonText = buttonText;
    this.initialDirectory = initialDirectory;
    this.directory = directory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    editableField = new TextField(field.getValue());
    editableArea = new TextArea(field.getValue());

    readOnlyLabel = new Label(field.getValue());
    fieldLabel = new Label(field.labelProperty().getValue());
    editableField.setPromptText(field.placeholderProperty().getValue());

    if (field.valueProperty().get().equals("null")) {
      field.valueProperty().set("");
    }

    chooserButton.setOnAction(event -> {
      File currentInitialDirectory = initialDirectory;
      boolean fileChosen = !field.valueProperty().get().trim().isEmpty();
      if (initialDirectory == null && fileChosen) {
        // define previously chosen path as initial directory
        String previousPath = field.valueProperty().get();
        // initial directory must be a folder
        if (!new File(previousPath).isDirectory()) {
          Path path = Paths.get(previousPath);
          previousPath = path.getParent().toAbsolutePath().toString();
        }
        currentInitialDirectory = new File(previousPath);
      }

      File chosen;

      if (directory) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(currentInitialDirectory);
        chosen = directoryChooser.showDialog(node().getScene().getWindow());
      } else {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(currentInitialDirectory);
        chosen = fileChooser.showOpenDialog(node().getScene().getWindow());
      }

      if (chosen != null) {
        editableField.setText(chosen.getAbsolutePath());
      }
    });

    chooserButton.setText(buttonText);

    StackPane fieldStackPane = new StackPane();
    fieldStackPane.getChildren().addAll(editableField, editableArea, readOnlyLabel);
    fieldStackPane.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(fieldStackPane, Priority.ALWAYS);

    contentBox.getChildren().addAll(fieldStackPane, chooserButton);
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
    editableArea.setPrefRowCount(5);
    editableArea.setPrefHeight(80);
    editableArea.setWrapText(true);

    if (field.isMultiline()) {
      node.setPrefHeight(80);
      readOnlyLabel.setPrefHeight(80);
    }

    node.getChildren().add(contentBox);

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

    field.errorMessagesProperty().addListener((observable, oldValue, newValue) ->
        toggleTooltip(field.isMultiline() ? editableArea : editableField)
    );

    editableField.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableField)
    );
    editableArea.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableArea)
    );
  }

}