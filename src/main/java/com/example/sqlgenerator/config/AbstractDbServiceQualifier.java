package com.example.sqlgenerator.config;

import com.example.sqlgenerator.enums.SupportedDbType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://medium.com/@pradumnkr98/dynamic-bean-injection-in-spring-how-to-select-beans-based-on-enum-values-ea290fdd4442
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface AbstractDbServiceQualifier {
    SupportedDbType type();
}
