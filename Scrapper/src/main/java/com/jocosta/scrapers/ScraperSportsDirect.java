package com.jocosta.scrapers;

import com.jocosta.SaveData;
import com.jocosta.tables.Comparison;
import com.jocosta.tables.Details;
import com.jocosta.tables.Models;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
* Scrapes data from Sports Direct
* Uses Selenium for web Scraping and saves the data to the database using SaveData class
* */

@Service
public class ScraperSportsDirect {

    //url of the website to scrape data from
    private static final String url = "https://www.sportsdirect.com/mens/footwear/trainers/nike#dcp=1&dppp=500&OrderBy=rank&Filter=none";
    //webdriver to interact with the browser
    private final WebDriver driver;
    //variables to store scraped data
    private String name;
    private BigDecimal price;
    private String colour;
    private String description;
    private String img_url;
    private String retailer_url;


    /**
    * Initializes a new instance of the ScraperSportsDirect class
     * Configures the ChromdeDriver and sets the user-agent
    * */

    //constructor to init webdriver
    public ScraperSportsDirect() {
        ChromeOptions chromeOptions = new ChromeOptions();


        chromeOptions.addArguments("--user-agent="+"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        this.driver = new ChromeDriver(chromeOptions);
    }


    /**
    *
    * Scrape data from Sports Direct website and save it to the database
    * */
    public void scrape(){
        //init SaveData class to save scraped data to database
        SaveData save = new SaveData();
        save.init();

        //init objects for database entities
        Models model = new Models();
        Details detail = new Details();
        Comparison comp = new Comparison();

        driver.get(url);

        WebElement acceptBtn = driver.findElement(By.xpath("//*[@id=\"onetrust-accept-btn-handler\"]"));
        acceptBtn.click();

        //find all products links in the page
        List<WebElement> links = driver.findElements(By.cssSelector("a.ProductImageList"));



        for (int i = 0; i < links.size(); i++){
            if(i >= 100){
                break;
            }


            //refresh the list of products after each iteration
            links = driver.findElements(By.cssSelector("a.ProductImageList"));


            try{
                retailer_url = links.get(i).getAttribute("href");

            links.get(i).click();
            }catch (ElementClickInterceptedException e){
                e.printStackTrace();
                continue;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            WebElement openFeaturesPanel = driver.findElement(By.cssSelector("a.collapsed"));
            openFeaturesPanel.click();

            List<WebElement> featuresTitles = driver.findElements(By.cssSelector("span.feature-name"));
            List<WebElement> featureNames = driver.findElements(By.cssSelector("span.feature-value"));
            for(int j = 0; j < featuresTitles.size(); j++){
                boolean found = false;
                if(featuresTitles.get(j).getAttribute("innerHTML").equals("Collection")){
                    name = featureNames.get(j).getAttribute("innerHTML");
                    found = true;
                    break;

                }

                if(j == 4 && found==true){
                    ;
                    break;
                }
            }


            try {

            WebElement elementPrice = driver.findElement(By.cssSelector("span.productHasRef"));
            String convertPrice = elementPrice.getText();
            String remove_Pounds_Sign = convertPrice.replaceAll("[£]", "");
            price = new BigDecimal(remove_Pounds_Sign);
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {

            WebElement elementColour = driver.findElement(By.id("colourName"));
            colour = elementColour.getText();
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {

                WebElement elementDescription = driver.findElement(By.cssSelector("div.productDescriptionInfoText"));
                description = elementDescription.getText();
            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {
            WebElement elementImg = driver.findElement(By.xpath("//*[@id=\"imgProduct_2\"]"));
            img_url = elementImg.getAttribute("src");

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            model.setName(name);
            model.setDescription(description);
            model.setImg_url(img_url);

            detail.setColour(colour);
            detail.setRetailer_name("Sports Direct");
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

            links = driver.findElements(By.cssSelector("a.ProductImageList"));

        }

        save.shutdown();
        driver.close();





    }

}
