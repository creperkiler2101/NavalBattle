package game.database.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "rang")
@Table(name = "rang")
public class Rank {
    private int id;
    private String rangname;
    private String image;
    private int experience;

    @Id
    @GenericGenerator(name="hilogen", strategy="increment")
    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getRangname() {
        return rangname;
    }
    public void setRangname(String rangname) {
        this.rangname = rangname;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "player{" +
                "id=" + id +
                ", rangname='" + rangname + '\'' +
                ", image='" + image + '\'' +
                ", experience=" + experience + "}";
    }
}
