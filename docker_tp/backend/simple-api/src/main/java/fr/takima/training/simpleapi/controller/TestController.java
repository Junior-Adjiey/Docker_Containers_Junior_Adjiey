package fr.takima.training.simpleapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testEndpoint() {
        return "🚀 Déploiement CI/CD Ansible réussi !";
    }
}
