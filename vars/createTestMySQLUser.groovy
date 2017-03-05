#!/usr/bin/env groovy

/*
    Accepted Parameters:
        mysqlPath: The system path to the MySQL binary. Default: /usr/bin/mysql
        mysqlPort: The port to use to connect to MySQL. Default: 3306
        dbName:    The name of the database to create the test user for. Default: "testdb_${env.BUILD_NUMBER}"
        dbUser:    The name of the database user to create the test user with.
        dbPass:    The password of the database user.
    
    Returns the credentials of the created test user as a Map.
        Map Keys:
            test_username: The username of the test user
            test_password: The password of the test user
*/
def call(Closure body) {
    def config = evaluateMySQLDatabaseConfiguration(body)
    
    // Define the test user parameters here
    def test_user = "testdb_user_${env.BUILD_NUMBER}"
    def test_user_pwd = "testdb_password_${env.BUILD_NUMBER}"
    
    // Run shell commands to create the user here
    def CREATE_SQL = "CREATE USER '${test_user}'@'%' IDENTIFIED BY '${test_user_pwd}';"
    def GRANT_SQL = "GRANT ALL PRIVILEGES ON ${config.dbName}.* TO '${test_user}'@'%';" +
        "GRANT ALL PRIVILEGES ON ${config.dbName}.* TO '${test_user}'@'localhost';"
    def SHELL_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${CREATE_SQL}${GRANT_SQL}\nEOF"
    sh "${SHELL_CMD}"
    
    // Return the credentials of the new test user
    return [test_username:test_user, test_password:test_user_pwd]
}
