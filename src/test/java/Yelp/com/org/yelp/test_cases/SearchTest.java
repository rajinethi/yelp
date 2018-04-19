package Yelp.com.org.yelp.test_cases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Yelp.com.org.yelp.Base.BasePageSetup;
import Yelp.com.org.yelp.Pages.Search;
import Yelp.com.org.yelp.Util.TestUtil;

import java.util.Hashtable;

public class SearchTest extends BasePageSetup {
	Search s = new Search();

	@BeforeClass
	public void openBrowser() {
		testSite();
	 }
	
	@AfterClass
	public void tearDown(){
		tearDownAfterClass();
	}
	
	@Test (dataProvider="getData")
	public void searchPageTest(Hashtable<String, String> data){

		s.findDropDownMenu(data.get("category"),data.get("type"));
		s.parameterizeFilter();
		s.starRating();
		s.expandFirstSearchResult();
		driver.navigate().to(CONFIG.getProperty("testSiteLandingPageURL"));
	}
	
	@DataProvider
	public Object[][] getData(){
		return TestUtil.getData("SearchTest", BasePageSetup.xls1);
	}
	
	
}
