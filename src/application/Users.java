package application;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Users {
    Map<String,User> storage = new HashMap<>();

    public Users(){}

    public Users(User testUser) {
        register(testUser);
    }

    public void register(User testUser) {
        if(isNull(storage.get(testUser.userName()))) {
            storage.put(testUser.userName(), testUser);

        }else{
            throw new DuplicateUserNameException();
        }


    }

    public User getByUserName(String userName) {
        return storage.get(userName);
    }

    public User login(String userName, String password) {
        User foundUser = getByUserName(userName);
        if(nonNull(foundUser)){
            if(foundUser.password().equals(password)){
                foundUser.loggedIn(true);
                return foundUser;
            }
        }
        throw new UserLoginException();
    }

    public void logout(User testUser) {
        User foundUser = getByUserName(testUser.userName());
        if(nonNull(foundUser)){
            foundUser.loggedIn(false);
        }
    }

    public void annointSeller(String username){
        User byUserName = getByUserName(username);
        if( nonNull(byUserName)){
            byUserName.seller(true);
        }
    }
}
