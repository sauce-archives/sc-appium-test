
node {
    docker.image("java:8").inside {
        stage("checkout") {
            checkout scm
        }

        def scLogFile = "sc.log"
        def scPidFile = "sc.pid"
        def nginx = docker.image("nginx:1.13.5").run("-p 8666:80 -v web:/usr/share/nginx/html:ro")

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
                def ip = sh returnStdout: true, script: "hostname -i"
                withEnv(["DESTINATION_URL=$ip:8666") {
                    sh "./gradlew test"
                }
            }
        } finally {
            nginx.stop()
            try {
                def pid = readFile scPidFile
                sh "kill ${pid}"
            } finally {
                sh "cat ${scLogFile}"
            }
        }
    }
}