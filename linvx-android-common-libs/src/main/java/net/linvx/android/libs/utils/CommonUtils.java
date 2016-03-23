package net.linvx.android.libs.utils;

import java.lang.reflect.Field;

/**
 * Created by lizelin on 16/3/17.
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 获取类的静态属性值
     * @param className
     * @param field
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <T> T getClassStaticFieldValue(String className, String field) throws ClassNotFoundException,
            NoSuchFieldException,IllegalAccessException
    {
        boolean classFounded = false, fieldFounded = false;
        Class c =   Class.forName(className);
        Field f = f = c.getDeclaredField(field);
        return (T) (f.get(c));

    }

    public static void main(String[] args) {
        boolean a = false;
        try {
            a = getClassStaticFieldValue("net.linvx.android.BuildConfig", "DEBUG");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //boolean a =  CommonUtils.getLogFlag("net.linvx.android.BuildConfig", "DEBUG");
        System.out.println(a);
    }
}
