package api.controllers;

import api.interfaces.IAccountController;
import api.misc.EmptyJsonResponse;
import api.repositories.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restmodels.RESTPlayer;

import java.util.HashSet;

@RestController
public class AccountController implements IAccountController {

    private PlayerRepository playerRepository;
    private HashSet<String> activePlayers = new HashSet<>();

    public AccountController() {
        playerRepository = new PlayerRepository();
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    @PostMapping("/account/register")
    public Object register(@RequestParam String username, String password) {

        if (username == null || password == null) {
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (playerRepository.getPlayerByUsername(username) != null)
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.CONFLICT);

        return playerRepository.registerPlayer(new RESTPlayer(0, username, password));
    }

    @PostMapping("/account/login")
    public Object login(@RequestParam String username, String password) {

        if (username == null || password == null) {
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_ACCEPTABLE);
        }

        RESTPlayer storedPlayer = playerRepository.signInPlayer(new RESTPlayer(0, username, password));
        if (storedPlayer == null) {
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_ACCEPTABLE);
        }

        if(activePlayers.contains(storedPlayer.getUsername())) {
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.CONFLICT);
        }
        activePlayers.add(storedPlayer.getUsername());
        return storedPlayer;
    }

    @GetMapping("/account/logout")
    public Object signOut(@RequestParam String username) {
        if(username == null)
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_ACCEPTABLE);

        if(activePlayers.contains(username)) {
            activePlayers.remove(username);
            return true;
        }

        return false;
    }
}
