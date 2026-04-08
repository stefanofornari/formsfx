package com.dlsc.formsfx.demo;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This is the main class to start the application.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SimpleDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Form form = Form.of(
            Group.of(
                Field.ofStringType("A simple text field")
                    .label("A simple text field")
                    .placeholder("placeholder")
                    .required("This field is required")
                    .tooltip(
                        "This is a simple text field with long tooltip, " +
                        "which is repeated twice: " +
                        "this is a simple text field with long tooltip."
                    )
            )
        ).title("Simple Demo");

        final BorderPane root = new BorderPane();
        root.setCenter(new FormRenderer(form));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
