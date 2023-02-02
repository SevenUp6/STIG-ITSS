package com.xjrsoft.common.annotation;

import com.xjrsoft.common.Enum.TransDataType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DataTrans {

    TransDataType dataType() default TransDataType.DEFAULT;

    String dataCode() default "";

    String showField() default "";

    String savedField() default "";

    boolean multi() default false;
}
