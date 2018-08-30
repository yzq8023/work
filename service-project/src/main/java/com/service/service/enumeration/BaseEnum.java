package com.service.service.enumeration;

public interface BaseEnum<E extends Enum<?>,T> {
     T getCode();
     String getStatus();

}
