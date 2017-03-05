#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbUser:    The name of the database user to drop the test user with.
        dbPass:    The password of the database user.
        testUser:  The username of the test user to be dropped. Default: "testdb_user_${env.BUILD_NUMBER}"
        
*/
def call(Closure body) {
    def config = evaluateMySQLDatabaseConfiguration(body)
    
    // Define the test user parameters here
    def test_user = config.testUser ?: "testdb_user_${env.BUILD_NUMBER}"
    
    // Run shell commands to drop the user here
    def DROP_SQL = "DROP USER '${test_user}'@'%';"
    def REVOKE_SQL = "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'%';" +
        "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '${test_user}'@'localhost';"
    def SHELL_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${DROP_SQL}${REVOKE_SQL}\nEOF"
    sh "${SHELL_CMD}"
}
