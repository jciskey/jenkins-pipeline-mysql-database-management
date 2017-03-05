#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to destroy. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to destroy the database with.
        dbPass:    The password of the database user.
*/
def call(Closure body) {
    // Destroy the test database
    dropMySQLDatabase(body)
    
    // Destroy the test user
    dropTestMySQLUser(body)
}
