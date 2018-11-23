/**
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.annotation.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Demon
 * @date 2016年12月10日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CNum {
}
