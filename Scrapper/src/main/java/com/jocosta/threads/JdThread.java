package com.jocosta.threads;

import com.jocosta.scrapers.ScraperJD;


/**
 * Represents a thread meant to run ScraperJD
 */
public class JdThread extends Thread{


    /**
     * Executes the scraperJD when started
     */
    @Override
    public void run(){
        ScraperJD jd = new ScraperJD();

        jd.scrape();
    }
}
