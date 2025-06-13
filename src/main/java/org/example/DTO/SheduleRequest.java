package org.example.DTO;


import lombok.Builder;
import lombok.Data;
import org.example.util.ReflectionUtil;

@Data
@Builder
public class SheduleRequest {

    public SheduleRequest(boolean t){}

    public SheduleRequest(Object...args){
        ReflectionUtil.setAllFields(this, args);
    };

    private String station;
    private String transport;
    private String date;

    // сюда доп параметр

}
