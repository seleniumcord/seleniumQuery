package io.github.seleniumquery.wait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.github.seleniumquery.SeleniumQueryConfig;
import io.github.seleniumquery.SQLocalFactory;
import io.github.seleniumquery.SeleniumQueryObject;
import io.github.seleniumquery.by.SeleniumQueryBy;
import io.github.seleniumquery.wait.evaluators.Evaluator;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

/**
 * @author acdcjunior
 * @since 0.1.0
 */
public class SeleniumQueryFluentWait {

	/**
	 * @author acdcjunior
	 * @since 0.1.0
	 */
	private static <T> T fluentWait(SeleniumQueryObject seleniumQueryObject, Function<By, T> function, String reason) {
		try {
			return new FluentWait<By>(seleniumQueryObject.getBy())
					.withTimeout(SeleniumQueryConfig.getWaitTimeoutInSeconds(), TimeUnit.SECONDS)
					.pollingEvery(SeleniumQueryConfig.getWaitPollingInMillisseconds(), TimeUnit.MILLISECONDS)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(NoSuchElementException.class)
					.until(function);
		} catch (TimeoutException sourceException) {
			throw new SeleniumQueryWaitException(sourceException, seleniumQueryObject, reason);
		}
	}
	
	public static interface WaitMethod {
		<T> SeleniumQueryObject evaluateUntil(Evaluator<T> evaluator, T value, SeleniumQueryObject seleniumQueryObject, boolean negated);
	}
	
	/**
	 * @author acdcjunior
	 * @since 0.5.0
	 */
	public static WaitMethod QUERY_UNTIL = new WaitMethod() {
		@Override
		public <T> SeleniumQueryObject evaluateUntil(final Evaluator<T> evaluator, final T value, SeleniumQueryObject seleniumQueryObject
				, final boolean negated) {
			final WebDriver driver = seleniumQueryObject.getWebDriver();
			final SeleniumQueryBy by = seleniumQueryObject.getBy();
			List<WebElement> elements = fluentWait(seleniumQueryObject, new Function<By, List<WebElement>>() {
				@Override
				public List<WebElement> apply(By selector) {
					List<WebElement> elements = driver.findElements(by);
					final boolean evaluate = evaluator.evaluate(driver, elements, value);
					if (!negated) {
						if (!evaluate) {
							return null;
						}
					} else {
						if (evaluate) {
							return null;
						}
					}
					return elements;
				}
			}, "to "+evaluator.stringFor(value));
			return SQLocalFactory.getInstance().createWithInvalidSelector(seleniumQueryObject.getWebDriver(), elements, seleniumQueryObject);
		}
	};
	
	/**
	 * @author acdcjunior
	 * @since 0.5.0
	 */
	public static WaitMethod WAIT_UNTIL = new WaitMethod() {
		@Override
		public <T> SeleniumQueryObject evaluateUntil(final Evaluator<T> evaluator, final T value, SeleniumQueryObject seleniumQueryObject
				, final boolean negated) {
			final WebDriver driver = seleniumQueryObject.getWebDriver();
			final SeleniumQueryBy by = seleniumQueryObject.getBy();
			SeleniumQueryFluentWait.fluentWait(seleniumQueryObject, new Function<By, Boolean>() {
				@Override
				public Boolean apply(By selector) {
					final boolean evaluate = evaluator.evaluate(driver, driver.findElements(by), value);
					if (negated) {
						return !evaluate;
					}
					return evaluate;
				}
			}, "to "+evaluator.stringFor(value));
			return seleniumQueryObject;
		}
	};

}