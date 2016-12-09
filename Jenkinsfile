node {

    stage('Unit Test(Multi version)') {
        parallel java8: {
            unitTest('java:openjdk-8')
        }, java9: {
//            unitTest('oracle-java9-plus')
            echo '９はスキップ'
        }
    }

    stage('残骸の確認') {
        sh 'find ./'
    }

    staget('Binary(jar) Build') {
        docker.image(containerImage).inside("-u root") {
            // Checkout
            checkout scm
            // jar build (TestはSkip)
            sh './gradlew clean build -Dskip.tests=true'
        }
    }

    stage('残骸の確認２') {
        sh 'find ./'
    }



}

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
