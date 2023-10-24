package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Utilizator;
public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("entity cannot be null");
        }
        if(entity.getId() == null){
            throw new ValidationException("id cannot be null");
        }
        if(entity.getFirstName().length() < 3){
            throw new ValidationException("first name must contain at least 3 characters");
        }
        if(entity.getLastName().length() < 3){
            throw new ValidationException("last name must contain at least 3 characters");
        }
    }
}

