
node {
    docker.image("maven:3.5.0").inside {
        stage("checkout") {
            checkout scm
        }

        def scLogFile = "sc.log"

        try {
            stage("start sc") {
                sh "curl https://saucelabs.com/downloads/sc-4.4.9-linux.tar.gz -o sc.tar.gz"
                sh "tar -xvzf sc.tar.gz"
                sh "sc-4.4.9-linux/bin/sc --user ${SAUCE_USERNAME} --api-key ${SAUCE_KEY} --tunnel-identifier ${TUNNEL_IDENTIFIER} > ${scLogFile} 2>&1 &"
            }

            stage("wait for sc") {
                timeout(time: 3, unit: 'MINUTES') {
                    sh "( tail -f -n0 ${scLogFile} & ) | grep -q 'Sauce Connect is up, you may start your tests'"
                }
            }

            stage("start test") {
                sh "mvn test"
            }
        } finally {
            sh "killall sc | echo No 'sc' process found to kill"
        }
    }
}