import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationValidation {

    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, 5);

    private final static String AFTER_REGISTRATION_TEXT = "Konto zostało utworzone\n" +
            "Za chwilę na podany przy rejestracji adres e-mail otrzymasz wiadomość z linkiem aktywacyjnym. Kliknij w niego aby móc korzystać z konta.\n";

    private final static String NOT_MEET_REQUIREMENTS_ALERT = "×\nPodane dane nie spełniają wymaganych kryteriów.";
    private final static String EMAIL_REGISTERED_BEFORE_ALERT = "Ten adres email jest już zarejestrowany w systemie. Jeśli zapomniałeś hasło - możesz utworzyć nowe - kliknij tutaj.";

    @Test
    public void participantRegistrationValidation() {

        final String name = "Xxxx";
        final String surname = "Xxxxxxxx";
        final String email = "xxxxxxx@xxxxxxx";
        final String emailRegisteredBefore = "xxxxx@xxxxxxxx";
        final String password = "XxxXxxXxx";
        final String registrationHeaderText = "Rejestracja Uczestnika";

        driver.get("application URL");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/register']")));
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        driver.findElement(By.cssSelector("a[href='/registerUser/participant']")).click();

        String initialUrl = driver.getCurrentUrl();
        Assertions.assertEquals(registrationHeaderText, driver.findElement(By.cssSelector("h2.text-primary")).getText());

        clearDataEnterPersonalDataSubmitAndCheckUrl("", "", "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, "", "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, password, "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, password, password,initialUrl);

        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());

        driver.findElement(By.cssSelector("input#gdprMessage")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertEquals(NOT_MEET_REQUIREMENTS_ALERT, driver.findElement(By.cssSelector("div.alert.alert-danger.alert-dismissible")).getText());
        Assertions.assertEquals(EMAIL_REGISTERED_BEFORE_ALERT,driver.findElement(By.cssSelector("p.text-danger")).getText());

        driver.findElement(By.cssSelector("input#email")).clear();
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("input#password_two")).sendKeys(password);
        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertRegistrationInfo();
    }

    @Test
    public void leaderRegistrationValidation() {

        final String name = "Xxxxx";
        final String surname = "Xxxxxxx";
        final String email = "xxxxxxxx@xxxxxxx";
        final String emailRegisteredBefore = "xxxxx@xxxxxxx";
        final String password = "XxxXxxxx";
        final String registrationHeaderText = "Rejestracja Prowadzącego";
        final String contactInfo = "Przykładowa informacja dla uczestników o możliwości kontaktu";

        driver.get("application URL");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/register']")));
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        driver.findElement(By.cssSelector("a[href='/registerUser/leader']")).click();

        String initialUrl = driver.getCurrentUrl();
        Assertions.assertEquals(registrationHeaderText, driver.findElement(By.cssSelector("h2.text-primary")).getText());

        clearDataEnterPersonalDataSubmitAndCheckUrl("", "", "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, "", "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, "", "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, "", "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, password, "",initialUrl);
        clearDataEnterPersonalDataSubmitAndCheckUrl(name, surname, emailRegisteredBefore, password, password,initialUrl);

        driver.findElement(By.cssSelector("input#churchStatus1")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());
        driver.findElement(By.cssSelector("textarea#contactInfo")).sendKeys(contactInfo);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());
        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());

        driver.findElement(By.cssSelector("input#gdprMessage")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertEquals(NOT_MEET_REQUIREMENTS_ALERT, driver.findElement(By.cssSelector("div.alert.alert-danger.alert-dismissible")).getText());
        Assertions.assertEquals(EMAIL_REGISTERED_BEFORE_ALERT,driver.findElement(By.cssSelector("p.text-danger")).getText());

        driver.findElement(By.cssSelector("input#email")).clear();
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("input#password_two")).sendKeys(password);
        driver.findElement(By.cssSelector("input#wantsToReceiveSystemEmail")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertRegistrationInfo();
    }

    public void clearDataEnterPersonalDataSubmitAndCheckUrl(String name, String surname, String email, String password, String passwordTwo, String currentUrl) {

        driver.findElement(By.cssSelector("input#fname")).clear();
        driver.findElement(By.cssSelector("input#surname")).clear();
        driver.findElement(By.cssSelector("input#email")).clear();
        driver.findElement(By.cssSelector("input#password")).clear();
        driver.findElement(By.cssSelector("input#password_two")).clear();

        driver.findElement(By.cssSelector("input#fname")).sendKeys(name);
        driver.findElement(By.cssSelector("input#surname")).sendKeys(surname);
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("input#password_two")).sendKeys(passwordTwo);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assertions.assertEquals(currentUrl, driver.getCurrentUrl());
    }

    public void assertRegistrationInfo() {
        Assertions.assertTrue(driver.findElement(By.cssSelector("div[class='card-body']")).isDisplayed());
        Assertions.assertEquals(AFTER_REGISTRATION_TEXT, driver.findElement(By.cssSelector("div[class='card-body']")).getText());
    }


}
