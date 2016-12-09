node {

    stage('Soruce checkout)') {
        // Checkout
        checkout scm
    }

    // ２つの言語バージョンでUnitTest
    stage('Unit Test(Multi version)') {
        parallel java8: {
            unitTest('java:openjdk-8')
        }, java9: {
            echo 'test'
            // unitTest('oracle-java9-plus')
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
        // TODO 実装

        // まず、コンテナ立ててそこにデプロイする。

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
        // 自身の内容をコピーして、一階層落とす。
        sh "cp -avr . ${containerImage} || echo 'copy sources'"
        dir("./${containerImage}") {
            // Unit test
            sh './gradlew clean test'

            // FIXME なぜかここで「そんなファイルはない」と怒られ、死ぬ。用調査。
            // JUnitテストレポートを保存
            // step([$class: 'JUnitResultArchiver', testResults: './build/test-results/**.xml'])
        }
    }
}
