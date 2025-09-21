package com.ORM_JPA_and_Hibernate.hibernate_framework.util;

import com.ORM_JPA_and_Hibernate.hibernate_framework.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory getConnection()
    {
        Configuration configuration = new Configuration();
        configuration.configure("/resource/hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);

        SessionFactory sessionFactory= configuration.buildSessionFactory();
        return sessionFactory;
    }
}
