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
package com.dlsc.formsfx.model.validators;

/**
 * A IntegerValidator checks if a string can be parsed as a valid integer.
 *
 */
public class IntegerValidator extends CustomValidator<String> {

    /**
     * Creates an IntegerValidator that checks if the input string is a valid integer.
     *
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     */
    public IntegerValidator(String errorMessage) {
        this(errorMessage, false);
    }

    /**
     * Creates an IntegerValidator that checks if the input string is a valid integer.
     *
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     * @param allowEmpty
     *              Whether to allow empty or null values as valid.
     */
    public IntegerValidator(String errorMessage, boolean allowEmpty) {
        super(input -> {
            if (input == null || input.isEmpty()) {
                return allowEmpty;
            }

            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, errorMessage);
    }

}
