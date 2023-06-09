package com.github.somethinginconspicuous.exceptions;

import com.github.somethinginconspicuous.AbstractArgs;
import com.github.somethinginconspicuous.Stomp;

/**
 * Indicates that a field that was supposed to corrospond to a
 * command line argument was encountered that was not properly
 * set up. This could be caused by the field not being {@code public}, 
 * being declared {@code final}, or any other reason that would make it 
 * unwritable by the parser.
 * 
 * @author Something Inconspicuous
 * @see Stomp#parse(AbstractArgs, String[])
 */
public class IllegalArgumentFieldException extends RuntimeException {
    
}
