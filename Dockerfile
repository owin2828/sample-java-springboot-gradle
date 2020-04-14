FROM 10.217.65.18:5000/java:8
EXPOSE 8080
ADD build/libs/Member_Hibernate-0.0.1-SNAPSHOT.jar member.jar
ENTRYPOINT ["java", "-jar", "/member.jar"]
