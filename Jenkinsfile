pipeline {
    agent {
        label 'master'
    }
    parameters {
        string(
            name: 'SONARQUBE_CONFIG_ID',
            defaultValue: 'Sonar Qube Server',
            description: 'The ID of the SonarQube Configuration to use')
    }
    environment {
        BUILD_TYPE = buildType()
        NAME_APK = nameApk()
    }
    stages {
        stage("Checkout") {
            agent {
                label 'ec2-linux-spot-slave'
            }
            steps {
                script {
                    notifyBuildDevops('STARTED')
                    notifyBuild()
                    config()
                }
            }
        }
        // stage("Static Analysis") {
        //     agent {
        //         label "ec2-linux-spot-slave"
        //     }
        //     steps {
        //         script {
        //             withAndroid {
        //                 withSonarQubeEnv(params.SONARQUBE_CONFIG_ID) {
        //                     sh "./gradlew sonarqube -Dsonar.projectKey=AppConsultorasAndorid -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN -PbuildType='${env.BRANCH_NAME}' -PflavorType='esika'"
        //                 }
        //             }
        //         }
        //     }
        // }
        stage("Build") {
            agent {
                label "ec2-linux-spot-slave"
            }
            steps {
                script {
                    withAndroid {
                        if(isReview()  || isIntegration() || true){
                            sh "./gradlew assemble${BUILD_TYPE.capitalize()}"
                            upload()
                        }else{
                            sh "./gradlew bundle${BUILD_TYPE.capitalize()}"
                            uploadBundle()
                        }
                    }
                }
            }
        }
        stage("e2e tests") {
            agent {
                label 'ec2-linux-spot-slave'
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'genymotion', passwordVariable: 'GENYMOTION_PASS', usernameVariable: 'GENYMOTION_USER')]) {
                        sh "docker run -d -v $WORKSPACE/e2e:/root/tmp -v $WORKSPACE/e2e/src/app/:/app/ -v $WORKSPACE/presentation/:/presentation/ -e TYPE=SaaS -e USER=$GENYMOTION_USER -e PASS=$GENYMOTION_PASS -e APPIUM=true --name container-appium-${env.BUILD_ID} budtmo/docker-android-genymotion:1.7-p1"
                        APPIUM_HOST = sh(returnStdout: true, script: "echo \"\$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container-appium-${env.BUILD_ID})\"").trim()
                        sleep 180
                        sh "docker run -v $WORKSPACE/e2e/:/tests -e APPIUM_APK_PATH=\"/presentation/build/outputs/apk/esika/${BUILD_TYPE}/consultoras-esika-${BUILD_TYPE}-1.4.9${NAME_APK}.apk\" -e APPIUM_HOST=\"${APPIUM_HOST}\" -e DEVICE_UDID=\"localhost:38727\" -e PLATFORM_VERSION=\"6.0\" --name codecept-${env.BUILD_ID} codeception/codeceptjs:2.0.4 /bin/bash -c \"npm install && npx codeceptjs run --grep '@LoginExitosoe' --reporter mochawesome\""
                    }
                }
            }
            post {
                always {
                    sh "docker stop container-appium-${env.BUILD_ID}"
                    sh "docker rm container-appium-${env.BUILD_ID}"
                    archiveArtifacts 'e2e/report/**'
                    zip zipFile: "report-${env.BUILD_ID}.zip", archive: false, dir: 'e2e/report'
                    withAWS(credentials:'devops-s3',region:'us-east-1') {
                        s3Upload(file:"report-${env.BUILD_ID}.zip", bucket:'velocity-s3', 'path':"tests/${env.JOB_NAME}/${env.BUILD_ID}/report.zip")
                    }
                }
            }
        }
        stage("Deploy") {
            agent {
                label "ec2-linux-spot-slave"
            }
            steps {
                script {
                    download()
                    if (isReview() || isIntegration()) {

                        def (nameEsika, apkEsika, mappingEsika) = filesEsika()
                        def (nameLBel, apkLBel, mappingLBel) = filesLBel()

                        notifyUrl("esika", nameEsika)
                        notifyUrl("lbel", nameLBel)

                    } else {

                        def (nameEsikaBundle, aabEsika, mappingEsikaBundle) = filesEsikaBundle()
                        def (nameLBelBundle, aabLBel, mappingLBelBundle) = filesLBelBundle()

                        withFastlane {
                            if (isMaster()) {
                                sh "fastlane android release marca:'esika' aab:'$aabEsika' apk:nil mapping:'$mappingEsikaBundle'"
                                sh "fastlane android release marca:'lbel' aab:'$aabLBel' apk:nil mapping:'$mappingLBelBundle'"
                            } else if (isStage()) {
                                sh "fastlane android beta marca:'esika' aab:'$aabEsika' apk:nil mapping:'$mappingEsikaBundle'"
                                sh "fastlane android beta marca:'lbel' aab:'$aabLBel' apk:nil mapping:'$mappingLBelBundle'"
                            } else if (isDevelop()) {
                                sh "fastlane android internal marca:'esika' aab:'$aabEsika' apk:nil mapping:'$mappingEsikaBundle'"
                                sh "fastlane android internal marca:'lbel' aab:'$aabLBel' apk:nil mapping:'$mappingLBelBundle'"
                            }
                        }
                        notifyUrl("esika", nameEsikaBundle)
                        notifyUrl("lbel", nameLBelBundle)

                    }
                }
            }
        }
    }
    post {
        success {
            script {
                notifyBuild("SUCCESS")
                deleteDir()
            }
        }
        aborted {
            script {
                notifyBuild("ABORTED")
                deleteDir()
            }
        }
        failure {
            script {
                notifyBuild("FAILED")
                deleteDir()
            }
        }
        unstable {
            script {
                notifyBuild("UNSTABLE")
                deleteDir()
            }
        }
        always{	
            notifyBuildDevops(currentBuild.result)	
        }
    }
}

