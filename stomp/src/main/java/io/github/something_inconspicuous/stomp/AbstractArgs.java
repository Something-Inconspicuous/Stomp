/*
 * MIT License
 * 
 * Copyright (c) 2023 Something-Inconspicuous
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.something_inconspicuous.stomp;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.github.something_inconspicuous.stomp.exceptions.IllegalArgumentClassException;
import io.github.something_inconspicuous.stomp.exceptions.IllegalArgumentFieldException;
import io.github.something_inconspicuous.stomp.exceptions.IllegalCommandArgumentException;

/**
 * A class that extends {@link AbstractArgs} will be able to act as a container for argument
 * fields.
 */
public abstract class AbstractArgs {
    /**
     * Parse an argument vector (array of strings) into fields of a class that corrospond
     * to the arguments using options to circumvent order.
     * 
     * <p>
     * 
     * The array of strings will include the "options" that the user has entered. For
     * example, if they run the program with the command
     * 
     * <p>
     * {@code java -jar myJar.jar -o image.png}
     * <p>
     * 
     * The {@code args} that the main method is given will contain {@code ["-o", "image.png"]}.
     * The {@link #parse(String[], int, int)} will search through the argument vector and give
     * arguments values based on options that are customizable through the {@link Arg} annotation.
     * 
     * @param args The raw given arguments for the program to parse. This should
     * contain the options and their values
     * @param start The index of the first argument to parse (0 to start from the first given)
     * @param end The index of the last argument to parse + 1 (args.length to end at the last argument)
     * @throws IllegalCommandArgumentException if the values given to the arguments
     * do not work with the given required arguments
     * @throws IllegalArgumentFieldException if the user argument container class
     * contains fields that are not properly accessable to the parsing method
     * @throws IllegalArgumentClassException if the user argument container class
     * does not properly derive from {@link AbstractArgs}
     * 
     * @author Matthew "Something Inconspicuous"
     * 
     * @see Arg
     */
    public final void parse(String[] args) {
        parse(args, 0, args.length);
    }

    /**
     * Parse an argument vector (array of strings) into fields of a class that corrospond
     * to the arguments using options to circumvent order.
     * 
     * <p>
     * 
     * The array of strings will include the "options" that the user has entered. For
     * example, if they run the program with the command
     * 
     * <p>
     * {@code java -jar myJar.jar -o image.png}
     * <p>
     * 
     * The {@code args} that the main method is given will contain {@code ["-o", "image.png"]}.
     * The {@link #parse(String[], int, int)} will search through the argument vector from the
     * given start index and give arguments values based on options that are customizable through
     * the {@link Arg} annotation.
     * 
     * @param args The raw given arguments for the program to parse. This should
     * contain the options and their values
     * @param start The index of the first argument to parse (0 to start from the first given)
     * @param end The index of the last argument to parse + 1 (args.length to end at the last argument)
     * @throws IllegalCommandArgumentException if the values given to the arguments
     * do not work with the given required arguments
     * @throws IllegalArgumentFieldException if the user argument container class
     * contains fields that are not properly accessable to the parsing method
     * @throws IllegalArgumentClassException if the user argument container class
     * does not properly derive from {@link AbstractArgs}
     * 
     * @author Matthew "Something Inconspicuous"
     * 
     * @see Arg
     */
    public final void parse(String[] args, int start) {
        parse(args, start, args.length);
    }

    /**
     * Parse an argument vector (array of strings) into fields of a class that corrospond
     * to the arguments using options to circumvent order.
     * 
     * <p>
     * 
     * The array of strings will include the "options" that the user has entered. For
     * example, if they run the program with the command
     * 
     * <p>
     * {@code java -jar myJar.jar -o image.png}
     * <p>
     * 
     * The {@code args} that the main method is given will contain {@code ["-o", "image.png"]}.
     * The {@link #parse(String[], int, int)} will search through the argument vector from the
     * given start index to the given end index and give arguments values based on options that
     * are customizable through the {@link Arg} annotation.
     * 
     * @param args The raw given arguments for the program to parse. This should
     * contain the options and their values
     * @param start The index of the first argument to parse (0 to start from the first given)
     * @param end The index of the last argument to parse + 1 (args.length to end at the last argument)
     * @throws IllegalCommandArgumentException if the values given to the arguments
     * do not work with the given required arguments
     * @throws IllegalArgumentFieldException if the user argument container class
     * contains fields that are not properly accessable to the parsing method
     * @throws IllegalArgumentClassException if the user argument container class
     * does not properly derive from {@link AbstractArgs}
     * 
     * @author Matthew "Something Inconspicuous"
     * 
     * @see Arg
     */
    public final void parse(String[] args, int start, int end) {
        Field[] fields = getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Arg arga = fields[i].getAnnotation(Arg.class);

            if(arga == null){
                if(fields[i].getAnnotation(NotArg.class) == null){
                    new IllegalArgumentFieldException(format("Field \"%s\" is not given the @%s.", fields[i], Arg.class)).printStackTrace();
                }
                continue;
            }
            setField(args, start, end, fields[i], arga);
        }
        
