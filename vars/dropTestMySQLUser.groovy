#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbUser:    The name of the database user to drop the test user with.
        dbPass:    The password of the database user.
        testUser:  The username of the test user to be dropped. Default: "testdb_user_${env.BUILD_NUMBER}"
        
*/
def call(String dbUserName, String dbPassword, String dbDropUserName = '', String mysqlPath = '', String mysqlPort = '') {

    configuration = [:]
    configuration.dbUser = dbUserName
    configuration.dbPass = dbPassword
    configuration.mysqlPath = mysqlPath
    configuration.mysqlPort = mysqlPort
    def dropconfig = evaluateMySQLConfiguration(configuration)

    if (dbDropUserName.isEmpty()) {
        // Define the test user parameters here
        String test_username = "testdb_user_${env.MYSQL_UUID}"
        test_user = test_username.take(30)
    }
    else
    {
        test_user = dbDropUserName
    }

    // Run shell commands to drop the user here
    def REVOKE_SQL = "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'%'; REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'localhost';"
    def FLUSH = "FLUSH PRIVILEGES;"
    def SHELL_CMD1 = "\"${dropconfig.mysqlPath}\" -u \"${dropconfig.dbUser}\" --password=\"${dropconfig.dbPass}\" <<-EOF\n${REVOKE_SQL}${FLUSH}\nEOF"
    sh "${SHELL_CMD1}"
    def DROP_SQL = "DROP USER '${test_user}'; DROP USER '${test_user}'@'localhost';"
    def SHELL_CMD2 = "\"${dropconfig.mysqlPath}\" -u \"${dropconfig.dbUser}\" --password=\"${dropconfig.dbPass}\" <<-EOF\n${DROP_SQL}\nEOF"
    sh "${SHELL_CMD2}"
}
