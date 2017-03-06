package bachelorproject.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;

import java.io.Serializable;

@Interceptor
@Loggable
public class LoggingInterceptor{

	private static final Logger logger = Logger.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logMethod(InvocationContext ic) throws Exception {
    	logger.info("Entering: " + ic.getTarget().getClass().getName() +  " method: "+ ic.getMethod().getName());
        try {
            return ic.proceed();
        } finally {
        	logger.info("Exiting: " + ic.getTarget().getClass().getName() +  " method: "+ ic.getMethod().getName());
        }
    }
}
