
node {
    def scLogFile = "sc.log"
    def scPidFile = "sc.pid"
    def endpoint

    try {
        docker.image("java:8").inside {
            stage("checkout") {
                checkout scm
            }

            stage("start webserver") {
                sh "apt-get install -y python curl"
                sh "python -m SimpleHTTPServer &"
                def ip = sh returnStdout: true, script: 'hostname -I'
                endpoint = "http://${ip.trim()}:8000/web"
                sh "curl ${endpoint}"
            }

            stage("start sc") {
                sh "curl https://saucelabs.com/downloads/sc-4.4.9-linux.tar.gz -o sc.tar.gz"
                sh "tar -xvzf sc.tar.gz"
                sh "sc-4.4.9-linux/bin/sc --user=${SAUCE_USERNAME} --api-key=${SAUCE_KEY} --tunnel-identifier=${TUNNEL_IDENTIFIER} --rest-url=${SC_PUBLIC_ENDPOINT} --pidfile=${scPidFile} -B all > ${scLogFile} 2>&1 &"
            }

            stage("wait for tunnel") {
                timeout(time: 3, unit: 'MINUTES') {
                    sh "( tail -f -n0 ${scLogFile} & ) | grep -q 'Sauce Connect is up, you may start your tests'"
                }
            }

            stage("test") {
                try {
                    withEnv(["DESTINATION_URL=endpoint"]) {
                        sh "./gradlew clean test"
                    }
                } finally {
                    junit "**/TEST-*.xml"
                }
            }
        }
    } finally {
        try {
            sh "cat ${scLogFile}"
        } catch (all) {
            print "Failed to cat ${scLogFile}. Probably it doesn't exist because the test aborted early."
        }
    }
}