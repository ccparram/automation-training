package com.globant.automation.trainings.servicetesting.orm;

import com.globant.automation.trainings.servicetesting.models.MyPojo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import java.util.Date;

/**
 * @author Juan Krzemien
 */
public class OrmStandAlone {

    private static final SessionFactory sessionFactory = new Configuration()
            .addAnnotatedClass(MyPojo.class)
            .buildSessionFactory();

    private static final ThreadLocal<Session> sessions = ThreadLocal.withInitial(sessionFactory::openSession);

    @Test
    public void testSimple2() throws Exception {
        Session session = sessions.get();
        Transaction tx = session.beginTransaction();
        session.save(new MyPojo("Ruso", new Date()));
        session.flush();
        tx.commit();
        session.close();
        sessions.remove();
    }

}
