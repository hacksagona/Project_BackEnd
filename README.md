# 갓생메이커

<br>

# 👏 프로젝트 소개

2030 사람들이 원하는 목표를 달성할 수 있도록 도와주는 애플리케이션으로, 

이용자가 원하는 미션을 설정하여 하루 하루 정해진 목표를 완수할 수 있도록 도와준다. 

자신이 오늘 하루 했던 미션 내용을 사진을 통해 공유하면 사람들이 보고 응원의 메세지를 보내 동기부여를 받게된다.

<br>

# ****⚙️**** 프로젝트 개요

- 프로젝트명 : 갓생메이커
- 개발 인원 : 프론트(React) 2명, 백엔드(Spring) 3명
- 개발 기간 : 2022.06.24 ~ 2022.08.04
- 개발 환경 :  React, React-Redux, React-Query, Springboot 2.6.8, JDK 11, Spring Security, Spring JPA
- 배포 환경 : Gradle, AWS S3, AWS EC2
- 웹 서버 : Tomcat 9.0.63
- 데이터베이스 : MySQL 8.0.29 (AWS RDS)
- 협업 도구 : Git, Slack, Notion, GatherTown, Kakao Talk

>**[Back-End Repository](https://github.com/hacksagona/Project_BackEnd)
><br>
>[팀 노션](https://www.notion.so/5-2de18c593e1e409d817f6ebe79b36bab)**
<br>

# ⭐️ 팀 구성
| 이름     | 깃허브 주소                                                | 포지션     |
|:--------:|:----------------------------------------------------------:|:-----------:|
| 김준호 | [https://github.com/kevinkim910408](https://github.com/kevinkim910408)                     | Frontend     |
| 고백재   | [https://github.com/baeg-jae](https://github.com/baeg-jae)                     | Frontend     |
| 고승준   | [https://github.com/kokomong2](https://github.com/kokomong2) | Backend     |
| 김규관   | [https://github.com/kwan97](https://github.com/kwan97)                     | Backend |
| 김성영   | [https://github.com/sungyoungk](https://github.com/sungyoungk)                     | Backend  |
| 이지유   |                     | UI & UX  |
<br>


# ****🍀**** 와이어프레임

- [[와이어프레임 팀 피그마]](https://www.figma.com/file/jwyyh1kwYKJVD9LoQMnypT/Untitled?node-id=0%3A1)

<br>

# 💛 API 설계

- [[API 설계 팀 노션]](https://www.notion.so/8991a35585474adcb5778b9d38e0e6df?v=0ec066f3839b4309ab13abe09d446377)
<br>

# ****🧩 아키텍처****
<img width="777" alt="스크린샷 2022-07-14 오후 6 12 00" src="https://user-images.githubusercontent.com/101084642/178947459-17940308-dbe9-4ebc-a2dd-f02de3e97ac1.png">

<br>

# ****💡 Trouble Shooting****
- **백엔드:**

1. 웹소켓 끊김 현상

채팅 대화중 간헐적으로 웹소켓이 끊기는 현상이 발생하였습니다. 
자꾸 연결이 끊기는데 이유를 모르겠어서 찾아보던중
웹소켓은 기본적으로 한번에 64kb 이상 데이터를 보낼 경우 보내지지 않는 경우가 있다는것을 알게됐습니다.
이럴경우 웹소켓 연결이 끊어져 버리는 현상이 있는데 혹시 이러한 이유 때문인건가 싶어서
WebSocketConfig에 configureWebSocketTransport를 추가하여 데이터 크기 제한을 높였습니다
![image](https://user-images.githubusercontent.com/105098279/182486382-0670a25f-dc87-4cef-a529-56a58106fcdc.png)

추가 후에 웹소켓 끊김 현상이 현저히 줄어든것을 확인하였습니다

2. 전역 예외처리

저희는 예외처리 메시지를 직접입력하여 처리하고있었습니다.
이렇게 하다보니 팀원들끼리 메시지 내용이 통일되지도 않고,
중복되는 예외도 많아서 불편함을 느꼈고,
클라이언트에 정확한 에러 상태를 보여주기도 힘들다고 판단하여
CustomException 클래스를 만들어 전역 예외처리를 하였습니다.

![image](https://user-images.githubusercontent.com/105098279/182487460-8ed579b9-d63e-4f07-8411-b52e075dba37.png)

![image](https://user-images.githubusercontent.com/105098279/182487492-16458d8d-a941-4a33-8b0b-16c1f7850697.png)



<br>

<!-- # ****💡 Trouble Shooting****
1. - **프론트엔드:**
    - **싱글페이지 어플리케이션에서 번들 사이즈가 커지면 로딩속도나 성능면에서 문제가 생길 수고, 모든 페이지를 처음부터 불러올 필요가 있을까 하는 생각이 있었습니다.**
        - **검색을 통해, 지금 사용하는 코드가 아닌 코드는 나중에 불러와 사용할 수 가 있는 코드 스플리팅이 있다고 해서 도입하여 페이지별 로딩속도를 개선하였습니다.**
        - **정확히는 페이스북에서  추천하는 Loadable Components를 사용하여 코드 스플리팅을 진행하였습니다.**
        ![zxc](https://user-images.githubusercontent.com/59503331/178904370-d7e2c699-d718-400a-8b15-74f3c5188c5c.PNG)          
        - **닉네임가입 페이지에서 닉네임 최대길이와, 현재길이를 보여주기위해 div태그 안에 input의 border를 안보이게해서 만들었는데, input을 focusing 했을때 상위 태그인 div에 접근을 하는 방법이 필요했습니다.**
        - **검색을 통해 &:focus-within 이라는 가상클래스를 찾아내어서 상위태그에 접근하여 스타일링을 적용했습니다.**
        ![asasd](https://user-images.githubusercontent.com/59503331/178904368-02b1a738-765d-4a59-9923-f575851a5af9.PNG) -->
            
