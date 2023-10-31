package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Prietenie;

import java.util.Objects;

public class PrietenieValidator implements Validator<Prietenie> {
    // Class that validates Prietenie

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("entity cannot be null");
        }
        if (entity.getId() == null) {
            throw new ValidationException("id cannot be null");
        }
        if (entity.getId().getLeft() == null) {
            throw new ValidationException("left id cannot be null");
        }
        if (entity.getId().getRight() == null) {
            throw new ValidationException("right id cannot be null");
        }
        if (Objects.equals(entity.getId().getLeft(), entity.getId().getRight())) {
            throw new ValidationException("cannot be friends with self");
        }
    }
}
