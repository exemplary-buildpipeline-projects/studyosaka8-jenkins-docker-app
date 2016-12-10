node {

    stage('Soruce checkout)') {
        // 念の為 ドット始まり(.git含む) 以外のファイルを削除しておく。
        sh 'sudo rm -rf ./*'
        // Checkout
        checkout scm
    }

    // ２つの言語バージョンでUnitTest
//    stage('Unit Test(Multi version)') {
//        parallel java8: {
//            unitTest('java:openjdk-8')
//        }, java9: {
//            sleep 120  // プロセス分けてるくせに競合する…ため、ちょっとマを開ける。
//            unitTest('oracle-java9-plus')
//        }
//    }

    stage('Binary(jar) Build') {
        docker.image('java:openjdk-8').inside("-u root") {
            // Checkout
            checkout scm
            // jar build (TestはSkip)
            sh './gradlew clean build -Dskip.tests=true'
        }
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
            def command = "./gradlew integrationTest -Dit.appRootUrl=http://${uiTestServerIp}:8080/ -Dit.seleniumeRemoteDriverUrl=http://${seleniumServerIp}:24444//wd/hub"
            echo "command : " + command
            sh command
        }

        // TODO テストが失敗しても、コンテナ殺すハンドリング。

        // テストが終わったので、デプロイしたサーバは殺す。
        sh "docker rm -f deploy-app || echo 'container allrady clearad.'"

    }

    stage('Deploy test environemt(by Branch name)') {

        // ブランチ名取得。
        def branch = env.JOB_NAME.replaceAll(/.*\//,"")
        // デプロイするコンテナ名を作成。
        def deployContainerName = "app-${branch}"
        echo "deployContainerName : ${deployContainerName}"

        // ファイルが在るか？チェック(戻り値が0でなくば死ぬのを利用して)
        sh 'ls -l ./build/libs/*.jar'

        // デプロイサーバをコンテナで立てる。（もとから在るなら削除。）
        sh "docker rm -f ${deployContainerName} || echo 'container allrady clearad.'"
        sh 'docker run -d --name ${deployContainerName} -v $PWD/build/libs:/tmp/app_import deploy-target'

        // 直近で建てたデプロイコンテナのIPを取得。
        def deployContainerIp = getIpAddressByContainerName(deployContainerName)
        echo "deployContainerIp : ${deployContainerIp}"

        // nginxでリンクをはり、外から見れるようにする。
        sh "sudo echo 'location /master { proxy_pass http://${deployContainerIp}:8080/; }' > /etc/nginx/default.d/${branch}.conf"
        sh "sudo service nginx reload"

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
