pipeline {
    agent { label 'docker-gradle-java8' }
    options { timestamps() }
    environment {
        NEXUS_CREDENTIALS = credentials('nexus_maven')

        PROJECT_NAME = getName()
        PROJECT_VERSION = getVersion()
        PROJECT_GROUPID = getGroupId()
        FILE_TYPE = "{{file_type}}"
        EMAIL = "{{e_mail}}"
    }
    stages {
        stage("gradle build") {
            steps {
                script {
                    sh "./gradlew build -x test"
                }
            }
        }

        stage("Dockernizer") {
            steps {
                script{
                    docker.withRegistry('http://10.217.65.18:5000', 'nexus_maven'){
                        customImage = docker.build("10.217.65.18:5002/member_test:0.0.1")
                    }
                }
            }
        }

        stage("integration test") {
            steps {
                script {
                    docker.withRegistry('http://10.217.65.18:5000', 'nexus_maven'){
                        docker.image('10.217.65.18:5000/mysql:5').withRun('--network "newdeal" -e "MYSQL_ROOT_PASSWORD=1324qwer" -e "MYSQL_ROOT_HOST=%" ') { c ->
                            sleep 5
                            def DB=hostIp("${c.id}")
                            withEnv(["DB_IP=${DB}"]) {
                                sh './gradlew check'
                            }
                        }
                    }
                }
            }
        }

        // stage("api test"){
        //     steps {
        //         script {
        //             docker.withRegistry('http://10.217.65.18:5000', 'nexus_maven'){
        //                 docker.image('10.217.65.18:5000/mysql:5').withRun('--network "newdeal" -e "MYSQL_ROOT_PASSWORD=1324qwer" -e "MYSQL_ROOT_HOST=%" ') { cc ->
        //                     sleep 5
                            
        //                     def DBIP=hostIp("${cc.id}")
        //                     customImage.withRun("--network newdeal -e DB_IP=${DBIP}") { ccm ->
        //                         sleep 5
                                
        //                         def CMIP=hostIp("${ccm.id}")
        //                         sh "newman run src/test/test.json --env-var url=${CMIP}"
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }


        stage("Sonar") {
            steps {
                withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'seungjoon-sonar') {
                    sh "sonar-scanner\
                      -Dsonar.projectKey=${PROJECT_NAME}\
                      -Dsonar.java.binaries=build/libs\
                      -Dsonar.exclusions=src/main/resources/static/bootstrap/vendor/chart.js/Chart.js"
                }
            }
        }

        stage("gradle uploadArchive"){
            steps {
                sh "gradle uploadArchive"
            }
        }
    }
    post {
        always {
            junit '**/build/test-results/test/*.xml'
        }
        // Email 설정
        // success {
		// 	echo 'successed'
		// 	emailext (	
		// 	    from: "devops.sw@kt.com"
		// 		to: "${EMAIL}",
		// 		subject: "${env.JOB_NAME} #${env.BUILD_NUMBER} [${currentBuild.result}]",
		// 		body: "Build URL: ${env.BUILD_URL}.\n\n",
		// 		attachLog: true,
		// 	)
		// }
		// {
		// 	echo 'failed'
		// 		emailext (
		// 			from: "devops.sw@kt.com"
		// 			to: "${EMAIL}",
		// 			subject: "${env.JOB_NAME} #${env.BUILD_NUMBER} [${currentBuild.result}]",
		// 			body: "Build URL: ${env.BUILD_URL}.\n\n",
		// 			attachLog: true,
		// 	)
		// }

    }
}

def getVersion() {
    return sh (
        script: "gradle properties -q | grep version: | awk '{print \$2}'",
        returnStdout: true
    ).trim()
}

def getName() {
    return sh (
        script: "gradle properties -q | grep name: | awk '{print \$2}'",
        returnStdout: true
    ).trim()
}

def getGroupId() {
    return sh (
        script: "gradle properties -q | grep group: | awk '{print \$2}'",
        returnStdout: true
    ).trim()
}

def hostIp(container) {
    sh "docker inspect -f {{.NetworkSettings.Networks.newdeal.IPAddress}} ${container} > host.ip"
    readFile('host.ip').trim()
}
