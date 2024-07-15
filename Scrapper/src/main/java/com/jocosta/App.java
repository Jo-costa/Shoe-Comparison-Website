package com.jocosta;


import com.jocosta.threads.*;


/**
 * Main class that starts the program
 *
 * */
public class App
{

    /**
    * Main entry point of the program
    *
    * @param args for main method
    * */
    public static void main( String[] args ) {

        NikeThread nike = new NikeThread();
        JdThread jd = new JdThread();
        SportsDirectThread sd = new SportsDirectThread();
        FootAsylumThread fa = new FootAsylumThread();
        SizeThread sz = new SizeThread();


        nike.start();
        jd.start();
        sd.start();
        fa.start();
        sz.start();



    }
}
