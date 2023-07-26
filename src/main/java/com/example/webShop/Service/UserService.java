package com.example.webShop.Service;

import com.example.webShop.database.User;
import com.example.webShop.database.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public void registerUser(String email, String password1, String password2) throws UserException {
        if (!password1.equals(password2)) {
            throw new UserException("parolele nu sunt identice");
        }
        User user = new User();
        user.setPassword(password1);
        user.setEmail(email);
        userDAO.save(user);
    }

    public List<User> loginUser(String email, String password) throws UserException {
        List<User> userList = userDAO.findAllByEmail(email);


        //List<USer > = List<OBject>

        if (userList.isEmpty()) {
            throw new UserException("user/parola incorecte");
        }

        //User user = userList.get(0);

        if (!userList.get(0).getPassword().equals(password)) {
            throw new UserException("user/parola incorecte");
        }
        return userList;
    }
}