        //System.out.println("Parse finished");
    }
    
    private void setField(String[] args, int start, int end, Field field, Arg arga) {
        final String longName = getLongNameFrom(field, arga);
    
        final String shortName = getShortNameFrom(arga);

        boolean fieldWasSet = false;
        for(int i = start; i < end; i++){
            
            if(args[i].equals(longName) || args[i].equals(shortName)){
                
                boolean tempAccessable;
                try{
                    tempAccessable = field.canAccess(this);
                } catch (IllegalArgumentException e){
                    tempAccessable = field.canAccess(null);
                    new IllegalArgumentFieldException(format("WARNING: argument fields (\"%s\") should not be static", field)).printStackTrace();
                }
                field.setAccessible(true);

                if(i == args.length - 1){
                    if(field.getType() == boolean.class){
                        try {
                            field.set(this, !field.getBoolean(this));
                            fieldWasSet = true;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                    
                    throw new IllegalCommandArgumentException(format("No given value found for argument %s.", longName));
                }
                
                final Class<?> argType = field.getType();
                //.println(argType);
                
                //SECTION - string parse
                if(argType == String.class){
                    try {
                        field.set(this, args[++i]);
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - int parse
                if(argType == int.class || argType == Integer.class){
                    try {
                        field.set(this, Integer.parseInt(args[++i]));
                        //fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to int value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - float parse
                if(argType == float.class || argType == Float.class){
                    try {
                        field.set(this, Float.parseFloat(args[++i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to float value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - long parse
                if(argType == long.class || argType == Long.class){
                    try {
                        field.set(this, Long.parseLong(args[++i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to long value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - double parse
                if(argType == double.class || argType == Double.class){
                    try {
                        field.set(this, Double.parseDouble(args[++i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to double value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - byte parse
                if(argType == byte.class || argType == Byte.class){
                    try {
                        field.set(this, Byte.parseByte(args[++i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to byte value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - short parse
                if(argType == short.class || argType == Short.class){
                    try {
                        field.set(this, Short.parseShort(args[++i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to short value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - char parse
                if(argType == char.class || argType == Character.class){
                    try {
                        field.set(this, args[++i].charAt(0));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (IndexOutOfBoundsException e) {
                        throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to char value", args[i]));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - bool parse
                if(argType == boolean.class || argType == Boolean.class){
                    //If no value is given, do the opposite of the defualt
                    if(args[++i].startsWith("-")){
                        try {
                            field.set(this, !field.getBoolean(this));
                            fieldWasSet = true;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                        } finally {
                            field.setAccessible(tempAccessable);
                        }
                        break;
                    }
                    try {
                        field.set(this, Boolean.parseBoolean(args[i]));
                        fieldWasSet = true;
                        //System.out.format("Set field %s to value %s\n", field, args[i]);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                    } finally {
                        field.setAccessible(tempAccessable);
                    }
                    break;
                }
                //!SECTION

                //SECTION - Object parse
                try {
                    field.set(this, argType.getConstructor(String.class).newInstance((args[i + 1])));
                    fieldWasSet = true;
                } catch (IllegalAccessException e) {
                    new IllegalArgumentFieldException(format("Field \"%s\" is not writable.", field)).printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.getCause().printStackTrace();
                } catch (InstantiationException | NoSuchMethodException e) {
                    new IllegalArgumentFieldException(format("Field \"%s\" is unable to be constucted from a string.", field)).printStackTrace();
                } finally {
                    field.setAccessible(tempAccessable);
                }
                //!SECTION
            }
        }
        if(!fieldWasSet && arga.required()){
            throw new IllegalCommandArgumentException(format("Argument \"%s\" is required, yet no given argument corrosponds to it.", longName));
        }
    }

    private String getShortNameFrom(Arg arga) {
        String shortName = arga.shortName();
        if(shortName != null && !shortName.equals("")){
            shortName = "-" + shortName;
        } else {
            shortName = null; // no defualt for short version
        }
        return shortName;
    }

    private String getLongNameFrom(Field field, Arg arga) {
        String longName = arga.longName();
        if(longName == null || longName.equals("")){
            longName = field.getName(); // Default to field name
        }
        longName = "--" + longName;
        return longName;
    }
}
