/*
 * Copyright (c) 2016 seleniumQuery authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.seleniumquery.internal;

import io.github.seleniumquery.SeleniumQueryObject;
import io.github.seleniumquery.by.SeleniumQueryBy;
import io.github.seleniumquery.by.SeleniumQueryInvalidBy;
import io.github.seleniumquery.functions.SeleniumQueryFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * This factory builds {@link io.github.seleniumquery.SeleniumQueryObject}s.
 *
 * The actual implementation is {@link SqObject}s.
 *
 * @author acdcjunior
 * @since 0.9.0
 */
public class SqObjectFactory {

    private static SqObjectFactory instance;

    public static SqObjectFactory instance() {
        if (instance == null) {
            instance = new SqObjectFactory(new SeleniumQueryFunctions());
        }
        return instance;
    }

    public SqObjectFactory(SeleniumQueryFunctions seleniumQueryFunctions) {
        this.seleniumQueryFunctions = seleniumQueryFunctions;
    }

    private final SeleniumQueryFunctions seleniumQueryFunctions;

    public SeleniumQueryObject create(WebDriver driver, By by, List<WebElement> elements, SeleniumQueryObject previous) {
        return create(this.seleniumQueryFunctions, driver, by, elements, previous);
    }

    public SeleniumQueryObject create(SeleniumQueryFunctions seleniumQueryFunctions, WebDriver driver, By by, List<WebElement> elements, SeleniumQueryObject previous) {
        return new SqObject(seleniumQueryFunctions, driver, by, elements, previous);
    }

    /**
     * @deprecated
     * Don't build with this invalid selector. Construct a {@link SeleniumQueryInvalidBy} yourself, with better selector string.
     */
    public SeleniumQueryObject createWithInvalidSelector(WebDriver driver, List<WebElement> elements, SeleniumQueryObject previous) {
        return create(driver, getNoSelectorInvalidBy(), elements, previous);
    }

    public SeleniumQueryObject createWithInvalidSelectorAndNoPrevious(WebDriver driver, List<WebElement> elements) {
        return createWithInvalidSelector(driver, elements, SqObject.NOT_BUILT_BASED_ON_A_PREVIOUS_OBJECT);
    }

    public SeleniumQueryObject createWithInvalidSelectorAndNoPrevious(WebDriver driver, WebElement... elements) {
        return createWithInvalidSelectorAndNoPrevious(driver, asList(elements));
    }

    public SeleniumQueryObject createWithValidSelectorAndNoPrevious(WebDriver driver, String selector) {
        return new SqObject(seleniumQueryFunctions, driver, getBy(selector));
    }

    private By getBy(String selector) {
        return SeleniumQueryBy.byEnhancedSelector(selector);
    }

    private By getNoSelectorInvalidBy() {
        return SeleniumQueryInvalidBy.UNAVAILABLE_BY;
    }

}