package com.kapildas.afpvalidator.validator;

import com.kapildas.afpvalidator.AFPFile;
import com.kapildas.afpvalidator.config.ApplicationConfiguration;
import com.kapildas.afpvalidator.database.DatabaseConnection;

public class CorpIdValidator extends Validator {

    @Override
    public void validate(AFPFile afpFile) {
        try (DatabaseConnection connection = new DatabaseConnection()) {
            String query = String.format(ApplicationConfiguration.CORP_VALIDATION_QUERY.getValue(), afpFile.getCorpId());
            final int corpIdCount = connection.executeCountQuery(query);
            if (corpIdCount == 0) {
                getErrors().add("Invalid corp id : " + afpFile.getCorpId());
            }
        }
    }
}
