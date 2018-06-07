package com.kapildas.afpvalidator.validator;

import java.util.ArrayList;
import java.util.List;

import com.kapildas.afpvalidator.AFPFile;

public abstract class Validator {
    private List<String> errors;

    public abstract void validate(AFPFile afpFile);

    public boolean isValid() {
        return !getErrors().isEmpty();
    }

    public List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void addError(String error) {
        getErrors().add(error);
    }
}
