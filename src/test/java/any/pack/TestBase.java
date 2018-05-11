package any.pack;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;

public class TestBase {
    public WebDriver driver;
    private WebDriverWait wait;
    public static Properties properties;
    private String browser, url;
    private DesiredCapabilities caps = new DesiredCapabilities();
    private final String HUB_URL = "http://hub_latest:4444/wd/hub";

    @BeforeSuite
    public void runBrowser() {
        loadProperties();
        init(browser, url);
    }

    @AfterSuite
    public void quitBrowser() {
        driver.quit();
    }

    private void init(String browser, String url) {
        startBrowser(browser);
        this.wait = new WebDriverWait(driver, 10, 500);
        getUrl(url);
    }

    private void startBrowser(String browser) {
        if (browser.equalsIgnoreCase("firefox")) {
            caps.setCapability("acceptInsecureCerts", true);
            FirefoxOptions options = new FirefoxOptions().addPreference("intl.accept_languages", "en").merge(caps);
            driver = new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("remoteFirefox")) {
            caps.setCapability("acceptSslCerts", true);
            caps.setBrowserName(FIREFOX);
            caps.setAcceptInsecureCerts(true);
            caps.setCapability("resolution", "1920x1080");
            try {
                URL serverUrl = new URL(HUB_URL);
                driver = new RemoteWebDriver(serverUrl, caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--lang=en");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("remoteChrome")) {
            caps.setCapability("acceptSslCerts", true);
            caps.setBrowserName(CHROME);
            caps.setAcceptInsecureCerts(true);
            caps.setCapability("resolution", "1920x1080");
            caps.setCapability("browser", "Chrome");
            try {
                URL serverUrl = new URL(HUB_URL);
                driver = new RemoteWebDriver(serverUrl, caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUrl(String url) {
        driver.manage().deleteAllCookies();
        driver.get(url);
    }

    private void loadProperties() {
        this.browser = System.getProperty("driverName");
        properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("./src/test/resources/" + System.getProperty("env") + ".properties");
            properties.load(file);
        } catch (FileNotFoundException e1) {
            System.err.println("ERROR: Properties file not found!");
        } catch (IOException e2) {
            System.err.println("ERROR: Loading properties failed!");
        }
        this.url = properties.getProperty("url");
    }

    protected void waitForVisibilityOf(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToBeClickable(WebElement element) {
        try {
            Thread.sleep(500);
        } catch (Throwable e) {
        }
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForDisappearanceOf(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void getScreenshot(String name, WebDriver webDriver) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

        String reportDirectory = System.getProperty("reportDir") + "/" + System.getProperty("driverName") + "/Screenshots/";

        try {
            File destFile = new File(
                    (String) reportDirectory + name + "_" + format.format(calendar.getTime()) + ".png");
            FileUtils.copyFile(scrFile, destFile);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
