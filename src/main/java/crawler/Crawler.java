package crawler;

import db.ConnectionProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import selenium.Selenium;

import java.util.List;

public class Crawler extends Selenium{

    public JavaQuiz javaQuiz;

    public WebDriver connectSelenium() {
        Selenium selenium = new Selenium();

        String url = "https://data-flair.training/blogs/java-quiz/";
        return selenium.connectSelenium(url);
    }

//    public WebDriver connectSelenium() {
//        System.setProperty("webdriver.chrome.driver", "D:\\JavaSelenium\\chromedriver.exe");
//
//        WebDriver driver = new ChromeDriver();
//
//        driver.manage().window().maximize();
//
//        //Full screen
//        driver.manage().window().fullscreen();
//
//        driver.navigate().to("https://data-flair.training/blogs/java-quiz/");
//
//        System.out.println(driver.getTitle());
//
//        return driver;
//    }

    public void crawlData(WebDriver driver) {
        List<WebElement> quizPart = driver.findElements(By.xpath("/html/body/div[1]/div/div/div/div/div[1]/div[1]/div/div/div[4]/div[2]/ul/li"));
        int size = quizPart.size();
        String partNum;
        javaQuiz = new JavaQuiz();
        int totalQuestion = 0;
        for (int j = 1; j <= size; j++) {
            WebElement quizPartElementName = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/div[1]/div[1]/div/div/div[4]/div[2]/ul/li[" + j + "]/a"));
            partNum = quizPartElementName.getText();
            javaQuiz.setPartNumber(partNum);

            if (j <= 1) {
                driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/main/div[2]/article/div/div[2]/div[1]/ul/li[" + j + "]"));
            } else {
                WebElement aTag = driver.findElement(By.xpath("//*[@id='pagesinwidgets_page_section-128']/div/div[4]/div[2]/ul/li[" + j + "]/a"));
                String linkPart = aTag.getAttribute("href");

                driver.navigate().to(linkPart);
            }

            System.out.println("************************************************");

            List<WebElement> pageTotal;
            int idNumber = 137 + (j - 1);
            WebElement startQuizBtn = driver.findElement(By.xpath("//*[@id='wpProQuiz_" + idNumber + "']/div[5]/div/input"));
            if (startQuizBtn != null) {
                JavascriptExecutor js = (JavascriptExecutor)driver;
                js.executeScript("arguments[0].click()", startQuizBtn);
            }
            
            if (j <= 1) {
                pageTotal = driver.findElements(By.xpath("//*[@id='wpProQuiz_137']/div[12]/div[1]/ol/li"));
            } else {
                pageTotal = driver.findElements(By.xpath("//*[@id='wpProQuiz_" + idNumber + "']/div[12]/div[1]/ol/li"));
            }
            int sizePage = pageTotal.size();
            totalQuestion += sizePage;

            System.out.println(partNum + ": " + sizePage + " Page");

            for (int i = 1; i <= sizePage; i++) {
                System.out.println("=============================================");

                try {
                    Thread.sleep(500);
                    // Click continue page
                    WebElement pageNumber = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/main/div[2]/article/div/div[2]/div[1]/div[2]/div/div[2]/div[12]/div[1]/ol/li[" + i + "]"));
                    JavascriptExecutor js = (JavascriptExecutor)driver;
                    js.executeScript("arguments[0].click()", pageNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Get question
                WebElement questionNumPath = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/main/div[2]/article/div/div[2]/div[1]/div[2]/div/div[2]/div[14]/ol/li[" + i + "]/h5"));
                WebElement questionTextPath = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div/main/div[2]/article/div/div[2]/div[1]/div[2]/div/div[2]/div[14]/ol/li[" + i + "]/div[2]/div"));

                String answer;
                WebElement elementAnswerCss = null;

                if (j <= 1) {

                    //Click check answer
                    WebElement checkBtn = driver.findElement(By.xpath("//*[@id='wpProQuiz_137']/div[14]/ol/li[" + i + "]/input[3]"));
                    JavascriptExecutor clickCheckBtn = (JavascriptExecutor) driver;
                    clickCheckBtn.executeScript("arguments[0].click()", checkBtn);

                    try {
                        Thread.sleep(1000);
                        // Get answer
                        elementAnswerCss = driver.findElement(By.cssSelector("#wpProQuiz_137 > div.wpProQuiz_quiz > ol > li:nth-child(" + i + ") > div.wpProQuiz_question > ul > li.wpProQuiz_questionListItem.wpProQuiz_answerCorrectIncomplete > label"));
                    } catch (Exception w) {
                        w.printStackTrace();
                    }
                } else {
                    //Click check answer
                    WebElement checkBtn = driver.findElement(By.xpath("//*[@id='wpProQuiz_" + idNumber + "']/div[14]/ol/li[" + i + "]/input[3]"));
                    JavascriptExecutor clickCheckBtn = (JavascriptExecutor) driver;
                    clickCheckBtn.executeScript("arguments[0].click()", checkBtn);

                    try {
                        Thread.sleep(1000);
                        // Get answer
                        elementAnswerCss = driver.findElement(By.cssSelector("#wpProQuiz_" + idNumber + " > div.wpProQuiz_quiz > ol > li:nth-child(" + i + ") > div.wpProQuiz_question > ul > li.wpProQuiz_questionListItem.wpProQuiz_answerCorrectIncomplete > label"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String questionNum = questionNumPath.getText();
                String questionText = questionTextPath.getText();
                answer = elementAnswerCss.getText();

                javaQuiz.setQuestion(questionText);
                javaQuiz.setNumQuestion(questionNum);
                javaQuiz.setAnswer(answer);

                saveJavaQuiz(javaQuiz);
                System.out.println(questionNum);
                System.out.println(questionText);
                System.out.println("Answer: " + answer);
                System.out.println("Total question: " + totalQuestion);

            }
            System.out.println();
        }

        driver.quit();
    }

    public void saveJavaQuiz(JavaQuiz javaQuiz) {
        ConnectionProvider connectionProvider = new ConnectionProvider();

        StringBuilder sql = new StringBuilder("INSERT INTO javaquiz ");
        sql.append("(number_question, question, answer, part_number) ");
        sql.append("VALUES (?, ?, ?, ?)");

        connectionProvider.insert(sql.toString(), javaQuiz.getNumQuestion(), javaQuiz.getQuestion(), javaQuiz.getAnswer(), javaQuiz.getPartNumber());
    }

}
