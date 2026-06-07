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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * 
 */
public class IntegerValidatorTest {

    @Test
    public void valid_integer_strings() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer");

        then(validator.validate("123").getResult()).isTrue();
        then(validator.validate("-456").getResult()).isTrue();
        then(validator.validate("0").getResult()).isTrue();
        then(validator.validate("+789").getResult()).isTrue();
    }

    @Test
    public void invalid_integer_strings() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer");

        then(validator.validate("abc").getResult()).isFalse();
        then(validator.validate("12.34").getResult()).isFalse();
        then(validator.validate("1 2 3").getResult()).isFalse();
        then(validator.validate("").getResult()).isFalse();
        then(validator.validate("12a34").getResult()).isFalse();
    }

    @Test
    public void error_message() {
        String errorMessage = "Please enter a valid integer";
        IntegerValidator validator = new IntegerValidator(errorMessage);

        ValidationResult result = validator.validate("abc");
        then(result.getResult()).isFalse();
        then(result.getErrorMessage()).isEqualTo(errorMessage);
    }

    @Test
    public void null_input() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer");

        then(validator.validate(null).getResult()).isFalse();
    }

    @Test
    public void null_input_allowed() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer", true);

        then(validator.validate(null).getResult()).isTrue();
    }

    @Test
    public void empty_input_allowed() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer", true);

        then(validator.validate("").getResult()).isTrue();
    }

    @Test
    public void valid_integer_with_allow_empty() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer", true);

        then(validator.validate("123").getResult()).isTrue();
    }

    @Test
    public void invalid_integer_with_allow_empty() {
        IntegerValidator validator = new IntegerValidator("Not a valid integer", true);

        then(validator.validate("abc").getResult()).isFalse();
    }

}
