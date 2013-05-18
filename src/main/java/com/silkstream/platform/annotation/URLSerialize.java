package com.silkstream.platform.annotation;

import com.silkstream.platform.enums.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface URLSerialize {
	SerializeType type() default SerializeType.NORMAL;
}
