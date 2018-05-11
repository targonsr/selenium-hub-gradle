package any.pack.tests;

import any.pack.TestBase;
import any.pack.pageobjects.GooglePage;
import any.pack.pageobjects.GoogleResultsPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTest extends TestBase {
    GooglePage googlePage;

    @Test
    public void findMeOnGoogle() {
        googlePage = new GooglePage(driver);
        waitForVisibilityOf(googlePage.getSearchInput());
        googlePage.getSearchInput().sendKeys("\"" + properties.getProperty("name") + "\"");
        waitForElementToBeClickable(googlePage.getSearchButton());
        googlePage.getSearchButton().click();
        GoogleResultsPage googleResultsPage = new GoogleResultsPage(driver);
        assertThat(googleResultsPage.getResultsList().size()).isGreaterThan(1);
        assertThat(googleResultsPage.getResultsList()).allSatisfy(element ->
                assertThat(element.getText().toLowerCase()).contains(properties.getProperty("name"))
        );
    }
}