/* ──────────────────────────────────────────────────────── */

def nameApk() {
    def date = new Date().format("yyyyMMdd")
    def letters = "-nb.${date}"
    def result
    if (isDevelop())
        result =""
    else
        result = letters
    result
}
def isMaster() {
    return env.BRANCH_NAME == "master"
}

def isStage() {
    return env.BRANCH_NAME.contains("release")
}

def isDevelop() {
    return env.BRANCH_NAME == "develop"
}

def isReview() {
    return env.BRANCH_NAME.contains("review")
}

def isIntegration() {
    return env.BRANCH_NAME.contains("integracion")
}

def buildType() {
    def branch = env.BRANCH_NAME
    def name
    if (isMaster())
        name = "release"
    else if (isStage())
        name = "stage"
    else if (isDevelop())
        name = "develop"
    else if (isIntegration())
        name = "develop"
    else
        name = "review"
    name
}

def config() {
    def CONFIG_URL = "https://s3-us-west-2.amazonaws.com/belcorp-apps/keys"
    def CONFIG_PROP = "key.properties"
    def CONFIG_KEY = "belcorp.jks"
    sh "chmod +x ./gradlew"
    sh "mkdir -p config"
    sh "curl -o config/$CONFIG_PROP $CONFIG_URL/old/$CONFIG_PROP"
    sh "curl -o config/$CONFIG_KEY $CONFIG_URL/old/$CONFIG_KEY"
    sh "curl -o config/google_play.json $CONFIG_URL/google_play.json"
}

def library() {
    sh "rm -rf ../BelcorpLibrary-Android"
    withCredentials([usernamePassword(credentialsId: "github_beltdfabrica_credentials", usernameVariable: "USERNAME", passwordVariable: "PASSWORD")]) {
        sh 'git clone -b qas_consultoras https://${USERNAME}:${PASSWORD}@github.com/TDFabrica/BelcorpLibrary-Android.git ../BelcorpLibrary-Android'
    }
}

def withAndroid(unit) {
  def image = docker.build("consultoras/android", "./docker/android")
  image.inside("-u 0") {
    withEnv(["CI=true"]) {
        config()
        library()
        sh "./gradlew clean"
        unit()
    }
  }
}

def withFastlane(unit) {
  def image = docker.build("consultoras/fastlane", "./docker/fastlane")
  image.inside("-u 0") {
    withEnv(["CI=true"]) {
        config()
        library()
        unit()
    }
  }
}

/* ──────────────────────────────────────────────────────── */

def notifyBuild(String buildStatus = "STARTED") {
  def not = load "ci/notify.groovy"
  not.send(buildStatus)
}

def notifyUrl(marca, app) {
  def not = load "ci/notify.groovy"
  def BUILD_TYPE = buildType()
  def date = new Date().format("yyyyMMdd")
  def url = "https://s3-us-west-2.amazonaws.com/belcorp-apps/Consultoras/$marca/$BUILD_TYPE/$date/$BUILD_TYPE/$app"
  not.sendUrlApk(marca, url, "SUCCESS")
}

def upload() {
  def s3 = load "ci/s3.groovy"
  def BUILD_TYPE = buildType()
  s3.upload("Consultoras", BUILD_TYPE)
}

def uploadBundle() {
  def s3 = load "ci/s3.groovy"
  def BUILD_TYPE = buildType()
  s3.uploadBundle("Consultoras", BUILD_TYPE)
}

def download() {
  def s3 = load "ci/s3.groovy"
  def BUILD_TYPE = buildType()
  s3.download("Consultoras", BUILD_TYPE, "tmp")
}

def filesEsika() {
  def apks = findFiles(glob: "tmp/esika/**/*.apk")
  def mappings = findFiles(glob: "tmp/esika/**/mapping.txt")
  def x = apks.length - 1
  return [apks[x].name, apks[x].path, mappings[0].path]
}

def filesEsikaBundle() {
  def aab = findFiles(glob: "tmp/esika/**/*.aab")
  def mappings = findFiles(glob: "tmp/esika/**/mapping.txt")
  def x = aab.length - 1
  return [aab[x].name, aab[x].path, mappings[0].path]
}

def filesLBel() {
  def apks = findFiles(glob: "tmp/lbel/**/*.apk")
  def mappings = findFiles(glob: "tmp/lbel/**/mapping.txt")
  def x = apks.length - 1
  return [apks[x].name, apks[x].path, mappings[0].path]
}

def filesLBelBundle() {
  def aab = findFiles(glob: "tmp/lbel/**/*.aab")
  def mappings = findFiles(glob: "tmp/lbel/**/mapping.txt")
  def x = aab.length - 1
  return [aab[x].name, aab[x].path, mappings[0].path]
}

def notifyBuildDevops(String buildStatus = 'STARTED') {	
    buildStatus = buildStatus ?: 'SUCCESS'	
    String buildPhase = (buildStatus == 'STARTED') ? 'STARTED' : 'FINALIZED'	
    commit = (buildStatus == 'STARTED') ? 'null' : sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'")	

    sh """curl -H "Content-Type: application/json" -X POST -d '{	
        "name": "${env.JOB_NAME}",	
        "type": "pipeline",	
        "urlArtifacts":"tests/${env.JOB_NAME}/${env.BUILD_ID}/report.zip",	
        "build": {	
            "phase": "${buildPhase}",	
            "status": "${buildStatus}",	
            "number": ${env.BUILD_ID},	
            "scm": {	
                "commit": "${commit}"	
            },	
            "artifacts": {}	
        }	
    }' https://devops.belcorp.biz/gestionar_despliegues_qa"""	
}
