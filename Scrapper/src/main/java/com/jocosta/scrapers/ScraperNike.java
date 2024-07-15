package com.jocosta.scrapers;


import com.jocosta.SaveData;
import com.jocosta.tables.Comparison;
import com.jocosta.tables.Details;
import com.jocosta.tables.Models;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * Scrapes data from Nike website
 * Uses Selenium for web Scraping and saves the data to the database using SaveData class
 * */
@Service
public class ScraperNike {


    private static final String url = "https://www.nike.com/gb/w/mens-lifestyle-shoes-13jrmznik1zy7ok";
    private WebDriver driver;
    private String name;
    private BigDecimal price;
    private String colour;
    private String description;
    private String img_url;
    private String retailer_url;


    /**
     * Initializes a new instance of the ScraperNike class
     * Configures the ChromdeDriver and sets the user-agent
     * */
    public ScraperNike() {
        ChromeOptions chromeOptions = new ChromeOptions();


        chromeOptions.addArguments("--user-agent="+"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        this.driver = new ChromeDriver(chromeOptions);
    }


    /**
     *
     * Scrape data from Nike website and save it to the database
     * */
    public void scrape() {
        SaveData save = new SaveData();
        save.init();

        driver.get(url);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {

        WebElement acceptBtn = driver.findElement(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div/div/section/div[2]/div/button"));
        acceptBtn.click();

        }catch (NoSuchElementException e){
            e.printStackTrace();
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            long lastHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
            while (true) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);

                long newHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
                if (newHeight == lastHeight) {
                    break;
                }

                lastHeight = newHeight;
            }

            js.executeScript("scroll(0, -250)");


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> links = driver.findElements(By.cssSelector("a.product-card__img-link-overlay"));




        Models model = new Models();
        Details detail = new Details();
        Comparison comp = new Comparison();




        for(int i =0; i < links.size();i++){

            if(i>= 100){
                break;
            }


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retailer_url = links.get(i).getAttribute("href");


            links.get(i).click();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                WebElement acceptBtn = driver.findElement(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div/div/section/div[2]/div/button"));
                acceptBtn.click();

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {

            WebElement closeModal = driver.findElement(By.xpath("//*[@id=\"cart-dialog-root\"]/div[2]/div/div/div/div/div/button"));
            if(closeModal.isDisplayed()){
                closeModal.click();
            }
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }




            try{
            WebElement elementName = driver.findElement(By.xpath("//*[@id=\"pdp_product_title\"]"));
            name = elementName.getAttribute("innerHTML");

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }


            try{
            WebElement elementName = driver.findElement(By.xpath("//*[@id=\"skip-to-content\"]/div[1]/section[1]/div[2]/aside/div/div[1]/h1"));
            name = elementName.getAttribute("innerHTML");

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }




            try{
                WebElement elementPrice = driver.findElement(By.xpath("//*[@id=\"RightRail\"]/div/div[1]/div/div[2]/div/div/div/div/div"));

                String convertPrice = elementPrice.getText();
                String remove_Pounds_Sign = convertPrice.replaceAll("[£]", "");
                price = new BigDecimal(remove_Pounds_Sign);

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }try{
                WebElement elementPrice = driver.findElement(By.xpath("//*[@id=\"skip-to-content\"]/div[1]/section[1]/div[2]/aside/div/div[1]/div"));

                String convertPrice = elementPrice.getText();
                String remove_Pounds_Sign = convertPrice.replaceAll("[£]", "");
                price = new BigDecimal(remove_Pounds_Sign);

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try{
                WebElement elementColour = driver.findElement(By.cssSelector("li.description-preview__color-description"));
                String removeText = elementColour.getText().substring(14);
                colour = removeText;
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }
            try{
                WebElement elementColour = driver.findElement(By.xpath("//*[@id=\"skip-to-content\"]/div[1]/section[1]/div[2]/aside/div/div[1]/h2"));
                colour = elementColour.getText();
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }



            try{
            WebElement elementDescription = driver.findElement(By.xpath("//*[@id=\"RightRail\"]/div/div[7]/div/p"));
            description = elementDescription.getText();

            }catch (NoSuchElementException e){

                e.printStackTrace();

            }


            try{
            WebElement elementDescription = driver.findElement(By.xpath("//*[@id=\"skip-to-content\"]/div[1]/section[1]/div[2]/aside/div/div[2]/div/div[1]/p"));
            description = elementDescription.getText();

            }catch (NoSuchElementException e){

                e.printStackTrace();

            }


            try{
            WebElement elementDescription = driver.findElement(By.xpath("//*[@id=\"RightRail\"]/div/span/div/div/p"));
            description = elementDescription.getText();

            }catch (NoSuchElementException e){

                e.printStackTrace();

            }


            try{
            WebElement elementImage = driver.findElement(By.xpath("//*[@id=\"pdp-6-up\"]/button[1]/div/picture[2]/img"));
            img_url = elementImage.getAttribute("src");

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }


            try {
                WebElement elementImage = driver.findElement(By.xpath("//*[@id=\"pdp-6-up\"]/img[1]"));
                img_url = elementImage.getAttribute("src");

            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }

            try {
                WebElement elementImage = driver.findElement(By.xpath("//*[@id=\"skip-to-content\"]/div[1]/section[1]/div[1]/div/button[1]/figure/img"));
                img_url = elementImage.getAttribute("src");

            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }



            model.setName(name);
            model.setDescription(description);
            model.setImg_url(img_url);

            detail.setColour(colour);
            detail.setRetailer_name("Nike");
            detail.setRetailer_url(retailer_url);
            detail.setModel_id(model);

            comp.setDetails(detail);
            comp.setPrice(price);




            save.addModels(model);
            save.addDetails(detail);
            save.addComparison(comp);


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            driver.navigate().back();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                WebElement accept = driver.findElement(By.cssSelector("button.nds-btn"));
                accept.click();


            }catch (NoSuchElementException e){
                e.printStackTrace();
            }


            links = driver.findElements(By.cssSelector("a.product-card__img-link-overlay"));

        }

        save.shutdown();
        driver.close();

    }

}
