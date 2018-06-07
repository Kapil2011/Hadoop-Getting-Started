package com.kapildas.afpvalidator.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper<T> {
    T mapRecord(ResultSet resultSet) throws SQLException;
}
