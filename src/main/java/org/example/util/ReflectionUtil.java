package org.example.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static boolean IsNotEmptyFields(Object object){
        Class<?> clazz = object.getClass();

        for (Field field: clazz.getFields()){
            field.setAccessible(true);
            try {
                if (field.get(object) == null){
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    // вот эта вещб заполняет поля (лол, сделал тоже самое что на последеней консультации )
    public static void setAllFields(Object object, Object...args){
        Class<?> clazz = object.getClass();

        int count = 0;

//        for (int i = 0; i < args.length; i++){
//            Field field = clazz.getField(args]);
//        }


        for (Field field: clazz.getDeclaredFields()){
            field.setAccessible(true);
            try {
                field.set(object ,args[count]);
                count++;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setField(Object object, Object arg){
        Class<?> clazz = object.getClass();

        Field fieldFound = null;

        for (Field field: clazz.getDeclaredFields()){
            try {
                if (field.get(object) == null){
                    fieldFound = field;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            fieldFound.set(object, arg);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
