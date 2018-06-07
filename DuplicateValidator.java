package com.kapildas.afpvalidator.validator;

import java.util.List;

import com.kapildas.afpvalidator.AFPFile;
import com.kapildas.afpvalidator.config.ApplicationConfiguration;
import com.kapildas.afpvalidator.database.AFPRecordMapper;
import com.kapildas.afpvalidator.database.DatabaseConnection;

public class DuplicateValidator extends Validator {

    @Override
    public void validate(AFPFile afpFile) {
        try (DatabaseConnection connection = new DatabaseConnection()) {
            String query = String.format(ApplicationConfiguration.DUP_CORP_QUERY.getValue(), afpFile.getCorpId(), afpFile.getCycleNumber());
            final List<AFPFile> afpFiles = connection.executeReadQuery(query, new AFPRecordMapper());
        }
    }
}
