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

def call(def param1 = null) {
    return param1?: 'null parameter passed'
}
