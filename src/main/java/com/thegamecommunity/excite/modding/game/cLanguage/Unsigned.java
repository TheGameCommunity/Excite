package com.thegamecommunity.excite.modding.game.cLanguage;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE_USE })
public @interface Unsigned {

}
