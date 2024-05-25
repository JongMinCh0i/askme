package com.example.askme.common.resolver.accountInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Target(ElementType.PARAMETER)
@Retention(value = RUNTIME)
public @interface AccountInfo {
}
