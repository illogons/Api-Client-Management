package com.gonzalovega.clientmanagement.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(extensions = @Extension(
        properties = @ExtensionProperty(name = "x-is-read", value = "true")
))
public @interface SearchOperation {
    String summary() default "Search with multiple criteria";
    String description() default "";
    ApiResponse[] responses() default {};
}
