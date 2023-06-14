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

import io.github.something_inconspicuous.stomp.exceptions.IllegalArgumentClassException;
import io.github.something_inconspicuous.stomp.exceptions.IllegalArgumentFieldException;
import io.github.something_inconspicuous.stomp.exceptions.IllegalCommandArgumentException;

/**
 * 
 */
public abstract class AbstractArgs {
    public void printClass(){
        //System.out.println(getClass());
    }

    /**
     * 
     * @param args The raw given arguments for the program to parse. This should
     * contain the options and their values
     * @throws IllegalCommandArgumentException if the values given to the arguments
     * do not work with the given required arguments
     * @throws IllegalArgumentFieldException if the user argument container class
     * contains fields that are not properly accessable to the parsing method
     * @throws IllegalArgumentClassException if the user argument container class
     * does not properly derive from {@link AbstractArgs}
     */
    public final void parse(String[] args) {
        Field[] fields = getClass().getDeclaredFields();

        for (Field field : fields) {
            Arg arga = field.getAnnotation(Arg.class);

            if(arga == null){
                if(field.getAnnotation(NotArg.class) == null){
                    new IllegalArgumentFieldException(format("Field \"%s\" is not given the @%s.", field, Arg.class)).printStackTrace();
                }
                continue;
            }
            
            String longName = arga.longName();
            if(longName == null || longName.equals("")){
                longName = field.getName(); // Default to field name
            }
            longName = "--" + longName;

            String shortName = arga.shortName();
            if(shortName != null && !shortName.equals("")){
                shortName = "-" + shortName;
            } else {
                shortName = null; // no defualt for short version
            }
            
            boolean fieldWasSet = false;
            for(int i = 0; i < args.length; i++){
                //System.out.format("args[%d]: %s\n", i, args[i]);
                //System.out.format("Comparing %s to %s with result %b\n", args[i], longName, (args[i].equals(longName)));
                if(args[i].equals(longName) || args[i].equals(shortName)){
                    if(i == args.length - 1){
                        if(field.getType() == boolean.class){
                            try {
                                field.set(this, !field.getBoolean(this));
                                fieldWasSet = true;
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                new IllegalArgumentFieldException(
                                    format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                    e.fillInStackTrace()
                                )
                                .printStackTrace();
                            }

                            break;
                        }
                        
                        throw new IllegalCommandArgumentException(format("No given value found for argument %s.", longName));
                    }
                    
                    final Class<?> argType = field.getType();
                    //.println(argType);
                    
                    if(argType == String.class){
                        try {
                            field.set(this, args[++i]);
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == int.class){
                        try {
                            field.set(this, Integer.parseInt(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to int value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == float.class){
                        try {
                            field.set(this, Float.parseFloat(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to float value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == long.class){
                        try {
                            field.set(this, Long.parseLong(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to long value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == double.class){
                        try {
                            field.set(this, Double.parseDouble(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to double value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == byte.class){
                        try {
                            field.set(this, Byte.parseByte(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to byte value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == int.class){
                        try {
                            field.set(this, Integer.parseInt(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to int value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == short.class){
                        try {
                            field.set(this, Short.parseShort(args[++i]));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to short value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == char.class){
                        try {
                            field.set(this, args[++i].charAt(0));
                            fieldWasSet = true;
                            //System.out.format("Set field %s to value %s\n", field, args[i]);
                        } catch (IndexOutOfBoundsException e) {
                            throw new IllegalCommandArgumentException(format("Cannot convert argument \"%s\" to char value", args[i]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    if(argType == boolean.class){
                        //If no value is given, do the opposite of the defualt
                        if(args[++i].startsWith("-")){
                            try {
                                field.set(this, !field.getBoolean(this));
                                fieldWasSet = true;
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                new IllegalArgumentFieldException(
                                    format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                    e.fillInStackTrace()
                                )
                                .printStackTrace();
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
                            new IllegalArgumentFieldException(
                                format("Field %s innaccessable to parsing. Please set all argument fields to public visiblity.", field),
                                e.fillInStackTrace()
                            )
                            .printStackTrace();
                        }
                        break;
                    }

                    try {
                        field.set(this, argType.cast(args[i + 1]));
                        fieldWasSet = true;
                    } catch(IllegalAccessException | IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    
                    break;
                }
            }
            if(!fieldWasSet && arga.required()){
                throw new IllegalCommandArgumentException(format("Argument \"%s\" is required, yet no given argument corrosponds to it.", longName));
            }
        }

        //System.out.println("Parse finished");
    }
}
