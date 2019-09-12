package com.soprasteria.modelling.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 * The name of the column. Defaults to the property or field name
	 */
	String name() default "";
	
	/**
	 * The group of the column. If there are too many columns for a entity, group to differentiate
	 * @return
	 */
	String group() default "";
	
	boolean isNull() default true;
	
	boolean isKey() default false;
}
