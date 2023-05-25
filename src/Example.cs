// LINK - LICENSE

using Stomp;
using System;

class Example {
    class Args : AbstractArgs {
        [Arg("your", nameof(firstword), "f")]
        string firstword;
        string secondword;
    }

    public static void Main(string[] args){
        Args arg = AbstractArgs.Parse<Args>(args);

        int temp = 1;
        Console.WriteLine(temp);
        int temp2 = 
            ++temp;
        Console.WriteLine(temp2);
        for (int i = 0; i < args.Length; i++) {
            Console.WriteLine(args[i]);
        }
    }
}