package io.github.something_inconspicuous;

import io.github.something_inconspicuous.stomp.AbstractArgs;
import io.github.something_inconspicuous.stomp.Arg;
import io.github.something_inconspicuous.stomp.NotArg;

/**
 * This is an example user class to contain fields that corrospond to program arguments.
 * 
 * @author Matthew "Something Inconspicuous"
 */
public class Args extends AbstractArgs {
    @NotArg
    int notAnArg;

    @Arg(longName = "first", shortName = "f", required = true)
    public String firstWord;

    @Arg()
    public String secondWord;

    @Arg(shortName = "n")
    public int num;

    @Arg(shortName = "b")
    public boolean bool = true;
}