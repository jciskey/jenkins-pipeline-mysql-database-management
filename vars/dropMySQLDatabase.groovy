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
    def config = evaluateMySQLDatabaseConfiguration(body)
    
    // Run shell commands to drop the database here
    def DROP_SQL = "DROP DATABASE IF EXISTS ${config.dbName};"
    def SHELL_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${DROP_SQL}\nEOF"
    sh "${SHELL_CMD}"
}
