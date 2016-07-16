package com.mvc.util;

import java.util.HashMap;
import java.beans.BeanInfo; 
import java.beans.IntrospectionException; 
import java.beans.Introspector; 
import java.beans.PropertyDescriptor; 
import java.lang.reflect.InvocationTargetException; 
import java.lang.reflect.Method; 
import java.util.Map; 

import org.springframework.stereotype.Service;

@Service
public class ObjectUtil {
	public static Map convertBean(Object bean) 
            throws IntrospectionException, IllegalAccessException, InvocationTargetException { 
        Class type = bean.getClass(); 
        Map returnMap = new HashMap(); 
        BeanInfo beanInfo = Introspector.getBeanInfo(type); 

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 
            if (!propertyName.equals("class")) { 
                Method readMethod = descriptor.getReadMethod(); 
                Object result = readMethod.invoke(bean, new Object[0]); 
                if (result != null) { 
                    returnMap.put(propertyName, result); 
                } else { 
                    returnMap.put(propertyName, ""); 
                } 
            } 
        } 
        return returnMap; 
    } 
}
