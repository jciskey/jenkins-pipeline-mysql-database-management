#!/usr/bin/env groovy

/*
    Evaluates a map of database configuration information.
    
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to use. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to use. Required.
        dbPass:    The password of the database user. Required.
    
    Returns the parsed database configuration object.
*/
def call(Map config) {
    // Set any unprovided configuration values to defaults
    // Set any unprovided configuration to random generated.
    if (config.dbName == null || config.dbName == '') {
        String dbNameConstructed = "testdb_" + env.MYSQL_UUID
        config.dbName = dbNameConstructed
    }

    // Set newDB variable to true if not specified.
    if (config.newDB == null | config.newDB == '') {
        config.newDB = true
    }

    config.mysqlPath = config.mysqlPath ?: '/usr/bin/mysql'
    config.mysqlPort = config.mysqlPort ?: 3306
    
    // Check that required, non-defaultable values are present
    if (config.dbUser == null) {
        error "Missing required parameter 'dbUser'"
    }
    if (config.dbPass == null) {
        error "Missing required parameter 'dbPass'"
    }
    
    // Return the parsed database configuration
    return config
}
