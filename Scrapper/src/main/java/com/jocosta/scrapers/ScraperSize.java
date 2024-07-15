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
 * Scrapes data from Size? website
 * Uses Selenium for web Scraping and saves the data to the database using SaveData class
 * */
@Service
public class ScraperSize {

    private static final String url ="https://www.size.co.uk/mens/footwear/brand/nike/s/trainers/?max=500";
    private WebDriver driver;
    private String name;
    private BigDecimal price;
    private String colour;
    private String description;
    private String img_url;
    private String retailer_url;

    /**
     * Initializes a new instance of the ScraperSize class
     * Configures the ChromdeDriver and sets the user-agent
     * */
    public ScraperSize() {
        ChromeOptions chromeOptions = new ChromeOptions();


        chromeOptions.addArguments("--user-agent="+"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        this.driver = new ChromeDriver(chromeOptions);
    }


    /**
     *
     * Scrape data from Size? website and save it to the database
     * */
    public void scrape(){

        SaveData save = new SaveData();
        save.init();

        Models model = new Models();
        Details detail = new Details();
        Comparison comp = new Comparison();

        driver.get(url);



        try {
            WebElement acceptBtn = driver.findElement(By.cssSelector("button.btn.btn-level1.accept-all-cookies"));
            if(acceptBtn.isDisplayed()){
                acceptBtn.click();
            }

        }catch (NoSuchElementException e){
            e.printStackTrace();
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> links = driver.findElements(By.cssSelector("[data-e2e='plp-productList-link']"));

        System.out.println(links.size());

        for(int i = 0; i < 100; i++){

            if(i>= 100){
                break;
            }


            retailer_url = links.get(i).getAttribute("href");



            try{

            links.get(i).click();

            }catch (ElementClickInterceptedException e){
                i--;
                e.printStackTrace();

                continue;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            WebElement elementName = driver.findElement(By.xpath("//*[@id=\"productItemTitle\"]/h1"));
            name = elementName.getText();

            WebElement elementPrice = driver.findElement(By.xpath("//*[@id=\"productItemTitle\"]/div[1]/span"));
            String convertPrice = elementPrice.getText();
            String remove_Pounds_Sign = convertPrice.replaceAll("[£]", "");
            price = new BigDecimal(remove_Pounds_Sign);

            WebElement getDetails = driver.findElement(By.cssSelector("ul.acitem"));

            String getDetailsText = getDetails.getText();
            int firstIndex = getDetailsText.indexOf("\n");
            String getDescription = getDetailsText.substring(0, firstIndex);
            description = getDescription;


            int lastIndex = getDetailsText.lastIndexOf("Colour");
            int prodCodeIndex = getDetailsText.indexOf("Product");
            String getColour = getDetailsText.substring(lastIndex + 7, prodCodeIndex -1);
            colour = getColour;


            WebElement element_img = driver.findElement(By.cssSelector("img.imgMed  "));
            System.out.println(element_img.getAttribute("src"));

            img_url = element_img.getAttribute("src");



            model.setName(name);
            model.setDescription(description);
            model.setImg_url(img_url);

            detail.setColour(colour);
            detail.setRetailer_name("Size?");
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

            links = driver.findElements(By.cssSelector("a.itemImage.imagePaddingBottom"));

        }
        save.shutdown();
        driver.close();

    }//end scrape method

}
