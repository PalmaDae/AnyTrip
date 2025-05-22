package org.example.model;


import lombok.Builder;
import lombok.Data;
import org.example.util.ReflectionUtil;

@Data
@Builder
public class Shedule {

    public Shedule(Object...args){
        ReflectionUtil.setAllFields(this, args);
    };

    private String station;
    private String transport;
    private String date;

}
