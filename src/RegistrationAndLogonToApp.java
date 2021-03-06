import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationAndLogonToApp{

    WebDriver driver = new ChromeDriver();
    WebDriverWait w = new WebDriverWait(driver, 5);
    private final static String EMAIL_URL = "email url";
    private final static String AFTER_REGISTRATION_TEXT = "Konto zostało utworzone\n" +
            "Za chwilę na podany adres e-mail otrzymasz wiadomość z linkiem aktywacyjnym. Kliknij w niego aby móc korzystać z konta.\n";
    private final static String AFTER_ACTIVATION_TEXT = "×\nTwoje konto zostało zaktywowane, możesz się teraz zalogować.";
    private final static String AFTER_LOGOUT_TEXT = "×\nZostałeś(-aś) wylogowany(-a).";


    @Test
    public void participantRegistration() {

        final String name = "Xxxxxxxxx";
        final String surname = "Xxxxx";
        final String email = "Xxxxxxxx@xxxx.com";
        final String password = "XxxxxX";
        final String mainViewHeaderText = "Xxxx, xxxxxxx,xxxxxx";

        driver.get("application URL");

        w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/register']")));
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        driver.findElement(By.cssSelector("a[href='/registerUser/participant']")).click();
        driver.findElement(By.cssSelector("input#fname")).sendKeys(name);
        driver.findElement(By.cssSelector("input#surname")).sendKeys(surname);
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("input#password_two")).sendKeys(password);
        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();
        driver.findElement(By.cssSelector("input#wantsToReceiveInfoEmail")).click();
        driver.findElement(By.cssSelector("input#gdprMessage")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertRegistrationInfo();
        logToMailAndClickVerificationLink(email, password);
        switchToTab(2); //registration link opens in new background tab
        assertAccountActivationInfo();
        logToApplication(email, password);
        assertBeingLogged(mainViewHeaderText, name, surname);
        driver.findElement(By.cssSelector("a[href='/logout']")).click();
        assertSuccessfulLogout();
    }

    @Test
    public void leaderRegistration() {

        final String name = "Xxxxx";
        final String surname = "Xxxxxx";
        final String email = "XxxxxX@xxxx.com";
        final String password = "XxxXXxxx";
        final String contactInfo = "Xxxxxx, xxxxx, xxxx";
        final String mainViewHeaderText = "Xxxx, xxxxxxx, xxxxxx";

        driver.get("application URL");

        w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/register']")));
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        driver.findElement(By.cssSelector("a[href='/registerUser/leader']")).click();
        driver.findElement(By.cssSelector("input#fname")).sendKeys(name);
        driver.findElement(By.cssSelector("input#surname")).sendKeys(surname);
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("input#password_two")).sendKeys(password);
        driver.findElement(By.cssSelector("input#churchStatus1")).click();
        driver.findElement(By.cssSelector("textarea#contactInfo")).sendKeys(contactInfo);
        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();
        driver.findElement(By.cssSelector("input#wantsToReceiveInfoEmail")).click();
        driver.findElement(By.cssSelector("input#gdprMessage")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertRegistrationInfo();
        logToMailAndClickVerificationLink(email, password);
        switchToTab(2); //registration link opens in new background tab
        assertAccountActivationInfo();
        logToApplication(email, password);
        assertBeingLogged(mainViewHeaderText, name, surname);
        driver.findElement(By.cssSelector("a[href='/logout']")).click();
        assertSuccessfulLogout();
    }



    public void logToMailAndClickVerificationLink(String emailAddress, String password) {

        driver.get(EMAIL_URL);
        w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#username")));
        driver.findElement(By.cssSelector("input#username")).sendKeys(emailAddress);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("button#login_btn")).click();
        w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[class='row top']")));
        driver.findElement(By.cssSelector("div[class='row top']")).click();
        w.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'register')]")));
        driver.findElement(By.xpath("//a[contains(text(),'register')]")).click();
        // after clicking we delete message
        driver.findElement(By.cssSelector("button.pm_buttons-child.fa.fa-trash-o.toolbar-btn-trash.moveElement-btn-trash")).click();
        driver.findElement(By.xpath("//span[text()='Kosz']")).click();
        driver.findElement(By.cssSelector("div[class='row top']")).click();
        driver.findElement(By.cssSelector("button[data-action='delete']")).click();
        w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#confirmModalBtn")));
        driver.findElement(By.cssSelector("button#confirmModalBtn")).click();
    }


    public void switchToTab(int tabNumber) {

        w.until(ExpectedConditions.numberOfWindowsToBe(tabNumber));
        Set<String> ids = driver.getWindowHandles();
        Iterator<String> it = ids.iterator();
        for (int i = 0; i < tabNumber - 1; i++) {
            it.next();
        }
        driver.switchTo().window(it.next());
    }

    public void logToApplication(String email, String password) {

        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void assertBeingLogged(String mainViewHeaderText, String name, String surname) {

        Assertions.assertTrue(driver.findElement(By.cssSelector("h2[class='text-primary']")).isDisplayed());
        Assertions.assertEquals(mainViewHeaderText, driver.findElement(By.cssSelector("h2[class='text-primary']")).getText());
        Assertions.assertTrue(driver.findElement(By.cssSelector("span[class='my-auto']")).isDisplayed());
        Assertions.assertTrue(Pattern.matches("(.)*" + name + " " + surname, driver.findElement(By.cssSelector("span[class='my-auto']")).getText()));
    }

    public void assertSuccessfulLogout() {

        Assertions.assertTrue(driver.findElement(By.cssSelector("div.alert.alert-success.alert-dismissible")).isDisplayed());
        Assertions.assertEquals(AFTER_LOGOUT_TEXT, driver.findElement(By.cssSelector("div.alert.alert-success.alert-dismissible")).getText());
    }

    public void assertAccountActivationInfo() {

        Assertions.assertTrue(driver.findElement(By.cssSelector("div[class='alert alert-success alert-dismissible']")).isDisplayed());
        Assertions.assertEquals(AFTER_ACTIVATION_TEXT, driver.findElement(By.cssSelector("div[class='alert alert-success alert-dismissible']")).getText());
    }

    public void assertRegistrationInfo() {

        Assertions.assertTrue(driver.findElement(By.cssSelector("div[class='card-body']")).isDisplayed());
        Assertions.assertEquals(AFTER_REGISTRATION_TEXT, driver.findElement(By.cssSelector("div[class='card-body']")).getText());
    }
}