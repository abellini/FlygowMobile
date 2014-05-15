package br.com.flygowmobile.entity;

import java.io.Serializable;
import java.util.Date;

import br.com.flygowmobile.database.RepositoryAttendant;

public class Attendant implements Serializable {

    private int attendantId;
    private String name;
    private String lastName;
    private String address;
    private Date birthDate;
    private String photo;
    private String login;
    private String password;
    private String email;

    public static String[] columns = new String[] {
            RepositoryAttendant.Attendants.COLUMN_NAME_ATTENDANT_ID,
            RepositoryAttendant.Attendants.COLUMN_NAME_NAME,
            RepositoryAttendant.Attendants.COLUMN_NAME_LAST_NAME,
            RepositoryAttendant.Attendants.COLUMN_NAME_ADDRESS,
            RepositoryAttendant.Attendants.COLUMN_NAME_BIRTH_DATE,
            RepositoryAttendant.Attendants.COLUMN_NAME_PHOTO,
            RepositoryAttendant.Attendants.COLUMN_NAME_LOGIN,
            RepositoryAttendant.Attendants.COLUMN_NAME_PASSWORD,
            RepositoryAttendant.Attendants.COLUMN_NAME_EMAIL
    };

    public Attendant() {}

    public Attendant(int attendantId, String name, String lastName, String address, Date birthDate, String photoName, String login, String password, String email) {
        this.setAttendantId(attendantId);
        this.setName(name);
        this.setLastName(lastName);
        this.setAddress(address);
        this.setBirthDate(birthDate);
        this.setPhoto(photoName);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
    }

    public int getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJSON() {
        return "{" +
                "\"attendantId\": "+ getAttendantId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"lastName\": " + getLastName() + ", " +
                "\"address\": " + getAddress() + ", " +
                "\"birthDate\": " + getBirthDate() + ", " +
                "\"photo\": " + getPhoto() + ", " +
                "\"login\": " + getLogin() + ", " +
                "\"password\": " + getPassword() + ", " +
                "\"email\": " + "\"" + getEmail() +
                "}";
    }
}
