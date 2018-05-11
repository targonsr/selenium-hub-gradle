package any.pack.listeners;

import any.pack.TestBase;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenShotListener extends TestBase implements ITestListener {

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
    }

    public void onTestSkipped(ITestResult result) {
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        WebDriver webDriver = ((TestBase) currentClass).driver;

        getScreenshot(result.getName(), webDriver);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Object currentClass = result.getInstance();
        WebDriver webDriver = ((TestBase) currentClass).driver;

        getScreenshot(result.getName(), webDriver);
    }
}
