package game.database;

import com.fasterxml.classmate.AnnotationConfiguration;
import game.database.models.Player;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.xml.crypto.Data;
import java.io.InputStream;
import java.util.List;

public class Database {
    private static SessionFactory ourSessionFactory;

    static {
        try {
            Configuration conf = new Configuration();
            conf.configure();

            ourSessionFactory = conf.buildSessionFactory();
        }
        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static boolean isPlayerExists(String nickname) {
        try (Session session = getSession()) {
            session.beginTransaction();

            Query query = session.createQuery("FROM player");
            List<Player> list = (List<Player>)query.list();

            Player p = null;
            for (int i = 0; i < list.size(); i++) {
                Player p_ = list.get(i);
                if (p_.getNickname().equals(nickname)) {
                    p = p_;
                    break;
                }
            }
            return p != null;
        }
    }
}
