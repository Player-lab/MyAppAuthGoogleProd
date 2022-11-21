package angers.bonneau.myappauthgoogle;

public class Courses {

    // variables pour stocker les données.
    private String NomCours, DescriptionCours, DureeCours,Name,Mail, Time;
    private Long Id;

    public Courses() {
        // Constructeur vide.
        // requis pour le firebase.
    }

    // Constructor for all variables.
    public Courses(String nomCours, String descriptionCours, String dureeCours, String name, String mail, String time, Long id) {
        this.NomCours = nomCours;
        this.DescriptionCours = descriptionCours;
        this.DureeCours = dureeCours;
        this.Name = name;
        this.Mail = mail;
        this.Time = time;
        this.Id = id;
    }

    // getter et setter permettent de récup et de set des data

    public String getNomCours() {
        return NomCours;
    }

    public void setNomCours(String nomCours) {
        NomCours = nomCours;
    }

    public String getDescriptionCours() {
        return DescriptionCours;
    }

    public void setDescriptionCours(String descriptionCours) {
        DescriptionCours = descriptionCours;
    }

    public String getDureeCours() {
        return DureeCours;
    }

    public void setDureeCours(String dureeCours) {
        DureeCours = dureeCours;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}