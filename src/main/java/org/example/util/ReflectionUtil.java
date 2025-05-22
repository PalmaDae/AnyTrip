package org.example.util;

import java.lang.reflect.Field;

public class CheckInClass {

    public static boolean IsNotEmptyFields(Object object){
        Class<?> clazz = object.getClass();

        for (Field field: clazz.getFields()){
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

}
