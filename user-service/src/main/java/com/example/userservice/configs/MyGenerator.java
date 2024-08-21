package com.example.userservice.configs;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
//import org.hibernate.exceptions.spi.Configurable;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class MyGenerator implements IdentifierGenerator, Configurable {

    String prefix = "ord";
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        String query = String.format("select %s from %s",
                session.getEntityPersister(o.getClass().getName(), o)
                        .getIdentifierPropertyName(),
                o.getClass().getSimpleName());
        Stream ids = session.createQuery(query).stream();
        Long max = ids.map(ob -> ob.toString().replace(prefix + "-", ""))
                .mapToLong(value -> Long.parseLong(value.toString()))
                .max()
                .orElse(0L);
        max +=1;
        return prefix + "-" + System.currentTimeMillis();
    }

//    @Override
    public void configure(Properties properties) throws HibernateException {
        prefix = properties.getProperty("prefix");
    }
}
