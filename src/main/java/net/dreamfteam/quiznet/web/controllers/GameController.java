package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.service.GameSessionService;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import net.dreamfteam.quiznet.web.validators.GameSessionValidator;
import net.dreamfteam.quiznet.web.validators.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(Constants.GAME_URLS)
public class GameController {

    private final GameService gameService;
    private final GameSessionService gameSessionService;
    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public GameController(GameService gameService, GameSessionService gameSessionService,
                          IAuthenticationFacade authenticationFacade) {
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody DtoGame dtoGame) throws ValidationException {
        GameValidator.validate(dtoGame);

        return new ResponseEntity<>(
                gameService.createGame(dtoGame, authenticationFacade.getUserId()),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit")
    public ResponseEntity<?> updateGame(@RequestBody DtoGame dtoGame) throws ValidationException {
        GameValidator.validateUpdate(dtoGame);

        gameService.updateGame(dtoGame);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable String gameId) {
        return new ResponseEntity<>(gameService.getGameById(gameId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam String gameId) {
        gameService.startGame(gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/questions/get")
    public ResponseEntity<?> getQuestions(@RequestParam String sessionId) {
        return new ResponseEntity<>(gameService.getQuestion(sessionId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/join/{accessId}")
    public ResponseEntity<?> joinGame(@PathVariable String accessId){
        return new ResponseEntity<>(gameSessionService.joinGame(accessId,authenticationFacade.getUserId()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/result")
    public ResponseEntity<?> setResult(@RequestBody DtoGameSession dtoGameSession){
        GameSessionValidator.validate(dtoGameSession);

        gameSessionService.setResult(dtoGameSession);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sessions/{gameId}")
    public ResponseEntity<?> getSessions(@PathVariable String gameId){
        return new ResponseEntity<>(gameSessionService.getSessions(gameId),HttpStatus.OK);
    }
}
