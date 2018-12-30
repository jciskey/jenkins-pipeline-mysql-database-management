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
    String test_username = "testdb_user_${config.uuid}"
    def test_user = test_username.take(32)
    def test_user_pwd = "testdb_password_${config.uuid}"
    
    // Run shell commands to create the user here
    def CREATE_SQL = "CREATE USER '${test_user}'@'%' IDENTIFIED BY '${test_user_pwd}';" +
        "CREATE USER '${test_user}'@'localhost' IDENTIFIED BY '${test_user_pwd}';"
    def GRANT_SQL = "GRANT ALL PRIVILEGES ON ${config.dbName}.* TO '${test_user}'@'%';" +
        "GRANT ALL PRIVILEGES ON ${config.dbName}.* TO '${test_user}'@'localhost';"
    def FLUSH_SQL = "FLUSH PRIVILEGES;"
    
    def CREATE_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${CREATE_SQL}\nEOF"
    def GRANT_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${GRANT_SQL}\nEOF"
    def FLUSH_CMD = "\"${config.mysqlPath}\" -u \"${config.dbUser}\" --password=\"${config.dbPass}\" <<-EOF\n${FLUSH_SQL}\nEOF"
    
    echo "Creating test user '${test_user}'"
    sh "${CREATE_CMD}"
    
    echo "Granting permissions for test user '${test_user}'"
    sh "${GRANT_CMD}"
    
    echo "Refreshing database privileges for the new test user"
    sh "${FLUSH_CMD}"
    
    // Return the credentials of the new test user
    return [test_username:test_user, test_password:test_user_pwd]
}
