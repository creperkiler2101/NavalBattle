package game.database;

import com.fasterxml.classmate.AnnotationConfiguration;
import game.database.models.Game;
import game.database.models.Player;
import game.database.models.Rank;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.xml.crypto.Data;
import java.io.InputStream;
import java.util.ArrayList;
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
        Player p = null;

        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM player");
            List<Player> list = (List<Player>)query.list();

            for (int i = 0; i < list.size(); i++) {
                Player p_ = list.get(i);
                if (p_.getNickname().equals(nickname)) {
                    p = p_;
                    break;
                }
            }
        }

        return p != null;
    }

    public static Game getGame(int id) {
        Game p = null;
        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM game");
            List<Game> list = (List<Game>)query.list();


            for (int i = 0; i < list.size(); i++) {
                Game p_ = list.get(i);
                if (p_.getId() == id) {
                    p = p_;
                    break;
                }
            }
        }

        return p;
    }

    public static ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<>();
        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM game");
            List<Game> list = (List<Game>)query.list();

            for (int i = 0; i < list.size(); i++) {
                Game p_ = list.get(i);
                games.add(p_);
            }
        }

        return games;
    }

    public static ArrayList<Game> getGames(String nickname) {
        ArrayList<Game> games = new ArrayList<>();
        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM game");
            List<Game> list = (List<Game>)query.list();

            for (int i = 0; i < list.size(); i++) {
                Game p_ = list.get(i);
                if (p_.getPlayerOne().equals(nickname) || p_.getPlayerTwo().equals(nickname)) {
                    games.add(p_);
                }
            }
        }

        return games;
    }

    public static Player getPlayer(String nickname) {
        Player p = null;
        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM player");
            List<Player> list = (List<Player>)query.list();


            for (int i = 0; i < list.size(); i++) {
                Player p_ = list.get(i);
                if (p_.getNickname().equals(nickname)) {
                    p = p_;
                    break;
                }
            }
        }

        return p;
    }

    public static Rank getRank(Player p) {
        Rank r = null;

        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM rang");
            List<Rank> list = (List<Rank>)query.list();

            r = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                Rank rank = list.get(i);
                if (p.getExperience() >= rank.getExperience())
                    r = rank;
            }
        }

        return r;
    }

    public static Rank getNextRank(Player p) {
        Rank r = null;

        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM rang");
            List<Rank> list = (List<Rank>)query.list();

            r = list.get(0);
            for (int i = 0; i < list.size(); i++) {
                Rank rank = list.get(i);
                if (p.getExperience() >= rank.getExperience()) {
                    if (i != list.size() - 1)
                        r = list.get(i + 1);
                    else
                        r = rank;
                }
            }
        }

        return r;
    }

    public static boolean logIn(String nickname, String password) {
        Player p = null;
        try (Session session = getSession()) {
            //session.beginTransaction();

            Query query = session.createQuery("FROM player");
            List<Player> list = (List<Player>)query.list();

            for (int i = 0; i < list.size(); i++) {
                Player p_ = list.get(i);
                if (p_.getNickname().equals(nickname)) {
                    if (p_.getPassword().equals(password)) {
                        p = p_;
                        break;
                    }
                }
            }
        }

        return p != null;
    }
}
