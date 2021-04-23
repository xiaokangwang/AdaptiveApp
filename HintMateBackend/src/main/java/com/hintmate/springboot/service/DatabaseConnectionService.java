package com.hintmate.springboot.service;

import java.sql.Connection;

public interface DatabaseConnectionService {

    Connection getDBConnectionToMeanings();
    Connection getDBConnectionToUKnowledge();
}
