package app;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import model.User;
import model.PremiumUser;
import model.RegularUser;

class FoodDeliveryPlatform {
    public static void main(String[] args) {

        List<User> users = new ArrayList<>();
        users.add(new PremiumUser("Maria Ionescu", "Bulevardul Libertatii 25", 150.0, 0.15));
        users.add(new RegularUser("Ion Popescu", "Strada Principala 10", 100.0));
        users.add(new PremiumUser("Ana Maria", "Strada Florilor 5", 200.0, 0.10));
        users.add(new RegularUser("George Vasilescu", "Bulevardul Unirii 15", 50.0));

        for (User user : users) {
            System.out.println(user.getName());
        }
        System.out.println();

        users.sort(Comparator.comparingDouble(User::getBalance));

        // Print sorted users
        for (User user : users) {
            System.out.println(user.getName() + "  " + user.getBalance());
        }
    }
}