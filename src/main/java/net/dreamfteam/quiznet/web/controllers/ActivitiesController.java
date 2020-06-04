package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.service.ActivitiesService;
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
	public ResponseEntity<?> getFriendsActivities(@RequestParam int startIndex, @RequestParam int amount) {
		return new ResponseEntity<>(activitiesService
				.getFriendsActivitiesPage(authenticationFacade.getUserId(), startIndex, amount),
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/totalsize")
	public ResponseEntity<?> getFriendsActivitiesTotalSize(){
		return new ResponseEntity<>(activitiesService.getFriendsActivitiesTotalSize(authenticationFacade.getUserId())
				,HttpStatus.OK);
	}

}
