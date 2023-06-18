package io.github.somethinginconspicuous;

/**
 * This is an example of how to use Stomp to parse the given array of strings into
 * a user created object (see {@link Args})
 * @see Args
 * @author Matthew "Something Inconspicuous"
 */
public class Main {
    public static void main(String[] args){
        Args arg = new Args();
        //arg.printClass();
        arg.parse(args);
        System.out.format("%s %s\n", arg.firstWord, arg.secondWord, arg.num);
        System.out.format("You can also do numbers: %d\n", arg.num);
        System.out.format("And booleans: %b\n", arg.bool);
    }
}