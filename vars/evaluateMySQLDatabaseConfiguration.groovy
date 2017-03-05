#!/usr/bin/env groovy

/*
    Evaluates a block/closure of database configuration information and converts it into an object.
    
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to use. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to use. Required.
        dbPass:    The password of the database user. Required.
    
    Returns the parsed database configuration object.
*/
def call(Closure body) {
    // Evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    // Set any unprovided configuration values to defaults
    config.dbName = config.dbName ?: "testdb_${env.BUILD_NUMBER}"
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
