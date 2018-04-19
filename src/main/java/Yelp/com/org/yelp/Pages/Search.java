package Yelp.com.org.yelp.Pages;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import Yelp.com.org.yelp.Base.BasePageSetup;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Search extends BasePageSetup{

	
	
	public void findDropDownMenu(String category, String type) {
		try {
			WebElement dropDownFind = driver.findElement(By.id(OR.getProperty("find")));
			
			
			Assert.assertNotNull(dropDownFind);

			dropDownFind.sendKeys(category);

			log("find "+category);
			takeScreenshot();

			WebElement searchButton = driver.findElement(By.id(OR.getProperty("search_button")));
			Assert.assertNotNull(searchButton);
			searchButton.click(); // Click on search button.

			log("click Search button");
			takeScreenshot();

			dropDownFind = driver.findElement(By.id(OR.getProperty("find")));
			Assert.assertNotNull(dropDownFind);
			dropDownFind.sendKeys(" "+type);

			log("append "+type);
			takeScreenshot();

			searchButton = driver.findElement(By.id(OR.getProperty("search_button")));
			Assert.assertNotNull(searchButton);
			searchButton.click(); // Click on search button.
			waitWebPage(5000);
			List<WebElement> liElements = driver.findElements(By.cssSelector(OR.getProperty("list_search_results")));
			Assert.assertNotNull(liElements);
			String showingResults = driver.findElement(By.cssSelector(OR.getProperty("results_pagination"))).getText();

			log("Total no. of search results: " + showingResults.substring(showingResults.indexOf("of") + 3));
			log("Number of results in the current page: " + liElements.size());
			takeScreenshot();
		} catch (WebDriverException exc) {

			fail("Unexpected end of TESTING: browser lost");
		} catch (InterruptedException exc) {
			fail("Unexpected exception while waiting the program: " + exc);
		} catch (IOException exc) {
			fail("Exception while writing the screenshot: " + exc);
		}
	}

	
	public void parameterizeFilter() {
		try {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			WebElement filter = driver.findElement(By.xpath(OR.getProperty("filter")));

			Assert.assertNotNull(filter);
			filter.click();
			waitWebPage(3000);

			log("expand filter menu");
			takeScreenshot();

			WebDriverWait wait = new WebDriverWait(driver, 30);	

			/* In case of unexpected browser behavior */
			boolean userClick = false;
			do {
				try {
//					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='place'][value='CA:San_Francisco::SoMa']")));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(OR.getProperty("neighborhood"))));
					userClick = true;
				} catch (org.openqa.selenium.TimeoutException exc) {
					System.out.println("Click on 'All Filters'!");
				}
			} while (!userClick);

//			WebElement neigbourhood = driver.findElement(By.cssSelector("input[name='place'][value='CA:San_Francisco::SoMa']"));
			WebElement neigbourhood = driver.findElement(By.cssSelector(OR.getProperty("neighborhood")));

			Assert.assertNotNull(neigbourhood);
			neigbourhood.click();

			log("select neighborhood (Soma)");
			takeScreenshot();

			waitWebPage(8000);

			List<WebElement> prices = driver.findElements(
					By.xpath(OR.getProperty("prices")));
			Assert.assertNotNull(prices);
			Assert.assertTrue(prices.size() == 2);
			prices.get(1).click();

			log("select price ($$)");

			log("apply filter");
			takeScreenshot();

			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			waitWebPage(8000);

			List<WebElement> searchResult = driver.findElements(By.cssSelector(OR.getProperty("list_search_results")));
			Assert.assertNotNull(searchResult);
			String showingResults = driver.findElement(By.cssSelector(OR.getProperty("results_pagination"))).getText();
			log("Total no. of search results: " + showingResults.substring(showingResults.indexOf("of") + 3));
			log("Number of results in the current page: " + searchResult.size());
			takeScreenshot();
		} catch (WebDriverException exc) {

			fail("Unexpected end of TESTING: browser lost");
		} catch (InterruptedException exc) {
			fail("Unexpected exception while waiting the program: " + exc);
		} catch (IOException exc) {
			fail("Exception while writing the screenshot: " + exc);
		}
	}

	
	public void starRating() {
		try {
			List<WebElement> searchResult = driver.findElements(By.cssSelector(OR.getProperty("list_search_results")));
			Assert.assertNotNull(searchResult);
			for (WebElement li: searchResult) {
				WebElement starRating = li.findElement(By.xpath(OR.getProperty("star_rating")));
				Assert.assertNotNull(starRating);
				WebElement title = li.findElement(By.xpath(OR.getProperty("search_result_title")));
				Assert.assertNotNull(title);
				log(title.getText() + " " + starRating.getAttribute("title"));
				takeScreenshot();
			}
		} catch (WebDriverException exc) {
			fail("Unexpected end of TESTING: browser lost");
		} catch (IOException exc) {
			fail("Exception while writing the screenshot: " + exc);
		}
	}

	
	public void expandFirstSearchResult() {
		try {
			waitWebPage(5000);
			List<WebElement> searchResult = driver.findElements(By.cssSelector(OR.getProperty("list_search_results")));
			Assert.assertNotNull(searchResult);
			WebElement firstResult = searchResult.get(0).findElement(By.xpath(OR.getProperty("first_search_result")));
			Assert.assertNotNull(firstResult);
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,500)", "");
			firstResult.click();
			waitWebPage(3000);
			log("Address: " + driver.findElements(By.xpath(OR.getProperty("first_search_result_address"))).get(0).getText());
			log("Phone: " + driver.findElement(By.xpath(OR.getProperty("first_search_result_phone"))).getText());
			log("Website: " + driver.findElement(By.xpath(OR.getProperty("first_search_result_website"))).getText());
			takeScreenshot();

			List<WebElement> reviews = driver.findElements(By.cssSelector(OR.getProperty("first_search_result_reviews")));
			for (int i = 0; i < 3; i ++) {
				log("REVIEW " + (i + 1) + ":\n" + reviews.get(i).findElement(By.xpath(OR.getProperty("reviews"))).getText() + "\n");
			}
			
		} catch (WebDriverException exc) {

			fail("Unexpected end of TESTING: browser lost");
		} catch (InterruptedException exc) {
			fail("Unexpected exception while waiting the program: " + exc);
		} catch (IOException exc) {
			fail("Exception while writing the screenshot: " + exc);
		}
	}

	

} // end of class

