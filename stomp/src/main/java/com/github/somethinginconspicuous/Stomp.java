package com.github.somethinginconspicuous;

import static java.lang.String.format;

import java.lang.reflect.Field;

import com.github.somethinginconspicuous.exceptions.IllegalCommandArgumentException;

public final class Stomp {
    /**
     * 
     * @param <A>
     * @param aargs
     * @param args
     * @return
     * @throws IllegalCommandArgumentException
     * @throws IllegalArgumentFieldException
     */
    static <A extends AbstractArgs> A parse(A aargs, String[] args) {
        Field[] fields = args.getClass().getDeclaredFields();

        for (Field field : fields) {
            Arg arga = field.getAnnotation(Arg.class);
            
            String longName = arga.longName();
            if(longName == ""){
                longName = field.getName();
            }
            longName = "--" + longName;
            
            //for (String arg : args) {
            for(int i = 0; i < args.length; i++){
                if(args[i] == longName || args[i] == "-" + arga.shortName()){
                    if(i == args.length - 1){
                        throw new IllegalCommandArgumentException(format("No given value found for argument %s.", longName));
                    }
                    
                    final Class<?> argType = field.getDeclaringClass();
                    
                    if(argType == String.class){
                        try {
                            field.set(aargs, args[i + 1]);
                        } catch(IllegalAccessException | IllegalArgumentException e){
                            e.printStackTrace();
                        }
                        continue;
                    }

                    try {
                        field.set(aargs, argType.cast(args[i + 1]));
                    } catch(IllegalAccessException | IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }

        }

        return null;
    }
}
