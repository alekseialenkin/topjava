package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

//https://habr.com/ru/articles/424819/
@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("validator")
    private LocalValidatorFactoryBean validatorFactory;

    @Autowired
    protected MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpringValidatorAdapter validatorAdapter = new SpringValidatorAdapter(validatorFactory);
        validatorAdapter.validate(target, errors);

        if (target instanceof UserTo) {
            UserTo user = (UserTo) target;
            checkEmailDuplicate(errors, user);
        } else if (target instanceof User) {
            User user = (User) target;
            checkEmailDuplicate(errors,
                    new UserTo(null, user.getName(), user.getEmail(), user.getPassword(), user.getCaloriesPerDay()));
        }
    }

    private void checkEmailDuplicate(Errors errors, UserTo user) {
        try {
            UserTo checkableUser = UsersUtil.asTo(service.getByEmail(user.getEmail()));
            if (user.getId() != null) {
                if (!checkableUser.getId().equals(user.getId())) {
                    addError(errors);
                }
            } else if (SecurityUtil.safeGet() != null) {
                if (checkableUser.getEmail().equalsIgnoreCase(user.getEmail()) && SecurityUtil.authUserId() != checkableUser.id()) {
                    addError(errors);
                }
            } else if (checkableUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                addError(errors);
            }
        } catch (NotFoundException ignored) {
        }
    }

    private void addError(Errors errors) {
        errors.rejectValue("email", "exception.email.duplicate",
                messageSource.getMessage("exception.email.duplicate", null, LocaleContextHolder.getLocale()));
    }
}
