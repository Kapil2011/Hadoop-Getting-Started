package com.kapildas.afpvalidator.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kapildas.afpvalidator.AFPFile;

public class AFPRecordMapper implements ResultMapper<AFPFile> {
    @Override
    public AFPFile mapRecord(ResultSet resultSet) throws SQLException {
        AFPFile.Factory factory = new AFPFile.Factory();
        factory.setFileType(resultSet.getString("file_type"));
        factory.setFileFormat(resultSet.getString("file_format"));
        factory.setJobName(resultSet.getString("job_name"));
        factory.setShortCode(resultSet.getString("short_code"));
        factory.setCycleNumber(resultSet.getInt("cycle_number"));
        factory.setCorpId(resultSet.getString("corp_id"));
        final Date cycleDate = resultSet.getDate("cycle_date");
        factory.setCycleDate(cycleDate.toLocalDate());
        return factory.build();
    }
}
