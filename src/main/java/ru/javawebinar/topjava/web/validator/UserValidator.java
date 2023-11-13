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
@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("validator")
    private LocalValidatorFactoryBean validatorFactory;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpringValidatorAdapter validatorAdapter = new SpringValidatorAdapter(validatorFactory);
        validatorAdapter.validate(target, errors);
        UserTo user = (UserTo) target;
        UserTo checkableUser = null;
        try {
            checkableUser = UsersUtil.asTo(service.getByEmail(user.getEmail()));
        } catch (NotFoundException ignored) {
        }
        if (checkableUser != null && user.getEmail().equals(checkableUser.getEmail())) {
            errors.rejectValue("email", "exception.email.duplicate",
                    messageSource.getMessage("exception.email.duplicate", null, LocaleContextHolder.getLocale()));
        }
    }
}
