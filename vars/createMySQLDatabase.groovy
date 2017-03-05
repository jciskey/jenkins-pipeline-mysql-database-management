#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to create. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to create the database with.
        dbPass:    The password of the database user.
    
    Returns the name of the created database.
*/
def call(Closure body) {
    def config = evaluateMySQLDatabaseConfiguration(body)
    
    // Run shell commands to create the database here
    def CREATE_SQL = "CREATE DATABASE IF NOT EXISTS ${config.dbName};"
    def SHELL_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${CREATE_SQL}\nEOF"
    sh "${SHELL_CMD}"
    
    // Return the name of the created database
    return config.dbName
}
