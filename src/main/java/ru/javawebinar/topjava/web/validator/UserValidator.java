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
import ru.javawebinar.topjava.repository.datajpa.DataJpaUserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class UserValidator implements Validator {
    @Autowired
    private DataJpaUserRepository repository;

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

        UserTo user = (UserTo) target;
        User test = repository.getByEmail(user.getEmail());

        if (test != null) {
            UserTo checkableUser = UsersUtil.asTo(test);
            if (user.getId() != null) {
                if (!checkableUser.getId().equals(user.getId())) {
                    addError(errors);
                }
            } else if (SecurityUtil.safeGet() != null) {
                if (checkableUser.getEmail().equalsIgnoreCase(user.getEmail()) && SecurityUtil.authUserId() != test.id()) {
                    addError(errors);
                }
            } else if (checkableUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                addError(errors);
            }
        }
    }

    private void addError(Errors errors) {
        errors.rejectValue("email", "exception.email.duplicate",
                messageSource.getMessage("exception.email.duplicate", null, LocaleContextHolder.getLocale()));
    }
}
