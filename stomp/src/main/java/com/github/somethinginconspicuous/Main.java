package com.github.somethinginconspicuous;

/**
 * @author Something Inconspicuous
 */
public class Main {
    public static void main(String[] args){
        System.out.println( "Hello World!" );
    }

    public class Args extends AbstractArgs {
        @Arg(longName = "first", shortName = "f", required = true)
        String firstWord;
    }
}
