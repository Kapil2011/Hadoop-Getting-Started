package com.kapildas.afpvalidator.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kapildas.afpvalidator.ValidationException;
import com.kapildas.afpvalidator.config.ApplicationConfiguration;

public class DatabaseConnection implements AutoCloseable {
	private final Connection connection;

	public DatabaseConnection() {
		try {
			connection = DriverManager.getConnection(ApplicationConfiguration.JDBC_URL.getValue(),
					ApplicationConfiguration.JDBC_USER.getValue(), ApplicationConfiguration.JDBC_PASSWORD.getValue());
		} catch (SQLException sqlExp) {
			throw new ValidationException("Unable to initialize database connection", sqlExp);
		}
	}

	public <T> List<T> executeReadQuery(String sql, ResultMapper<T> mapper) {
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
			List<T> result = new ArrayList<>();
			while (resultSet.next()) {
				result.add(mapper.mapRecord(resultSet));
			}
			return Collections.unmodifiableList(result);
		} catch (SQLException sqlExp) {
			throw new ValidationException("Error in executing the read query", sqlExp);
		}
	}

	public int executeCountQuery(String sql) {
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
			return resultSet.next() ? resultSet.getInt(1) : 0;
		} catch (SQLException sqlExp) {
			throw new ValidationException("Error in executing the count query", sqlExp);
		}
	}

	@Override
	public void close() {
		close(connection);
	}

	private void close(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// Ignore this error
			}
		}
	}
}
