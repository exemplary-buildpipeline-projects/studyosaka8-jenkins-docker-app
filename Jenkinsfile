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

}

def unitTest(containerImage) {
    docker.image(containerImage).inside("-u root") {
      // Checkout
      checkout scm
      // Unit test
      sh './gradlew clean test'

      echo 'テストレポートが拾えないみたいなので、確認'
      sh 'find ./'

      // JUnitテストレポートを保存
      step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*.xml'])
    }
}
