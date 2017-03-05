#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to drop. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to drop the database with.
        dbPass:    The password of the database user.
*/
def call(body) {
    // evaluate the body block, and collect configuration into the object
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
    
    // Run shell commands to drop the database here
    def DROP_SQL = "DROP DATABASE IF EXISTS ${config.dbName};"
    def SHELL_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${DROP_SQL}\nEOF"
    sh "${SHELL_CMD}"
}
