#!/usr/bin/env groovy

/*
def call() {
    return 'test no params'
}
*/
/*
def call(String param1 = 'test with single param default') {
    return param1
}
*/
/*
def call(def param1 = null) {
    return param1?: 'null parameter passed'
}
*/
/*
def call(String dbUser = 'db_user', String dbPass = 'db_pass', def dbName = null, String mysqlPath = '/usr/bin/mysql', Integer mysqlPort = 3306) {
    return 'test multiple parameters defined'
}
*/

def call(String dbUser = 'db_user', String dbPass = 'db_pass', def dbName = null, String mysqlPath = '/usr/bin/mysql', Integer mysqlPort = 3306) {
    
    def retval = [:]
    retval.username = dbUser
    retval.password = dbPass
    
    return [username: dbUser, password: dbPass]
}
