package ch.hearc.todont.controllers;

import ch.hearc.todont.models.ToDont;
import ch.hearc.todont.models.User;
import ch.hearc.todont.repositories.ToDontRepository;
import ch.hearc.todont.services.ToDontService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/todont/create")
public class CreateToDontController {

    @Autowired
    private ToDontService toDontService;

    @Autowired
    private ToDontRepository toDontRepo;

    /**
     * GET on the "/todont/create/" page.
     * 
     * @param model Model passed down to the view
     * @return The page template name
     */
    @GetMapping("/")
    public String get(Model model) {
        ToDont toDont = new ToDont();
        model.addAttribute("toDont", toDont);
        return "create";
    }

    /**
     * Post to add new to-dont.
     * 
     * @param user      User adding the to-dont
     * @param newToDont Returned value from form
     * @return The page template name
     */
    @PostMapping("/")
    public String createToDont(@RequestParam(name = "usernames[]") String[] usernames, 
            @RequestParam(name= "moderatorBoolean[]") String[] moderators,
            @AuthenticationPrincipal User user,
            @ModelAttribute ToDont newToDont) {

        newToDont.setDatePublished(Timestamp.from(Instant.now());

        // Add user by username to todont by pledge
        for (int i = 0; i < usernames.length; i++) {    
            toDontService.inviteUserToToDont(user, newToDont, usernames[i]);
            if (moderators[i] == "1") {
                toDontService.addModerator(user, newToDont, usernames[i]);
            }
        }

        // Save todont to DB
        if (toDontRepo.save(newToDont) != null) {
            return "";
        } else {
            // Error, stay on page
            return "error";
        }
        
    }
    
}
