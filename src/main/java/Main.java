import crawler.Crawler;
import db.ConnectionProvider;
import org.openqa.selenium.WebDriver;
import selenium.Selenium;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionProvider.getConnection();
        System.out.println("Connected to Database: " + connection.getCatalog());

        Crawler crawler = new Crawler();
        WebDriver driver = crawler.connectSelenium();
        crawler.crawlData(driver);

        connection.close();
    }
}
