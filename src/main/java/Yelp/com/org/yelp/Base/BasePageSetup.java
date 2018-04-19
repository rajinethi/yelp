package Yelp.com.org.yelp.Base;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.google.common.io.Files;

import Yelp.com.org.yelp.Util.Xls_Reader;

public class BasePageSetup {
	private static final StringBuilder REPORT = new StringBuilder();
	private static final String REPORT_FILENAME = "report.txt";
	private static File scrFile;
	private static int numScreenshot;
		
		public static WebDriver driver = null;
		public static Properties CONFIG = null;
		public static Properties OR = null;
		public static Xls_Reader xls1 = new Xls_Reader(System.getProperty("user.dir")+"/src/test/java/xls/TestCases.xls");

		
		public BasePageSetup(){
			if(driver==null){
				//initialize the properties file
				CONFIG = new Properties();
				OR = new Properties();
				try{
					//config
					FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/Yelp/com/org/yelp/Config/Config.properties");
					CONFIG.load(fs);
					
					//OR
					fs = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/Yelp/com/org/yelp/Config/OR.properties");
					OR.load(fs);
				}catch(Exception e){
					//
					System.out.println(e);
					return;
				}
				
				System.out.println("Bowser = " +CONFIG.getProperty("browser"));
				if (CONFIG.getProperty("browser").equals("Mozilla")){
					driver=new FirefoxDriver();
					driver.manage().window().maximize();
					System.out.println("START TESTING\n");
				}
				else if (CONFIG.getProperty("browser").equals("IE")){
					driver=new InternetExplorerDriver();
					driver.manage().window().maximize();
					System.out.println("START TESTING\n");
				}
				else if (CONFIG.getProperty("browser").equals("Chrome")){
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver/chromedriver");
					driver=new ChromeDriver();
					driver.manage().window().maximize();
					System.out.println("START TESTING\n");
				}
			}
			//implicit wait for the whole app
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		
		public void testSite(){
			driver.get(CONFIG.getProperty("testSiteLandingPageURL"));
			log("connect to " + CONFIG.getProperty("testSiteLandingPageURL"));
			try {
				takeScreenshot();
			} catch (IOException exc) {
				fail("Exception while writing the screenshot: " + exc);
			}
		}
		

		
		private void writeTestReport() throws IOException {
			try (FileWriter fw = new FileWriter(Paths.get(REPORT_FILENAME).toFile(), true)) {
				fw.write(LocalDateTime.now() + "\r\n" + REPORT + "-----------------------------------------\r\n");
			}
		}
		public void tearDownAfterClass() {
			try {
				driver.close();		
//				driver.quit();
				writeTestReport();
		
				System.out.println("\nEND TESTING");
			} catch (WebDriverException exc) {
		
				fail("Unexpected end of TESTING: browser lost");
			} catch (IOException exc) {
				fail("Exception while writing the report: " + exc);
			}
		}


	
	public void log(String log) {
		REPORT.append("\r\n").append(log).append("\r\n");
		System.out.println("INFO: " + log);
	}
	
	public void takeScreenshot() throws IOException {
		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); // To take screenshots.
		Files.copy(scrFile, new File(System.getProperty("user.dir") + "/" + numScreenshot++ + ".png"));
	}
	
	public void waitWebPage(int time) throws InterruptedException {
		Thread.sleep(time);
	}
}


