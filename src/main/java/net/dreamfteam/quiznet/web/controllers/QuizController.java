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

    @PostMapping("/get")
    public ResponseEntity<?> getQuestionList(@RequestBody DtoQuiz dtoQuiz) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuiz(dtoQuiz), HttpStatus.OK);
    }

    @PostMapping("/create/question")
    public ResponseEntity<?> createQuestion(@RequestBody Question question) throws ValidationException {
        QuizValidator.validateQuestion(question);
        return new ResponseEntity<>(quizService.saveQuestion(question), HttpStatus.CREATED);
    }

    @DeleteMapping("/edit/question")
    public ResponseEntity<?> deleteQuestion(@RequestBody Question question) throws ValidationException {
        quizService.deleteQuestion(question);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/getquestionlist")
    public ResponseEntity<?> getQuestionList(@RequestBody Question question) throws ValidationException {
        return new ResponseEntity<>(quizService.getQuestionList(question), HttpStatus.OK);
    }
}
