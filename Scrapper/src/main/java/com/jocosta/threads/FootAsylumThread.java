package com.jocosta.threads;

import com.jocosta.scrapers.ScraperFootAsylum;


/**
 * Represents a thread meant to run ScraperFootAsylum
 */
public class FootAsylumThread extends Thread{

    /**
     * Executes the scraperFootAsylum when started
     */
    @Override
    public void run(){
        ScraperFootAsylum ft = new ScraperFootAsylum();

        ft.scrape();
    }
}
