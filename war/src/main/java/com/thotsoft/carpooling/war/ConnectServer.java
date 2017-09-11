package com.thotsoft.carpooling.war;

import com.thotsoft.carpooling.api.CarpoolingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ConnectServer<T extends CarpoolingService> implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectServer.class);
    private String host;
    private int port;
    private String className;
    private InitialContext remotingContext;

    public ConnectServer(String host, int port, String className) {
        this.host = host;
        this.port = port;
        this.className = className;
    }

    public ConnectServer(String host, int port, Class clazz) {
        this.host = host;
        this.port = port;
        this.className = clazz.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    public T connect() throws NamingException {
        remotingContext = createRemoteEjbContext(host, port);

        // Syntax: ejb:${appName}/${moduleName}/${beanName}!${remoteView}
        // appName = name of EAR deployment (or empty for single EJB/WAR
        // deployments)
        // moduleName = name of EJB/WAR deployment
        // beanName = name of the EJB (Simple name of EJB class)
        // remoteView = fully qualified remote interface class
        String ejbUrl = "war-1.0/" + className + "Impl!com.thotsoft.carpooling.api." + className;
        return (T) remotingContext.lookup(ejbUrl);
    }

    private InitialContext createRemoteEjbContext(String host, int port) throws NamingException {
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProps.put("jboss.naming.client.ejb.context", true);

        jndiProps.put(Context.PROVIDER_URL, "http-remoting://" + host + ":" + port);
        return new InitialContext(jndiProps);
    }

    @Override
    public void close() {
        if (remotingContext != null) {
            try {
                remotingContext.close();
            } catch (NamingException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
