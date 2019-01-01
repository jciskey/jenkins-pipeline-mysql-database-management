#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbSchemaName:    The name of the database to destroy. Default: "testdb_${env.BUILD_NUMBER}"
        dbUserName:    The name of the database user to destroy the database with.
        dbPassword:    The password of the database user.
*/
def call(String dbUserName, String dbPassword, String dbSchemaName, String mysqlPath = '', String mysqlPort = '') {
    echo "====================================== Debug ======================================================="
    echo "DB User: ${dbUserName}"
    echo "DB Pass: ${dbPassword}"
    echo "DB Name: ${dbSchemaName}"
    
    // Destroy the test database
    dropMySQLDatabase(dbUserName, dbPassword, dbSchemaName)

    // Destroy the test user
    dropTestMySQLUser(dbUserName, dbPassword, dbSchemaName)
    }
