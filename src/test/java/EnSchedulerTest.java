import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

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
//        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://mate.academy/sign-in");
        driver.findElement(By.id("user-email")).sendKeys(email);
        driver.findElement(By.id("user-password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.get("https://mate.academy/events");
        sleep();

        String locator = "//p[text()='" + teacher + "']/parent::div/parent::div/parent::td//following-sibling::td[5]//span[text()='Записаться']";
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            element.click();
            sleep();
        }

        driver.quit();
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Something wrong");
        }
    }
}
