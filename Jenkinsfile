node {

    stage('残骸の確認０') {
        sh 'find ./'
    }

    // ２つの言語バージョンでUnitTest
    stage('Unit Test(Multi version)') {
        parallel java8: {
            unitTest('java:openjdk-8')
        }, java9: {
            unitTest('oracle-java9-plus')
        }
    }

    stage('残骸の確認') {
        sh 'find ./'
    }

    stage('Binary(jar) Build') {
        docker.image('java:openjdk-8').inside("-u root") {
            // Checkout
            checkout scm
            // jar build (TestはSkip)
            sh './gradlew clean build -Dskip.tests=true'
        }
    }

    stage('残骸の確認２') {
        sh 'find ./'
        sh 'ls -l ./build/libs'
    }

    stage('UI(Integration) Test') {

        // Seleniumサーバが立ってるかを確認し、無いようなら再度起動する。


        // まず、デプロイする。

        docker.image('java:openjdk-8').inside("-u root") {
            // Checkout
            checkout scm
            // jar build (TestはSkip)
            sh './gradlew clean build -Dskip.tests=true'
        }
    }



}

// Unit Test部分
// 引数は「実行したいDockerImage名」
def unitTest(containerImage) {
    docker.image(containerImage).inside("-u root") {

        // Checkout
        checkout scm
        // Unit test
        sh './gradlew clean test'

        // FIXME なぜかここで「そんなファイルはない」と怒られ、死ぬ。用調査。
        // JUnitテストレポートを保存
        // step([$class: 'JUnitResultArchiver', testResults: './build/test-results/**.xml'])

    }
}
