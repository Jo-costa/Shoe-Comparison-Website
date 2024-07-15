package com.jocosta.threads;

import com.jocosta.scrapers.ScraperNike;


/**
 * Represents a thread meant to run ScraperNike
 */
public class NikeThread extends Thread{


    /**
     * Executes the scraperNike when started
     */
    @Override
    public void run(){
        ScraperNike nike = new ScraperNike();

        nike.scrape();

    }
}
