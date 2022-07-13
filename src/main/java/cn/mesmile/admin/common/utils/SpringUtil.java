package cn.mesmile.admin.common.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * @author zb
 * @Description
 */
@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

	/**
	 * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
	 * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
	 */
	private static ConfigurableListableBeanFactory beanFactory;
	/**
	 * Spring应用上下文环境
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtil.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取{@link ApplicationContext}
	 *
	 * @return {@link ApplicationContext}
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
	 *
	 * @return {@link ListableBeanFactory}
	 */
	public static ListableBeanFactory getBeanFactory() {
		return null == beanFactory ? applicationContext : beanFactory;
	}
	

	/**
	 * 通过name获取 Bean
	 *
	 * @param <T>  Bean类型
	 * @param name Bean名称
	 * @return Bean
	 */
	public static <T> T getBean(String name) {
		return (T) getBeanFactory().getBean(name);
	}

	/**
	 * 通过class获取Bean
	 *
	 * @param <T>   Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param <T>   bean类型
	 * @param name  Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	/**
	 * 通过类型参考返回带泛型参数的Bean
	 *
	 * @param reference 类型参考，用于持有转换后的泛型类型
	 * @param <T>       Bean类型
	 * @return 带泛型参数的Bean
	 */
	public static <T> T getBean(TypeReference<T> reference) {
		final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
		final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
		final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
		final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
		return getBean(beanNames[0], rawType);
	}

	/**
	 * 获取指定类型对应的所有Bean，包括子类
	 *
	 * @param <T>  Bean类型
	 * @param type 类、接口，null表示获取所有bean
	 * @return 类型对应的bean，key是bean注册的name，value是Bean
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 获取指定类型对应的Bean名称，包括子类
	 *
	 * @param type 类、接口，null表示获取所有bean名称
	 * @return bean名称
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key 配置项key
	 * @return 属性值
	 */
	public static String getProperty(String key) {
		if(null == applicationContext){
			return null;
		}
		return applicationContext.getEnvironment().getProperty(key);
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		if(null == applicationContext){
			return null;
		}
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 *
	 * @return 当前的环境配置
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 动态向Spring注册Bean
	 * <p>
	 * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开放API
	 *
	 * @param <T>      Bean类型
	 * @param beanName 名称
	 * @param bean     bean
	 * @author shadow
	 */
	public static <T> void registerBean(String beanName, T bean) {
		if(null != beanFactory){
			beanFactory.registerSingleton(beanName, bean);
		} else if(applicationContext instanceof ConfigurableApplicationContext){
			ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
			context.getBeanFactory().registerSingleton(beanName, bean);
		}
	}
}