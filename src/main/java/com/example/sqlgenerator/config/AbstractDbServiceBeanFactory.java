package com.example.sqlgenerator.config;

import com.example.sqlgenerator.enums.SupportedDbType;
import com.example.sqlgenerator.services.db.AbstractDbService;
import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AbstractDbServiceBeanFactory implements ApplicationContextAware, InitializingBean {

    protected Map<SupportedDbType, AbstractDbService> typeBeanMap = new HashMap<>();
    private ApplicationContext applicationContext;
    private final ListableBeanFactory listableBeanFactory;

    public AbstractDbServiceBeanFactory(ListableBeanFactory listableBeanFactory) {
        this.listableBeanFactory = listableBeanFactory;
    }

    public AbstractDbService getAbstractDbServiceBean(SupportedDbType supportedDbType) {
        return typeBeanMap.get(supportedDbType);
    }

    @Override
    public void afterPropertiesSet() {
        initializeTypeBeanMap();
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initializeTypeBeanMap() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(AbstractDbServiceQualifier.class);

        for (String beanName : beansWithAnnotation.keySet()) {

            AbstractDbService abstractDbService = listableBeanFactory.getBean(beanName, AbstractDbService.class);
            AbstractDbServiceQualifier beanHandler = listableBeanFactory.findAnnotationOnBean(beanName, AbstractDbServiceQualifier.class);
            assert beanHandler != null;
            SupportedDbType supportedDbType = beanHandler.type();
            typeBeanMap.put(supportedDbType, abstractDbService);
        }
    }
}
