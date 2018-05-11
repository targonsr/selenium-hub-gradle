package any.pack.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class GoogleResultsPage {

    @FindBy(css = "div.g")
    private List<WebElement> resultsList;

    public List<WebElement> getResultsList() {
        return this.resultsList;
    }

    public GoogleResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
