node {

    stage('Unit Test(Multi version)') {
        parallel java8: {
            unitTest('java:openjdk-8')
        }, java9: {
            unitTest('oracle-java9-plus')
        }
    }

}

def unitTest(containerImage) {
    docker.image(containerImage).inside("-u root") {
      // Checkout
      checkout scm
      // Unit test
      sh './gradlew clean test'
      // JUnitテストレポートを保存
      step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/*.xml'])
    }
}
