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
/*
def call(body) {
    return [username:'test', password:'pass']
}
*/
/*
def call(Closure body) {
    echo sh(returnStdout: true, script: 'env')
}
*/

def call(Closure body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    //echo config.dbUser
    //echo config.dbPass
}
