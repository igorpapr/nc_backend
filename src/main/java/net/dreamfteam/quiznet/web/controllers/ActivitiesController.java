package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.service.ImageService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoAdminActivation;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(Constants.ACTIVITIES_URLS)
public class ActivitiesController {

	final private ActivitiesService activitiesService;
	final private IAuthenticationFacade authenticationFacade;

	@Autowired
	public ActivitiesController(ActivitiesService activitiesService, IAuthenticationFacade authenticationFacade) {
		this.activitiesService = activitiesService;
		this.authenticationFacade = authenticationFacade;
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ResponseEntity<?> getFriendsActivities() {
		return new ResponseEntity<>(activitiesService.getFriendsActivities(authenticationFacade.getUserId())
				,HttpStatus.OK);
	}


}
