package com.example.oneread.di.anotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
Cả applicationcontext và activitycontext đều là thể hiện của lớp context
nên cần dùng Qualifier để phân biệt 2 đối tượng này
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
