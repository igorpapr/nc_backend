package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.service.GameSessionService;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import net.dreamfteam.quiznet.web.validators.GameSessionValidator;
import net.dreamfteam.quiznet.web.validators.GameValidator;
import net.dreamfteam.quiznet.web.validators.UserQuizRatingValidator;
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
    private final SseService sseService;
    private final QuizService quizService;

    @Autowired
    public GameController(GameService gameService,
                          GameSessionService gameSessionService,
                          IAuthenticationFacade authenticationFacade,
                          SseService sseService,
                          QuizService quizService) {
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
        this.authenticationFacade = authenticationFacade;
        this.sseService = sseService;
        this.quizService = quizService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody DtoGame dtoGame) throws ValidationException {
        GameValidator.validate(dtoGame);

        return new ResponseEntity<>(
                gameService.createGame(dtoGame,
                        authenticationFacade.getUserId(), authenticationFacade.getUsername()),
                HttpStatus.CREATED);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable String gameId) {
        return new ResponseEntity<>(gameService.getGameById(gameId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam String gameId) {
        gameService.startGame(gameId);
        sseService.send(gameId, "start");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/questions/get")
    public ResponseEntity<?> getQuestions(@RequestParam String sessionId) {
        return new ResponseEntity<>(gameService.getQuestion(sessionId), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @GetMapping("/join/{accessId}")
    public ResponseEntity<?> joinGame(@PathVariable String accessId) {
        return new ResponseEntity<>(
                gameSessionService.joinGame(accessId,
                        authenticationFacade.getUserId(),
                        authenticationFacade.getUsername()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @PostMapping("/result")
    public ResponseEntity<?> setResult(@RequestBody DtoGameSession dtoGameSession) {
        GameSessionValidator.validate(dtoGameSession);
        gameSessionService.setResult(dtoGameSession);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @GetMapping("/sessions/{gameId}")
    public ResponseEntity<?> getSessions(@PathVariable String gameId) {
        return new ResponseEntity<>(gameSessionService.getSessions(gameId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @PostMapping("/game/{gameId}/ready")
    public ResponseEntity<?> setReady(@PathVariable String gameId, @RequestParam String sessionId) {
        sseService.send(gameId, "ready", sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ANONYM')")
    @DeleteMapping("/remove/{sessionId}")
    public ResponseEntity<?> remove(@PathVariable String sessionId) {
        gameSessionService.removePlayer(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/rate-quiz")
    public ResponseEntity<?> rateQuiz(@RequestParam String sessionId, @RequestParam int ratingPoints) {
        UserQuizRatingValidator.validate(sessionId, ratingPoints);
        gameService.rateGame(sessionId, ratingPoints, authenticationFacade.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
