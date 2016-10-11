package com.theironyard.controllers;

import com.theironyard.entities.Game;
import com.theironyard.services.GameRepository;
import com.theironyard.entities.User;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class GameTrackerContoller {

    @Autowired
    GameRepository games;

    @Autowired
    UserRepository users;

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException{     // this just puts a user in the database at the start for debugging
        if (users.count() == 0){
            User user = new User();
            user.setName("John");
            user.setPassword("pass");
            users.save(user);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByName(userName);
        if (user == null) {
            user = new User(userName, password);
            users.save(user);
        } else if (!user.isValid(password)){
            throw new Exception("Invalid Password");
        }
        session.setAttribute("userName", userName);

        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model, String genre, Integer releaseYear, String search) {
        String userName = (String) session.getAttribute("userName"); //gets from the session that was saved at login
        User user = users.findFirstByName(userName);
        if (user != null) {        // need to protect against the first time a user loads the page
            model.addAttribute("user", user);
        }

        List<Game> gameList;
        if (genre != null) {
            gameList = (ArrayList) games.findByGenre(genre);
        } else if (search != null) {
            gameList = (ArrayList) games.findByNameStartsWith(search);
        } else if (releaseYear != null) {
            gameList = (ArrayList) games.findByReleaseYear(releaseYear);
        } else if (user != null) {
            gameList = (ArrayList) games.findByUser(user); //this else somehow only shows items by the user
        } else {
            gameList = (ArrayList) games.findAll();
        }
        model.addAttribute("games", gameList);
        return "home";
    }

    @RequestMapping(path = "/add-game", method = RequestMethod.POST)
    public String addGame(HttpSession session, String gameName, String gamePlatform, String gameGenre, int gameYear) { //has to match html, not class variables
        String userName = (String) session.getAttribute("userName");
        User user = users.findFirstByName(userName);

        Game game = new Game(gameName, gamePlatform, gameGenre, gameYear, user);
        games.save(game);
        return "redirect:/";
    }



}
