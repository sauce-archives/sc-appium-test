
node {
    def nginxContainer
    def nginxPort = 8666
    def nginxName = "sc-appium-test-nginx"
    stage("start webserver") {
        nginxContainer = docker.image("nginx:1.13.5").run("-p ${nginxPort}:80 -v web:/usr/share/nginx/html:ro --name ${nginxName}")
    }

    docker.image("java:8").inside("--net=container:${nginxName}") {
        stage("checkout") {
            checkout scm
        }

        def scLogFile = "sc.log"
        def scPidFile = "sc.pid"

        try {
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
                withEnv(["DESTINATION_URL=${nginxName}:${nginxPort}"]) {
                    sh "./gradlew test"
                }
            }
        } finally {
            nginxContainer.stop()
            try {
                def pid = readFile scPidFile
                sh "kill ${pid}"
            } finally {
                sh "cat ${scLogFile}"
            }
        }
    }
}