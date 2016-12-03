package factory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import bean.BeanDefinition;

public interface ApplicationContext {
	Object getBean(String beanName);
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
	Map<String, Method> getRequestMapping();
	Map<String, LinkedList<String>> getRequestMapping2();
}
