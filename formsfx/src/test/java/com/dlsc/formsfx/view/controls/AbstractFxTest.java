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

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;

public abstract class AbstractFxTest {

    protected javafx.stage.Stage stage;
    protected BorderPane root;
    protected Form form;

    // This method is called by the concrete test's @Start method
    public void start(javafx.stage.Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();

        init(stage, root);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create a simple form for a single field and set it as the root center via FormRenderer.
     */
    protected void createForm(Field<?> field) {
        this.form = Form.of(
            Group.of(field)
        ).title("Test");

        root.setCenter(new FormRenderer(form));
    }

    /**
     * Subclasses should initialize the UI by adding nodes to the provided root
     */
    protected abstract void init(javafx.stage.Stage stage, BorderPane root);
}
