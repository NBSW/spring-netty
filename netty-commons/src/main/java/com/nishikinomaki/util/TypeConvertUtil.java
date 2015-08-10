package com.nishikinomaki.util;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtilsBean;

/**
 * Created on 2015/6/24.
 *
 * @author Jax
 */
public final class TypeConvertUtil {

    private static ConvertUtilsBean convertUtilsBean = BeanUtilsBean2.getInstance().getConvertUtils();

    public static <T> T convert(Object in, Class<T> inClass){
        return (T)convertUtilsBean.convert(in, inClass);
    }
}
