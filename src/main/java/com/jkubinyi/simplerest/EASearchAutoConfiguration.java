package com.jkubinyi.simplerest;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jkubinyi.elasticappender.L4JElasticAppender;
import com.jkubinyi.elasticappender.search.EASearch;

@Configuration
public class EASearchAutoConfiguration {
	
	/**
	 * Will autoconfigure {@link EASearch} from the first configured {@link L4JElasticAppender}
	 * in Log4J configuration.
	 * 
	 * @return Prepared {@link EASearch} instance ready to be searched on.
	 */
	@Bean(destroyMethod = "close")
	public EASearch createEASearch() {
		org.apache.logging.log4j.core.LoggerContext logContext = (org.apache.logging.log4j.core.LoggerContext) LogManager
	            .getContext(false);
		
	    Collection<LoggerConfig> configurations = logContext.getConfiguration().getLoggers().values();
	    for(LoggerConfig configuration : configurations) {
	    	Collection<Appender> appenders = configuration.getAppenders().values();
	    	for(Appender appender : appenders) {
	    		if(appender.getClass() == L4JElasticAppender.class) {
	    			return EASearch.from((L4JElasticAppender) appender);
	    		}
	    	}
	    }
	    
		return null;
	}
}
