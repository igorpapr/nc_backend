package net.dreamfteam.quiznet.web.controllers;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ImageService;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;
import net.dreamfteam.quiznet.web.validators.QuizValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(Constants.QUIZ_URLS)
public class QuizController {
    private QuizService quizService;
    private UserService userService;
    private ImageService imageService;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public QuizController(QuizService quizService, UserService userService, ImageService imageService, IAuthenticationFacade authenticationFacade) {
        this.quizService = quizService;
        this.userService = userService;
        this.imageService = imageService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        QuizValidator.validate(dtoQuiz);
        return new ResponseEntity<>(quizService.saveQuiz(dtoQuiz, authenticationFacade.getUserId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        QuizValidator.validateForEdit(dtoQuiz);
        return new ResponseEntity<>(quizService.updateQuiz(dtoQuiz), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {

        Quiz quiz = quizService.getQuiz(dtoQuiz.getQuizId());
        User currentUser = userService.getById(authenticationFacade.getUserId());

        if (quiz == null) {
            throw new ValidationException("Quiz not found");
        }

        if (quiz.getCreatorId().equals(authenticationFacade.getUserId()) || currentUser.getRole() != Role.ROLE_USER) {
            throw new ValidationException("You can't delete not yours quiz");
        }

        quizService.deleteQuizById(dtoQuiz);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/quiz-image")
    public ResponseEntity<?> uploadQuizImage(@RequestParam("img") MultipartFile image, @RequestParam("quizId") String quizId) throws ValidationException {
        quizService.addQuizImage(imageService.saveImage(image), quizId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/question-image")
    public ResponseEntity<?> uploadQuestionImage(@RequestParam("img") MultipartFile image, @RequestParam("questionId") String questionId) throws ValidationException {
        quizService.addQuestionImage(imageService.saveImage(image), questionId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/filter-quiz-list")
    public ResponseEntity<?> getFilteredQuizList(@RequestBody DtoQuizFilter dtoQuizFilter) throws ValidationException {
        return new ResponseEntity<>(quizService.findQuizzesByFilter(dtoQuizFilter), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/question")
    public ResponseEntity<?> createQuestion(@RequestBody Question question) throws ValidationException {
        QuizValidator.validateQuestion(question);
        return new ResponseEntity<>(quizService.saveQuestion(question), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit/question")
    public ResponseEntity<?> editQuestion(@RequestBody Question question) throws ValidationException {
        QuizValidator.validateQuestion(question);
        return new ResponseEntity<>(quizService.updateQuestion(question), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/question")
    public ResponseEntity<?> deleteQuestion(@RequestBody Question question) throws ValidationException {
        quizService.deleteQuestion(question);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/markasfavourite")
    public ResponseEntity<?> setAsFavourite(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        quizService.markAsFavourite(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/markaspublished")
    public ResponseEntity<?> setAsPublished(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        quizService.markAsPublished(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        Quiz quiz = quizService.getQuiz(dtoQuiz.getQuizId());
        User currentUser = userService.getById(authenticationFacade.getUserId());

        if (quiz == null) {
            throw new ValidationException("Quiz not found");
        }

        if (quiz.getCreatorId().equals(authenticationFacade.getUserId()) || currentUser.getRole() != Role.ROLE_USER) {
            throw new ValidationException("You can't deactivate not yours quiz");
        }

        quizService.deactivateQuiz(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/validate")
    public ResponseEntity<?> validateQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {

        quizService.validateQuiz(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getuserquizlist")
    public ResponseEntity<?> getUserQuizList() throws ValidationException {

        boolean b = authenticationFacade == null;
        log.info("Some " + b);
        log.info("starting");

        return new ResponseEntity<>(quizService.getUserQuizList(authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/quiz-list-invalid/{page}")
    public ResponseEntity<?> getInvalidQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getInvalidQuizzes((page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }

    @GetMapping("/getquiztotalsize")
    public ResponseEntity<?> getQuizTotalSize() throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzesTotalSize(), HttpStatus.OK);
    }

    @GetMapping("/getquestionlist")
    public ResponseEntity<?> getQuestionList(@RequestParam String quizId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuestionList(quizId), HttpStatus.OK);
    }

    @GetMapping("/gettaglist")
    public ResponseEntity<?> getTagList() throws ValidationException {
        return new ResponseEntity<>(quizService.getTagList(), HttpStatus.OK);
    }

    @GetMapping("/getcateglist")
    public ResponseEntity<?> getCategoryList() throws ValidationException {
        return new ResponseEntity<>(quizService.getCategoryList(), HttpStatus.OK);
    }

    @GetMapping("/quiz-list/page/{page}")
    public ResponseEntity<?> getQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzes((page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getQuiz(@RequestParam String quizId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuiz(quizId), HttpStatus.OK);
    }
//
//    @GetMapping("/getshortlist")
//    public ResponseEntity<?> getShortListOfQuizzes() throws ValidationException {
//        return new ResponseEntity<>(quizService.shortListOfQuizzes(), HttpStatus.OK);
//    }

    @GetMapping("getme")
    public ResponseEntity<?> getMe() {
        String userId = authenticationFacade.getUserId();

        return new ResponseEntity<>(userService.getById(userId), HttpStatus.OK);
    }

}
