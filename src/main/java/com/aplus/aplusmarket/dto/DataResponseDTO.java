package com.aplus.aplusmarket.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/*
    2024.1.26 하진희 :  responseDTO (data필요시)
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DataResponseDTO<T> extends ResponseDTO {
    private T data;

    private DataResponseDTO(T data){
        super();
        this.data = data;
    }

    public DataResponseDTO(T data,int code,String message){
        super("success",code,message);
        this.data=data;
    }


    public DataResponseDTO(T data,int code){
        super("success", code,"success");
        this.data=data;
    }


    public static <T> DataResponseDTO<T> of(T data){
        return new DataResponseDTO<T>(data);
    }
    public static <T> DataResponseDTO<T> of(T data,int code, String message){
        return new DataResponseDTO<T>(data,code,message);
    }

    public static <T> DataResponseDTO<T> success(T data,int code){
        return new DataResponseDTO<T>(data,code,"success");
    }
    public static <T> DataResponseDTO<T> empty(String message,int code){
        return new DataResponseDTO<T>(null,code,message);
    }
}
