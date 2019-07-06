#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbSchemaName:    The name of the database to drop. Default: "testdb_${env.BUILD_NUMBER}"
        dbUserName:    The name of the database user to drop the database with.
        dbPassword:    The password of the database user.
*/
def call(String dbUserName, String dbPassword, String dbSchemaName, String mysqlPath = '', String mysqlPort = '') {
    configuration = [:]
    configuration.dbUser = dbUserName
    configuration.dbPass = dbPassword
    configuration.dbName = dbSchemaName
    configuration.mysqlPath = mysqlPath
    configuration.mysqlPort = mysqlPort
    def dropconfig = evaluateMySQLConfiguration(configuration)
    // Run shell commands to drop the database here
    def DROP_SQL = "DROP DATABASE IF EXISTS ${dbSchemaName};"
    def SHELL_CMD = "\"${dropconfig.mysqlPath}\" -u \"${dropconfig.dbUser}\" --password=\"${dropconfig.dbPass}\" <<-EOF\n${DROP_SQL}\nEOF"
    sh "${SHELL_CMD}"
}
