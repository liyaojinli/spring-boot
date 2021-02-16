
# springboot源码阅读

## SpringApplication init

```java

@SpringBootApplication
public class SpringBootApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationStart.class);
    }
}

```

```

public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		this.webApplicationType = WebApplicationType.deduceFromClasspath();
        /**
         * {@link ApplicationContextInitializer} 是spring组件spring-context组件中的一个接口，
         * 主要是spring ioc容器刷新之前的一个回调接口，用于处于自定义逻辑
         */
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
        /**
         * spring提供的事件监听机制
         * {@link ApplicationListener}
         * {@link ApplicationEvent}
         */
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
        LOGGER.info("webApplicationType->{}" + LINE_SEPARATOR +
                                "ApplicationContextInitializer->{}" + LINE_SEPARATOR +
                                "ApplicationListener->{}" + LINE_SEPARATOR +
                                "mainApplicationClass->{}",
                        this.webApplicationType,
                        this.getInitializers(),
                        this.getListeners(),
                        this.mainApplicationClass
                );
	}

```

SpringApplication初始化有以下核心逻辑

* 从src/main/resources/META-INF中读取spring.factories文件，分别初始化`ApplicationListener`,`ApplicationContextInitializer`
* `ApplicationListener`是spring中的事件监听逻辑
* `ApplicationContextInitializer`是spring中在进行applicationContext的refresh之前允许对applicationContext做一定改变的接口
* 根据上下文的classpath分析当前是servlet,reactive,none三种中的哪一种环境


## springApplication的run逻辑

```
/**
	 * Run the Spring application, creating and refreshing a new
	 * {@link ApplicationContext}.
	 * @param args the application arguments (usually passed from a Java main method)
	 * @return a running {@link ApplicationContext}
	 */
	public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
        // java.awt.headless=true表示是服务器程序，没有显示器，鼠标等外设，那么不能使用这些外设，比如生成一张图片就需要使用cpu计算得出
		configureHeadlessProperty();
        /**
         * springBoot提供的事件监听
         * {@link SpringApplicationRunListener}
         * {@link org.springframework.boot.context.event.EventPublishingRunListener} 这个类会发布事件
         * {@link org.springframework.boot.context.event.SpringApplicationEvent} 发布的事件类型就是这个类的子类
         */
		SpringApplicationRunListeners listeners = getRunListeners(args);
		listeners.starting();
		try {
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
			configureIgnoreBeanInfo(environment);
			Banner printedBanner = printBanner(environment);
			context = createApplicationContext();
			exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			prepareContext(context, environment, listeners, applicationArguments, printedBanner);
			refreshContext(context);
			afterRefresh(context, applicationArguments);
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
			}
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}

```

* `SpringApplicationRunListeners`事件监听，将springboot的事件发布到各个监听器

```
SpringApplicationRunListeners listeners = getRunListeners(args);
```

* 实现容器环境的配置文件读取，包含系统环境变量，jdk启动时指定的变量，以及application.yml中读取的配置等

```
ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments)
```

* 创建基于spring标准的applicationContext

```
context = createApplicationContext()
```

* 错误报告

```
exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
```
