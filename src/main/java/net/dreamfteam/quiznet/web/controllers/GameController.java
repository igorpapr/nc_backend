package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.validators.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(Constants.GAME_URLS)
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody DtoGame dtoGame) throws ValidationException {
        GameValidator.validate(dtoGame);

        return new ResponseEntity<>(gameService.createGame(dtoGame), HttpStatus.CREATED);
    }

    @GetMapping("/{accessId}")
    public ResponseEntity<?> getValidQuizList(@PathVariable String accessId) {
        return new ResponseEntity<>(gameService.getGameByAccessId(accessId), HttpStatus.OK);
    }

//    @PostMapping("start")
//    public ResponseEntity<?> startGame(@RequestParam String gameId) {
//        gameService.startGame(gameId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/start")
    public ResponseEntity<?> getUserQuizList(@RequestParam String gameId){
        gameService.startGame(gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
