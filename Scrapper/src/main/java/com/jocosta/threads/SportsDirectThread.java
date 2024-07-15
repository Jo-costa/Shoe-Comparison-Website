package com.jocosta.threads;

import com.jocosta.scrapers.ScraperSportsDirect;



/**
 * Represents a thread meant to run ScraperSportsDirect
 */
public class SportsDirectThread extends Thread{

    /**
     * Executes the scraperSportsDirect when started
     */
    @Override
    public void run(){

        ScraperSportsDirect sp = new ScraperSportsDirect();

        sp.scrape();
    }
}
