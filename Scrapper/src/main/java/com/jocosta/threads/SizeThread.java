package com.jocosta.threads;

import com.jocosta.scrapers.ScraperSize;


/**
 * Represents a thread meant to run ScraperSize
 * */
public class SizeThread extends Thread {


    /**
     * Executes the scraperSize when started
     */
    @Override
    public void run(){
        ScraperSize sz = new ScraperSize();
        sz.scrape();
    }
}
