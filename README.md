# Stomp
Args manager for Java.

## Usage

You can find an example of this package used in the
[Main.java](stomp/src/main/java/io/github/something_inconspicuous/Main.java)
file.

Here is also a step by step guide

1. Add this to your `pom.xml`
    ```xml
    <dependency>
        <groupId>io.github.somethinginconspicuous</groupId>
        <artifactId>stomp</artifactId>
        <version>2.2-SNAPSHOT</version>
    </dependency>
    ```

2. Run via command line
    ```cmd
    mvn install
    ```

3. Make sure to import the stomp package
    ```java
    import io.github.something_inconspicuous.stomp.*;
    ```

4. Create your program
    ```java
    public class Main {
        public static void main(String[] args){
            // stuff in here
        }
    }
    ```

5. Create your container class
    ```java
    class Args extends AbstractArgs {

    }
    ```

6. Create your arguments
    ```java
    class Args extends AbstractArgs {
        @Arg(required = true) // You can require arguments
        public String arg1;

        @Arg(shortName = "a2") // Give things short names
        public String arg2 = "hello"; // Give them defualt values

        @Arg(longName = "num", required = true) // Give them any option name you want
        public int arg3; // Number arguments

        @NotArg // Make the parser ignore a field 
        public char notArg = 'a';

        @Arg(longName = "bool", shortName = "b")
        public boolean arg4 = true; // Booleans can be added
        // If a boolean argument is passed without a value, it will
        // change it to the opposite of its defualt
    }
    ```

7. Parse your arguments
    ```java
    public class Example {
        public static void main(String[] args){
            Args arg = new Arg();
            arg.parse(args);

            // Example stuff
            // Do note, if an argument is not required, it
            // will have its "zeroed" value (null for objects)
            // if not passed in the args
            if(arg.arg4){
                System.out.println(arg.arg1 + " " + arg.arg2);
                System.out.println(arg.arg3 + 1);
            } else {
                System.out.println(arg.arg2 + " " + arg.arg1);
                System.out.println(arg.arg3 * 2);
            }

        }
    }
    ```

8. Pass your arguments <br>
    ```cmd
    java Example -b --arg1 "Wow, hello" -a2 Stomp! --num 3
    ```
    Will output the following using the above example
    ```
    Stomp! Wow, hello
    6
    ```

## The old one

The **abandonded** version of Stomp, made in and for C# can be found in the
[cs](./cs) folder.
