package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.validators.QuizValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping(Constants.QUIZ_URLS)
public class QuizController {
    private QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        QuizValidator.validate(dtoQuiz);
        return new ResponseEntity<>(quizService.saveQuiz(dtoQuiz), HttpStatus.CREATED);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        QuizValidator.validateForEdit(dtoQuiz);
        return new ResponseEntity<>(quizService.updateQuiz(dtoQuiz), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getQuiz(@RequestParam String quizId, @RequestParam String userId) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuiz(quizId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        if (isNull(quizService.getQuiz(dtoQuiz.getQuizId(), ""))) {
            return ResponseEntity.notFound().build();
        }
        quizService.deleteQuizById(dtoQuiz);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getuserquizlist")
    public ResponseEntity<?> getUserQuizList(@RequestParam String userId) throws ValidationException {
        return new ResponseEntity<>(quizService.getUserQuizList(userId), HttpStatus.OK);
    }

    @GetMapping("/quiz-list/{page}")
    public ResponseEntity<?> getQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzes((page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }

    @GetMapping("/quiz-list")
    public ResponseEntity<?> getQuizList(@RequestParam int startIndex, @RequestParam int amount) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzes(startIndex, amount), HttpStatus.OK);
    }

    @GetMapping("/quiz-list-invalid/{page}")
    public ResponseEntity<?> getInvalidQuizList(@PathVariable int page) throws ValidationException {
        return new ResponseEntity<>(quizService.getInvalidQuizzes((page - 1) * Constants.AMOUNT_QUIZ_ON_PAGE, Constants.AMOUNT_QUIZ_ON_PAGE), HttpStatus.OK);
    }


    @PostMapping("/create/question")
    public ResponseEntity<?> createQuestion(@RequestBody Question question) throws ValidationException {
        QuizValidator.validateQuestion(question);
        return new ResponseEntity<>(quizService.saveQuestion(question), HttpStatus.CREATED);
    }

    @PostMapping("/edit/question")
    public ResponseEntity<?> editQuestion(@RequestBody Question question) throws ValidationException {
        QuizValidator.validateQuestion(question);
        return new ResponseEntity<>(quizService.updateQuestion(question), HttpStatus.OK);
    }

    @DeleteMapping("/delete/question")
    public ResponseEntity<?> deleteQuestion(@RequestBody Question question) throws ValidationException {
        quizService.deleteQuestion(question);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @PostMapping("/markasfavourite")
    public ResponseEntity<?> setAsFavourite(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        quizService.markAsFavourite(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/markaspublished")
    public ResponseEntity<?> setAsPublished(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        quizService.markAsPublished(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        if (isNull(quizService.getQuiz(dtoQuiz.getQuizId(), ""))) {
            return ResponseEntity.notFound().build();
        }
        quizService.deactivateQuiz(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateQuiz(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {

        //TODO Add role verification (Only admins & moderators allowed)
        if (isNull(quizService.getQuiz(dtoQuiz.getQuizId(), dtoQuiz.getUserId()))) {
            return ResponseEntity.notFound().build();
        }
        quizService.validateQuiz(dtoQuiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getquiztotalsize")
    public ResponseEntity<?> getQuizTotalSize() throws ValidationException {
        return new ResponseEntity<>(quizService.getQuizzesTotalSize(), HttpStatus.OK);
    }
}
