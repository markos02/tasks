package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    AdminConfig adminConfig;

    @Autowired
    TaskRepository repository;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildTrelloCardMail(String message) {

        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        if (message.contains("has been created on your Trello account")) {

            Context context = new Context();
            context.setVariable("message", message);
            context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
            context.setVariable("button", "Visit website");
            context.setVariable("admin_name", adminConfig.getAdminName());
            context.setVariable("goodbye_message", "Have a nice day!");
            context.setVariable("company_name", adminConfig.getCompanyName());
            context.setVariable("company_email", adminConfig.getCompanyEmail());
            context.setVariable("company_phone", adminConfig.getCompanyPhone());
            context.setVariable("company_goal", adminConfig.getCompanyGoal());
            context.setVariable("show_button", false);
            context.setVariable("is_friend", false);
            context.setVariable("admin_config", adminConfig);
            context.setVariable("application_functionality", functionality);
            return templateEngine.process("mail/created-trello-card-mail", context);
        } else {
            Context context = new Context();
            context.setVariable("message", message);
            context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
            context.setVariable("button", "Visit website");
            context.setVariable("admin_name", adminConfig.getAdminName());
            context.setVariable("is_friend", false);
            context.setVariable("company_name", adminConfig.getCompanyName());
            context.setVariable("company_email", adminConfig.getCompanyEmail());
            context.setVariable("company_phone", adminConfig.getCompanyPhone());
            context.setVariable("company_goal", adminConfig.getCompanyGoal());
            if (repository.count() == 0) {
                context.setVariable("show_button", false);
                context.setVariable("goodbye_message", "Congratulations, you have no tasks, enjoy your free day!");
            } else {
                context.setVariable("show_button", true);
                context.setVariable("goodbye_message", "Have a nice day!");
            }
            context.setVariable("admin_config", adminConfig);
            context.setVariable("application_functionality", functionality);
            return templateEngine.process("mail/once-a-day-mail", context);
        }

    }
}
