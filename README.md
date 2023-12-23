애플리케이션을 띄우기 위해 기본적으로 설정해야 하는 속성 및 테이블

### 소개
이 프로젝트에서는 총 2개의 데이터베이스를 사용합니다. 
하나는 본 애플리케이션이 관리하는 DB    
나머지는 Producer 가 메시지를 처리하기 위해 접근해야 하는 Consumer 시스템의 DB

본 애플리케이션이 관리하는 DB 의 테이블은 모두 `@Entity` 로 정의되어 있습니다. `spring.jpa.hibernate.ddl-auto:create` 를 통해 초기 세팅을 합니다.  

### Consumer 시스템 DB 스키마
Consumer 시스템의 DB에는 단 1개의 테이블만 존재합니다. MYYSQL 기준 스키마는 아래와 같습니다. 
```
CREATE TABLE PAY_TO_ORDERS (
    order_pk INT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(255) NOT NULL,
    sent_time TIMESTAMP NOT NULL,
    task_type VARCHAR(255) NOT NULL
);
```

### application.yml
다음은 기본적으로 설정해야 할 `yml` 파일의 key 값들입니다. (권장) 표시가 없는 것은 필수 값들입니다.

```
# 본 애플리케이션 DB 접근을 위한 정보
spring:
  datasource:
    driver-class-name: 
    url: 
    username: 
    password:

  batch:
    job:
      enabled: false # 권장
    jdbc:
      initialize-schema: always or never (초기 세팅 always 이후 never)  # 권장
# consumer 시스템 DB 접근을 위한 정보
3rd-party:
  order:
    datasource:
      url: 
      username: 
      password: 
# 메시지 폴링 간격 (본 프로젝트에서는 PollableChannel을 사용합니다.)
poller:
  interval: {number type}

```




