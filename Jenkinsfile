node {

    stage('Soruce checkout)') {
        // 念の為 ドット始まり(.git含む) 以外のファイルを削除しておく。
        sh 'sudo rm -rf ./*'
        // Checkout
        checkout scm
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
        // TODO 実装

        // SeleniumサーバのIPを取得する。
        def seleniumServerIp = getIpAddressByContainerName('selenium')
        echo "seleniumServerIp : ${seleniumServerIp}"

        // ファイルが在るか？チェック(戻り値が0でなくば死ぬのを利用して)
        sh 'ls -l ./build/libs/*.jar'

        // まず、コンテナ立ててそこにデプロイする。
        sh "docker rm -f deploy-app || echo 'container allrady clearad.'"
        sh 'docker run -d --name deploy-app -v $PWD/build/libs:/tmp/app_import deploy-target'
        // 今建てたデプロイ用コンテナのIPを取得する。
        def uiTestServerIp = getIpAddressByContainerName('deploy-app')
        echo "uiTestServerIp : ${uiTestServerIp}"

        // インテグレーションテストを(Docekrの中で)回す。
        docker.image('java:openjdk-8').inside("-u root") {
            // UI Test
            sh "./gradlew integrationTest -Dit.appRootUrl=http://${uiTestServerIp}:8080/ -Dit.seleniumeRemoteDriverUrl=http://${seleniumServerIp}:24444//wd/hub"
        }

        // TODO テストが失敗しても、コンテナ殺すハンドリング。

        // テストが終わったので、デプロイしたサーバは殺す。
        sh "docker rm -f deploy-app || echo 'container allrady clearad.'"

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

// Dockerのコンテナ名から、IPAddressを割り出す。
def getIpAddressByContainerName(name) {
    return sh (
        script:  "docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${name}"
        ,returnStdout: true
    )
}
