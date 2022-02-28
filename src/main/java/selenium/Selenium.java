package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium {

    public WebDriver connectSelenium(String url) {
        System.setProperty("webdriver.chrome.driver", "D:\\JavaSelenium\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();

        //Full screen
        driver.manage().window().fullscreen();

        driver.navigate().to(url);

        System.out.println(driver.getTitle());

        return driver;
    }
}
