package com.example.oneread.di.anotation;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
Anotation này cho biết phạm vi tồn tại của các đối tượng được dagger cung cấp
ActivityScope : Phạm vi tồn tại trong activity chứ ko tồn tại trong cả vòng đời của app
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}