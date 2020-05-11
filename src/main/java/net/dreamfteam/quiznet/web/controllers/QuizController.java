package net.dreamfteam.quiznet.web.controllers;

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
import net.dreamfteam.quiznet.web.dto.DtoEditQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;
import net.dreamfteam.quiznet.web.validators.QuizValidator;
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
import com.google.gson.*;
import java.io.IOException;
import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping(Constants.QUIZ_URLS)
public class QuizController {
    final private UserService userService;
    final private QuizService quizService;
    final private IAuthenticationFacade authenticationFacade;
    final private ImageService imageService;
    final private Gson gson;

    public QuizController(QuizService quizService, ImageService imageService, UserService userService, IAuthenticationFacade authenticationFacade) {
        this.quizService = quizService;
        this.userService = userService;
        this.imageService = imageService;
        this.authenticationFacade = authenticationFacade;
        this.gson = new Gson();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestParam("obj") String quiz,  @RequestParam(value = "img", required = false)  MultipartFile image) throws ValidationException, IOException {
        DtoQuiz dtoQuiz = gson.fromJson(quiz, DtoQuiz.class);
        QuizValidator.validate(dtoQuiz);
        Quiz resQuiz = quizService.saveQuiz(dtoQuiz, authenticationFacade.getUserId());
        if (image != null) {
            resQuiz.setImageRef(imageService.saveImage(image));
            quizService.addQuizImage(resQuiz.getImageRef(), resQuiz.getId());
            resQuiz.setImageContent(image.getBytes());
        }
        return new ResponseEntity<>(resQuiz, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editQuiz(@RequestParam("obj") String editquiz,  @RequestParam(value = "img", required = false)  MultipartFile image) throws ValidationException, IOException {
        DtoEditQuiz dtoEditQuiz = gson.fromJson(editquiz, DtoEditQuiz.class);
        QuizValidator.validateForEdit(dtoEditQuiz);
        Quiz resQuiz = quizService.updateQuiz(dtoEditQuiz);
        if(image != null) {
            resQuiz.setImageRef(imageService.saveImage(image));
            quizService.addQuizImage(resQuiz.getImageRef(), resQuiz.getId());
            resQuiz.setImageContent(image.getBytes());
        }
        return new ResponseEntity<>(resQuiz, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @DeleteMapping
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
    @PostMapping("/filter-quiz-list/page/{page}")
    public ResponseEntity<?> getFilteredQuizList(@PathVariable int page, @RequestBody DtoQuizFilter dtoQuizFilter) throws ValidationException {

        return new ResponseEntity<>(quizService.findQuizzesByFilter(dtoQuizFilter, (page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/questions")
    public ResponseEntity<?> createQuestion(@RequestParam("obj") String questionStr,  @RequestParam(value = "img", required = false)  MultipartFile image) throws ValidationException, IOException {
        Question question = gson.fromJson(questionStr, Question.class);
        QuizValidator.validateQuestion(question);
        Question resQuestion = quizService.saveQuestion(question);
        if(image != null) {
            resQuestion.setImage(imageService.saveImage(image));
            quizService.addQuestionImage(resQuestion.getImage(), resQuestion.getId());
            resQuestion.setImageContent(image.getBytes());
        }
        return new ResponseEntity<>(resQuestion, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/questions/edit")
    public ResponseEntity<?> editQuestion(@RequestParam("obj") String questionStr,  @RequestParam(value = "img", required = false)  MultipartFile image) throws ValidationException, IOException {
        Question question = gson.fromJson(questionStr, Question.class);
        QuizValidator.validateQuestion(question);
        Question resQuestion = quizService.updateQuestion(question);
        if(image != null) {
            resQuestion.setImage(imageService.saveImage(image));
            quizService.addQuestionImage(resQuestion.getImage(), resQuestion.getId());
            resQuestion.setImageContent(image.getBytes());
        }
        return new ResponseEntity<>(resQuestion, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/questions")
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

    @PreAuthorize("hasRole('USER')")
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
        if (isNull(quizService.getQuiz(dtoQuiz.getQuizId(), authenticationFacade.getUserId()))) {
            return ResponseEntity.notFound().build();
        }
        dtoQuiz.setValidator_id(authenticationFacade.getUserId());
        quizService.validateQuiz(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/user-list")
    public ResponseEntity<?> getUserQuizList(@RequestParam String userId) throws ValidationException {

        return new ResponseEntity<>(quizService.getUserQuizList(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/quiz-list-invalid/page/{page}")
    public ResponseEntity<?> getInvalidQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getInvalidQuizzes((page - 1) * Constants.AMOUNT_VALID_QUIZ_ON_PAGE, Constants.AMOUNT_VALID_QUIZ_ON_PAGE, authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/quiz-list-valid/page/{page}")
    public ResponseEntity<?> getValidQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getValidQuizzes((page - 1) * Constants.AMOUNT_VALID_QUIZ_ON_PAGE, Constants.AMOUNT_VALID_QUIZ_ON_PAGE, authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/totalsize")
    public ResponseEntity<?> getQuizTotalSize() throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzesTotalSize(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/invalidquiztotalsize")
    public ResponseEntity<?> getInvalidQuizTotalSize() throws ValidationException {
        return new ResponseEntity<>(quizService.getInvalidQuizzesTotalSize(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/validquiztotalsize")
    public ResponseEntity<?> getValidQuizTotalSize() throws ValidationException {
        return new ResponseEntity<>(quizService.getValidQuizzesTotalSize(authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestionList(@RequestParam String quizId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuestionList(quizId), HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTagList() throws ValidationException {
        return new ResponseEntity<>(quizService.getTagList(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategoryList() throws ValidationException {
        return new ResponseEntity<>(quizService.getCategoryList(), HttpStatus.OK);
    }

    @GetMapping("/quiz-list/page/{page}")
    public ResponseEntity<?> getQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzes((page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }

    @GetMapping("/short-list")
    public ResponseEntity<?> getShortQuizList() throws ValidationException {
        return new ResponseEntity<>(quizService.shortListOfQuizzes(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getQuiz(@RequestParam String quizId, @RequestParam(required = false) String userId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuiz(quizId, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/setvalidator")
    public ResponseEntity<?> setQuizValidator(@RequestBody DtoQuiz quizDto) throws ValidationException {
        return new ResponseEntity<>(quizService.setValidator(quizDto.getQuizId(), authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/{quizId}/questions/amount")
    public ResponseEntity<?> getQuestionsAmountInQuiz(@PathVariable String quizId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuestionsAmountInQuiz(quizId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/{quizId}/questions/page/{page}")
    public ResponseEntity<?> getQuestionsInPage(@PathVariable String quizId, @PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuestionsInPage((page - 1) * Constants.AMOUNT_QUESTIONS_ON_PAGE, Constants.AMOUNT_QUESTIONS_ON_PAGE, quizId), HttpStatus.OK);
    }

}
