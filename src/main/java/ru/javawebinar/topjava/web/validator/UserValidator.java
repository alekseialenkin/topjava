package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

//https://habr.com/ru/articles/424819/
@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("validator")
    private LocalValidatorFactoryBean validatorFactory;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SpringValidatorAdapter validatorAdapter = new SpringValidatorAdapter(validatorFactory);
        validatorAdapter.validate(target, errors);

        UserTo user = (UserTo) target;
        UserTo user1 = null;
        try {
            user1 = UsersUtil.asTo(service.getByEmail(user.getEmail()));
        } catch (NotFoundException ignored) {
        }
        if (user1 != null && user.getEmail().equals(user1.getEmail())) {
            errors.rejectValue("email", "exception.email.duplicate");
        }
    }
}
