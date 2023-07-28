package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 10:30
 */
public class DynamicProxyTest {

    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory(new Student());
        Person proxyInstance = (Person) proxyFactory.getProxyInstance();
        System.out.println(proxyInstance.getClass());
        proxyInstance.say();

    }


}


class ProxyFactory implements InvocationHandler {

    private Object target;


    public ProxyFactory(Object target) {
        this.target = target;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object invoke = method.invoke(target, args);
        System.out.println("after");
        return invoke;

    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this);
    }

}

interface Person {
    void say();
}

class Student implements Person {

    @Override
    public void say() {
        System.out.println("hello");
    }
}