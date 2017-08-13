/*
 * Copyright (c) 2017 seleniumQuery authors
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

package io.github.seleniumquery.by.secondgen.csstree.condition.pseudoclass.childfilter;

import org.openqa.selenium.WebDriver;

import io.github.seleniumquery.by.secondgen.csstree.condition.pseudoclass.AstCssPseudoClassConditionVisitor;
import io.github.seleniumquery.by.secondgen.csstree.condition.pseudoclass.CssFunctionalPseudoClassCondition;
import io.github.seleniumquery.by.secondgen.csstree.condition.pseudoclass.finderfactorystrategy.MaybeNativelySupportedPseudoClass;
import io.github.seleniumquery.by.secondgen.finder.CssFinder;
import io.github.seleniumquery.by.secondgen.finder.XPathAndFilterFinder;

/**
 * :nth-last-child()
 * https://api.jquery.com/nth-last-child-selector/
 * https://developer.mozilla.org/pt-BR/docs/Web/CSS/:nth-last-child
 *
 * @author acdcjunior
 * @since 0.10.0
 */
public class CssNthLastChildPseudoClass extends CssFunctionalPseudoClassCondition implements MaybeNativelySupportedPseudoClass {

    public static final String PSEUDO = "nth-last-child";

    public CssNthLastChildPseudoClass(String pseudoClassArgument) {
        super(pseudoClassArgument);
    }

    @Override
    public void accept(AstCssPseudoClassConditionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String pseudoClassForCSSNativeSupportCheck(WebDriver webDriver) {
        return ":"+PSEUDO+"(1)";
    }

    @Override
    public CssFinder toCssWhenNativelySupported(WebDriver webDriver) {
        NthArgument nthArgument = getNthChildArgument();
        return new CssFinder(":"+PSEUDO+"("+nthArgument.toCSS()+")");
    }

    @Override
    public XPathAndFilterFinder toXPath(WebDriver webDriver) {
        NthArgument nthArgument = getNthChildArgument();
        return XPathAndFilterFinder.pureXPath(nthArgument.toXPath("(last()+1-position())"));
    }

    private NthArgument getNthChildArgument() {
        return new NthArgument(getArgument().getArgumentAsString());
    }

}
