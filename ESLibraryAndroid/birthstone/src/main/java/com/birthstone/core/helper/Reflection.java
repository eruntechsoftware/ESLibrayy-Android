package com.birthstone.core.helper;

import java.lang.reflect.Array;   
import java.lang.reflect.Constructor;   
import java.lang.reflect.Field;   
import java.lang.reflect.Method;   
 
  
public class Reflection {   
    /**  
     * õĳĹ
     *  
     * @param owner, fieldName  
     * @return Զ
     * @throws Exception  
     *  
     */  
    public Object getProperty(Object owner, String fieldName) throws Exception {   
        Class<? extends Object> ownerClass = owner.getClass();   
  
        Field field = ownerClass.getField(fieldName);   
  
        Class<?> property = field.getType();//get(owner);   
        
  
        return property;   
    }   
  
    /**  
     * õĳľ̬
     *  
     * @param className
     * @param fieldName
     * @return Զ
     * @throws Exception  
     */  
    public Object getStaticProperty(String className, String fieldName)   
            throws Exception {   
        Class<?> ownerClass = Class.forName(className);   
  
        Field field = ownerClass.getField(fieldName);   
  
        Object property = field.get(ownerClass);   
  
        return property;   
    }  
    
    /**  
     * õĳľ̬Եֶ
     *  
     * @param className
     * @param value   ֵ
     * @return Զ
     * @throws Exception  
     */  
    public String getStaticProperty(String className, Object value)   
            throws Exception {   
    	String name="";
        Class<?> ownerClass = Class.forName(className);   
        Field[] fields = ownerClass.getFields();  
        int size=fields.length;
		 for(int i=0; i<size; i++)
		 {
			 Object val=fields[i].getInt(ownerClass);
			 if(val.equals(value))
			 {
				 name=fields[i].getName();
				 break;
			 }
		 }
        return name;   
    } 
    
    /**  
     * õĳľ̬Եֵ
     *  
     * @param className
     * @param value
     * @return Զֵ
     * @throws Exception  
     */  
    public Object getStaticPropertyValue(String className, String name)   
            throws Exception {   
        Class<?> ownerClass = Class.forName(className);   
        Field[] fields = ownerClass.getFields();  
        int size=fields.length;
        String fieldName="";
        Object val = null;
        for(int i=0; i<size; i++)
		 {
        	fieldName=fields[i].getName();
			 
			 if(name.equals(fieldName))
			 {
				 val=fields[i].getInt(ownerClass);
				 break;
			 }
		 }
        return val;   
    } 
  
  
    /**  
     * ִĳ󷽷
     *  
     * @param owner  
     *
     * @param methodName  
     *
     * @param args  
     *
     * @return ֵ
     * @throws Exception  
     */  
    @SuppressWarnings("rawtypes")
	public Object invokeMethod(Object owner, String methodName, Object[] args)   
            throws Exception {   
  
        Class<? extends Object> ownerClass = owner.getClass();   
  
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Method method = ownerClass.getMethod(methodName, argsClass);   
  
        return method.invoke(owner, args);   
    }   

      /**  
     * ִĳľ̬
     *  
     * @param className  
     *
     * @param methodName  
     *
     * @param args  
.     *
.     * @return ִзصĽ
     * @throws Exception  
     */  
    @SuppressWarnings("rawtypes")
	public Object invokeStaticMethod(String className, String methodName,   
            Object[] args) throws Exception {   
        Class<?> ownerClass = Class.forName(className);   
  
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Method method = ownerClass.getMethod(methodName, argsClass);   
  
        return method.invoke(null, args);   
    }   
  
  
  
    /**  
     * ½ʵ
     *  
     * @param className  
     *
     * @param args  
     *            캯Ĳ
     * @return ½ʵ
     * @throws Exception  
     */  
    @SuppressWarnings("rawtypes")
	public Object createInstance(String className, Object[] args) throws Exception {   
        Class<?> newoneClass = Class.forName(className);   
        
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Constructor<?> cons = newoneClass.getConstructor(argsClass);   
  
        return cons.newInstance(args);   
  
    }  
    
    /**  
     * ½ʵ
     *  
     * @param className   
     */  
    public Object createInstance(String className) throws Exception {   
        Class<?> newoneClass = Class.forName(className);   
        return newoneClass.newInstance();   
    }  
  
  
       
    /**  
     * ǲĳʵ
     * @param obj ʵ
     * @param cls
     * @return  obj Ǵʵ򷵻 true
     */  
    public boolean isInstance(Object obj, Class<?> cls) {   
        return cls.isInstance(obj);   
    }   
       
    /**  
     * õеĳԪ
     * @param array
     * @param index
     * @return ֵָ
     */  
    public Object getByArray(Object array, int index) {   
        return Array.get(array,index);   
    }   
}  
