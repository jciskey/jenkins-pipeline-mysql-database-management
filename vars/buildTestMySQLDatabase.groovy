#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to create. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to create the database with.
        dbPass:    The password of the database user.
        newDB:      Boolean value to setup a new DB. Default: true.
    
    Returns the name of the created database as well as test user credentials in a Map.
        Map Keys:
            dbName:           The name of the test database
            testUsername:     The username of the test user
            testUserPassword: The password of the test user
*/
String call(Boolean newDB = true,  Closure body) {
    // Store UUID into a Jenkins environment variable.
    if (env.MYSQL_UUID == null || env.MYSQL_UUID == '' || newDB == true) {
        // Create a unique id to use for database names and user accounts.
        String uuid = UUID.randomUUID()
        uuidconstruct = uuid.replaceAll('-','_') + "_" + env.BUILD_NUMBER
        env.MYSQL_UUID = uuidconstruct
    }
    def config = evaluateMySQLDatabaseConfiguration(body)

    // Create the test database
    def createdDatabaseName = createMySQLDatabase {
        mysqlPath = config.mysqlPath
        mysqlPort = config.mysqlPort
        dbName = config.dbName
        dbUser = config.dbUser
        dbPass = config.dbPass
    }
    
    // Create the test user
    def test_user_credentials = createTestMySQLUser {
        mysqlPath = config.mysqlPath
        mysqlPort = config.mysqlPort
        dbName = createdDatabaseName
        dbUser = config.dbUser
        dbPass = config.dbPass
    }
    
    // Return the test database name and the test user credentials
    def retval = [:]
    retval.dbName = createdDatabaseName
    retval.testUsername = test_user_credentials.test_username
    retval.testUserPassword = test_user_credentials.test_password
    
    return retval
}
