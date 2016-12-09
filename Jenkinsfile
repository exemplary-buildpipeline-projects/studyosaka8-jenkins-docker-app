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
    }




}

// Unit Test部分
// 引数は「実行したいDockerImage名」
def unitTest(containerImage) {
    docker.image(containerImage).inside("-u root") {

        // ディレクトリを共有するっぽいので、被らないように「Container毎のDir」を作成。
        sh "mkdir -p ${containerImage}"
        dir("./${containerImage}) {
            // Checkout
            checkout scm
            // Unit test
            sh './gradlew clean test'

            // FIXME なぜかここで「そんなファイルはない」と怒られ、死ぬ。用調査。
            // JUnitテストレポートを保存
          // step([$class: 'JUnitResultArchiver', testResults: './build/test-results/**.xml'])
        }

    }
}
