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
import java.time.Duration;
import java.util.List;


/**
 * Scrapes data from JDSports website
 * Uses Selenium for web Scraping and saves the data to the database using SaveData class
 * */
@Service
public class ScraperJD {

    private static final String url = "https://www.jdsports.co.uk/men/mens-footwear/trainers/brand/nike/?max=500";
    private WebDriver driver;
    private String name;
    private BigDecimal price;
    private String colour;
    private String description;
    private String img_url;
    private String retailer_url;


    /**
     * Initializes a new instance of the ScraperJD class
     * Configures the ChromdeDriver and sets the user-agent
     * */
    public ScraperJD() {
        ChromeOptions chromeOptions = new ChromeOptions();


        chromeOptions.addArguments("--user-agent="+"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.6045.160 Safari/537.36");

        this.driver = new ChromeDriver(chromeOptions);
    }

    /**
     *
     * Scrape data from JDSports website and save it to the database
     * */
    public void scrape() {
        SaveData save = new SaveData();
        save.init();


        driver.get(url);



        WebElement acceptBtn = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/div[2]/button[2]"));

        acceptBtn.click();

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


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement getProdCounter = driver.findElement(By.xpath("//*[@id=\"productListRefine\"]/div/div[4]"));
        String convertProdCounter = getProdCounter.getText();
        String removeProductString = convertProdCounter.substring(0, 3);
        int prodCounter = Integer.parseInt(removeProductString);



        Models model = new Models();
        Details detail = new Details();
        Comparison comp = new Comparison();


        List<WebElement> links = driver.findElements(By.cssSelector("a.itemImage"));

        for(int i = 0; i < links.size(); i++){
            if(i>= 100){
                break;
            }

            retailer_url = links.get(i).getAttribute("href");

            links.get(i).click();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                WebElement elementName = driver.findElement(By.xpath("//*[@id=\"productItemTitle\"]/h1"));
                name = elementName.getAttribute("innerHTML");


            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {
                WebElement elementPrice = driver.findElement(By.xpath("//*[@id=\"productItemTitle\"]/div[1]/span"));
                String convertPrice = elementPrice.getText();
                String remove_Pounds_sign = convertPrice.replaceAll("[£]", "");
                price = new BigDecimal(remove_Pounds_sign);

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

            try {

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


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                WebElement expand_Prod_info_btn = driver.findElement(By.xpath("//*[@id=\"decriptionExtender\"]/i"));
                expand_Prod_info_btn.click();

                WebElement elementColour = driver.findElement(By.xpath("//*[@id=\"itemInfoContainer\"]/div[1]"));
                String getFullText = elementColour.getText();
                int firstIindex = getFullText.indexOf("\n");
                String getDescription = getFullText.substring(0, firstIindex);
                description = getDescription;

                int lastIndex = getFullText.lastIndexOf("\n");
                String getColour = getFullText.substring(lastIndex +1);
                colour = getColour;



                WebElement elementImage = driver.findElement(By.xpath("//*[@id=\"multiImageContainer\"]/picture[1]/img"));
                img_url = elementImage.getAttribute("src");

                System.out.println(img_url + " Image link");
                System.out.println(retailer_url + " Retailer Link");

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }


            model.setName(name);
            model.setDescription(description);
            model.setImg_url(img_url);

            detail.setColour(colour);
            detail.setRetailer_name("JDSports");
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

            links = driver.findElements(By.cssSelector("a.itemImage"));
        }

        save.shutdown();
        driver.close();

    }


}
