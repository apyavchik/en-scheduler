import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EnSchedulerTest {
    private String teacher = "";
    private String email = "";
    private String password = "";

    @Test
    public void checkIsRedirectToAuth() {
        teacher = System.getProperty("teacher");
        email = System.getProperty("email");
        password = System.getProperty("password");

        if (teacher.equals("") || email.equals("") || password.equals("")) {
            throw new RuntimeException("You should start application:\n" +
                    "mvn clean test \n" +
                    "-Dteacher=<teacherName> \n" +
                    "-Demail=<email> \n" +
                    "-Dpassword=<password> \n " +
                    "Please, check details in README.md \n ");
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        }
        if (SystemUtils.IS_OS_MAC) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        }
        if (SystemUtils.IS_OS_LINUX) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_linux");
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
//                "--headless",
                "--disable-gpu",
                "--window-size=2920,3200",
                "--ignore-certificate-errors");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.get("https://mate.academy/sign-in");

        driver.findElement(By.xpath("//*[@id='__next']/div/main/div/div[2]/div/div[2]/button")).click();

        driver.findElement(By.id("user-email")).sendKeys(email);
        driver.findElement(By.id("user-password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String locator = "//p[text()='" + teacher + "']/parent::div/parent::div/parent::td//following-sibling::td[5]//span[text()='Записаться']";

        WebDriverWait wait = new WebDriverWait(driver, TimeUnit.SECONDS.toSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='sc-htpNat sc-1ieasxn-7 hyULJw'][1]")));

        driver.get("https://mate.academy/events");
        sleep(5000);

        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            element.click();
            sleep(10000);
        }

        driver.quit();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Something wrong");
        }
    }
}
