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
 * Scrapes data from FootAsylum website
 * Uses Selenium for web Scraping and saves the data to the database using SaveData class
 * */
@Service
public class ScraperFootAsylum {
    private static final String url = "https://www.footasylum.com/brands/nike/trainers/mens/?gender=mens";
    private WebDriver driver;
    private String name;
    private BigDecimal price;
    private String colour;
    private String description;
    private String img_url;
    private String retailer_url;


    /**
     * Initializes a new instance of the ScraperFootAsylum class
     * Configures the ChromdeDriver and sets the user-agent
     * */
    public ScraperFootAsylum(){
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--user-agent="+"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        this.driver = new ChromeDriver(chromeOptions);
    }


    /**
     *
     * Scrape data from FootAsylum website and save it to the database
     * */
    public void scrape(){

        SaveData save = new SaveData();
        save.init();

        Models model = new Models();
        Details detail = new Details();
        Comparison comp = new Comparison();

        driver.get(url);

        List<WebElement> listOfShoes = driver.findElements(By.className("listing-text"));

        WebElement paginationDiv = driver.findElement(By.id("pagination"));
        List<WebElement> totalPages = paginationDiv.findElements(By.tagName("a"));

        int webPageNumber = 2;

        for(int i = 0; i < totalPages.size(); i++){

            listOfShoes = driver.findElements(By.className("listing-text"));

            if(webPageNumber > totalPages.size()){
                break;
            }

            for(int j = 0; j < listOfShoes.size(); j++){

                retailer_url = listOfShoes.get(j).getAttribute("href");
                listOfShoes.get(j).click();


                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                try {

                    WebElement closeModal = driver.findElement(By.xpath("//*[@id=\"bf-inactivity-popup-close\"]/img"));
                    if(closeModal.isDisplayed()){
                        closeModal.click();
                    }

                }catch (NoSuchElementException ex){
                    ex.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                img_url = driver.findElement(By.cssSelector("img.iiz__img   ")).getAttribute("src");


                WebElement elementName = driver.findElement(By.className("product-name"));
                name = elementName.getText();

                //find price element
                WebElement elementPrice = driver.findElement(By.xpath("//*[@id=\"appRoot\"]/section/div[3]/div[2]/section/div[1]/div[2]/div/h2/span"));
                //conver price element to string
                String convertPrice = elementPrice.getText();
                //remove $ sign from the element
                String removePoundsSign = convertPrice.replaceAll("[£]", "");
                //convert price to decimal
                price = new BigDecimal(removePoundsSign);

                WebElement elementColour = driver.findElement(By.cssSelector("p.product-colour"));
                colour = elementColour.getText();

                WebElement elementDescription = driver.findElement(By.xpath("//*[@id=\"description-content-panel\"]/p"));
                description = elementDescription.getText();

                model.setName(name);
                model.setDescription(description);
                model.setImg_url(img_url);

                detail.setColour(colour);
                detail.setRetailer_name("FootAsylum");
                detail.setRetailer_url(retailer_url);
                detail.setModel_id(model);

                comp.setDetails(detail);
                comp.setPrice(price);

                save.addModels(model);
                save.addDetails(detail);
                save.addComparison(comp);

                driver.navigate().back();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {

                    WebElement closeModal = driver.findElement(By.xpath("//*[@id=\"bf-inactivity-popup-close\"]/img"));
                    if(closeModal.isDisplayed()){
                        closeModal.click();
                    }
                }catch (NoSuchElementException ex){
                    ex.printStackTrace();
                }

                listOfShoes = driver.findElements(By.className("listing-text"));

            }


            //navigate to the next page
            String pages = url + "&page="+ webPageNumber;
            driver.get(pages);
            webPageNumber++;


            }

        save.shutdown();
        driver.close();






    }

}
