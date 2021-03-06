package org.tbwork.anole.spring.hotmod.monitor;

import org.tbwork.anole.loader.core.manager.monitor.processor.AnoleOutgoConfigUpdatePostProcessor;
import org.tbwork.anole.spring.hotmod.reflection.manager.BeanAutowiredValuePointManager;
import org.tbwork.anole.spring.hotmod.reflection.manager.impl.AnoleSpringBeanAutowiredValuePointManager;
import org.tbwork.anole.spring.statics.AnoleSpringNameBook;

/**
 * Work as a monitor to listen the change of config item.
 */
public class SpringAnoleOutgoConfigUpdatePostProcessor implements AnoleOutgoConfigUpdatePostProcessor {

    private BeanAutowiredValuePointManager beanAutowiredValuePointManager = AnoleSpringBeanAutowiredValuePointManager.instance;

    @Override
    public void process(String key, String newValue, long updateTime) {
        if(key.trim().toLowerCase().startsWith(AnoleSpringNameBook.FILED_ANOLE_KEY_PREFIX)){
            beanAutowiredValuePointManager.updateValue(key, newValue);
        }
    }
}
