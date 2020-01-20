import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.regex.Pattern;

public class LoginValidation {

    WebDriver driver = new ChromeDriver();

    private final String name = "Xxxx";
    private final String surname = "Xxxxxx";
    private final String email = "xxx@xxx";
    private final String password = "xxxxxx";
    private final String wrongEmailWithAt = "xxx@xxxxxxxxxx";
    private final String wrongEmailWithoutAt = "xxxxxx";
    final String wrongPassword = "XxxXxxxXxx";

    String wrongEmailOrPasswordAlert = "×\n" +
            "Błędny email lub hasło.\n" +
            "\nJeśli zapomniałeś hasło - kliknij tutaj";

    @Test
    public void loginValidation() {

        driver.get("application URL");
        String initialUrl = driver.getCurrentUrl();


        enterEmailAndPassword("", "");
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());

        enterEmailAndPassword(email, "");
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());
        clearEmailAndPasswordArea();

        enterEmailAndPassword("", password);
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());
        clearEmailAndPasswordArea();

        enterEmailAndPassword(wrongEmailWithoutAt, password);
        Assertions.assertEquals(initialUrl, driver.getCurrentUrl());
        clearEmailAndPasswordArea();

        enterEmailAndPassword(wrongEmailWithAt, password);
        assertErrorUrl(wrongEmailOrPasswordAlert);
        clearEmailAndPasswordArea();
        driver.get(initialUrl);

        enterEmailAndPassword(email, wrongPassword);
        assertErrorUrl(wrongEmailOrPasswordAlert);
        clearEmailAndPasswordArea();
        driver.get(initialUrl);

        enterEmailAndPassword(email, password);
        Assertions.assertTrue(driver.findElement(By.cssSelector("span[class='my-auto']")).isDisplayed());
        Assertions.assertTrue(Pattern.matches("(.)*" + name + " " + surname, driver.findElement(By.cssSelector("span[class='my-auto']")).getText()));
    }


    public void enterEmailAndPassword(String email, String password) {
        driver.findElement(By.cssSelector("input#email")).sendKeys(email);
        driver.findElement(By.cssSelector("input#password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void clearEmailAndPasswordArea() {
        driver.findElement(By.cssSelector("input#email")).clear();
        driver.findElement(By.cssSelector("input#password")).clear();
    }

    public void assertErrorUrl(String alert) {
        Assertions.assertTrue(driver.findElement(By.cssSelector("div[class='alert alert-danger alert-dismissible']")).isDisplayed());
        Assertions.assertEquals(alert, driver.findElement(By.cssSelector("div[class='alert alert-danger alert-dismissible']")).getText());
        Assertions.assertTrue(driver.getCurrentUrl().contains("?error=true"));
    }

}
