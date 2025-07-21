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

    public static void setAllFields(Object object, Object...args){
        Class<?> clazz = object.getClass();

        int count = 0;

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
}
