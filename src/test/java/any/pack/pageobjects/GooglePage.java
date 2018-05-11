package any.pack.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GooglePage {

    @FindBy(css = "#lst-ib")
    private WebElement searchInput;

    @FindBy(css = "input[name='btnK']")
    private WebElement searchButton;

    @FindBy(css = "div.gstl_0 sbdd_a")
    private WebElement dynamicTextRecommendations;

    public WebElement getSearchInput() {
        return searchInput;
    }

    public WebElement getSearchButton() {
        return searchButton;
    }

    public WebElement getDynamicTextRecommendations() {
        return dynamicTextRecommendations;
    }

    public GooglePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
