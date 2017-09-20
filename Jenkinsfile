
node {
    def nginxContainer
    def nginxName = "sc-appium-test-nginx"
    def scLogFile = "sc.log"
    def scPidFile = "sc.pid"
    stage("start nginx") {
        nginxContainer = docker.image("nginx:1.13.5").run("-v web:/usr/share/nginx/html/web:ro --name ${nginxName}")
    }

    try {
        docker.image("java:8").inside("--link ${nginxName}:${nginxName}") {
            stage("checkout") {
                checkout scm
            }

            stage("verify nginx") {
                sh "curl http://${nginxName}/web"
            }

            stage("start sc") {
                sh "curl https://saucelabs.com/downloads/sc-4.4.9-linux.tar.gz -o sc.tar.gz"
                sh "tar -xvzf sc.tar.gz"
                sh "sc-4.4.9-linux/bin/sc --user=${SAUCE_USERNAME} --api-key=${SAUCE_KEY} --tunnel-identifier=${TUNNEL_IDENTIFIER} --rest-url=${SC_PUBLIC_ENDPOINT} --pidfile=${scPidFile} > ${scLogFile} 2>&1 &"
            }

            stage("wait for tunnel") {
                timeout(time: 3, unit: 'MINUTES') {
                    sh "( tail -f -n0 ${scLogFile} & ) | grep -q 'Sauce Connect is up, you may start your tests'"
                }
            }

            stage("test") {
                try {
                    withEnv(["DESTINATION_URL=http://${nginxName}/web"]) {
                        sh "./gradlew clean test"
                    }
                } finally {
                    junit "**/TEST-*.xml"
                }
            }
        }
    } finally {
        stage("cleanup") {
            nginxContainer.stop()
            sh "cat ${scLogFile}"
        }
    }
}