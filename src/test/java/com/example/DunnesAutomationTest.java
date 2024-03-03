package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.FileWriter;
import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class DunnesAutomationTest {

    public static void main(String[] args) {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Navigate to the website
        driver.get("https://demo4-dunnes.buymie.eu/");

        // Click on Create Valuclub Account button
        WebElement createAccountButton = driver.findElement(By.xpath("/html/body/app-root/app-auth-layout/main/div/app-landing-page/div[1]/div/div[1]/a[2]"));
        createAccountButton.click();

        // Generate random email address
        String emailPrefix = "Auto" + (int) (Math.random() * 1000);
        String email = emailPrefix + "@harakirimail.com";

        // Enter email address and password
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mat-input-0")));
        System.out.println(emailField);
        emailField.sendKeys(email);
        driver.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys("aaAA11!!");
        driver.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys("aaAA11!!");
        driver.findElement(By.xpath("//*[@id=\"mat-mdc-checkbox-1-input\"]")).click();

        // Click on continue button
        driver.findElement(By.xpath("/html/body/app-root/app-auth-layout/main/div/app-sign-up/div/div[1]/form/div/div/button")).click();

        System.out.println("hi");
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("bye");
        String initialTabHandle = driver.getWindowHandle();



        ((JavascriptExecutor) driver).executeScript(String.format("window.open('https://harakirimail.com/inbox/%s','_blank');", emailPrefix));
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(initialTabHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        WebDriverWait frWait = new WebDriverWait(driver, Duration.ofSeconds(5000));
        WebElement aida= driver.findElement(By.xpath("//*[@id=\"mail_list_body\"]/tr[1]/td[2]/a"));
        System.out.println(aida);
        aida.click();



        WebElement verificationEmail = driver.findElement(By.xpath("//strong[contains(text(),'Your email verification code:')]"));
        String code = verificationEmail.getText();
        System.out.println(code);

        Pattern pattern = Pattern.compile("\\d+");

        // Create a Matcher object
        Matcher matcher = pattern.matcher(code);
        driver.switchTo().window(initialTabHandle);

        // Find and print all matches
        while (matcher.find()) {
            System.out.println("Verification Code: " + matcher.group());
            driver.findElement(By.xpath("/html/body/app-root/app-auth-layout/main/div/app-sign-up/div/div[1]/form/div/div/app-secure-code-input/div/input[1]")).sendKeys(matcher.group());

        }


        driver.findElement(By.xpath("/html/body/app-root/app-auth-layout/main/div/app-sign-up/div/div[1]/form/div/div/button[1]")).click();


        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll up the webpage
        js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
        WebDriverWait waito = new WebDriverWait(driver, Duration.ofSeconds(1000));
        WebElement result = waito.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-auth-layout/main/div/app-sign-up/div/div[1]/form/p")));
        try {
            FileWriter writer = new FileWriter("search_result.txt");
            writer.write(result.getText());
            writer.close();
        }
        catch (Exception ResultOfTest){
            System.out.println("Test is done");
        };

        // Get the result

        // Close the browser
        driver.quit();
    }
}
