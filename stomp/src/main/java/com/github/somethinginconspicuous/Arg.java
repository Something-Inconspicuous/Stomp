package com.github.somethinginconspicuous;

public @interface Arg {
    String longName() default "";
    String shortName() default "";
    boolean required() default false;
}
