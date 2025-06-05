package service;

import dao.UserDAO;
import exception.EntityNotFoundException;
import model.User;
import model.RegularUser;
import model.PremiumUser;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Adaugă un utilizator (regular sau premium)
    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    // Returnează toți utilizatorii
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    // Caută user după ID
    public User getUserById(int id) throws SQLException {
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        return user;
    }

    // Actualizează user (după ID)
    public void updateUser(User user, int id) throws SQLException {
        // Verifică dacă userul există (aruncă excepție dacă nu există)
        getUserById(id);
        userDAO.updateUser(user, id);
    }

    // Șterge user după ID
    public void deleteUser(int id) throws SQLException {
        // Verifică dacă userul există (aruncă excepție dacă nu există)
        getUserById(id);
        userDAO.deleteUser(id);
    }

    // Găsește user după nume (nume unic presupus)
    public User getUserByName(String name) throws SQLException {
        List<User> users = userDAO.getAllUsers();
        return users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User", name));
    }

    // Metodă suplimentară: Creare user regular
    public RegularUser createRegularUser(String name, String address, double balance) throws SQLException {
        RegularUser user = new RegularUser(name, address, balance);
        addUser(user);
        return user;
    }

    // Metodă suplimentară: Creare user premium
    public PremiumUser createPremiumUser(String name, String address, double balance, double discount) throws SQLException {
        PremiumUser user = new PremiumUser(name, address, balance, discount);
        addUser(user);
        return user;
    }
}