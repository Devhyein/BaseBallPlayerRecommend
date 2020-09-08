# 09/07
## 주제
야구 선수 스카우팅 시스템
* 야구 선수들의 각종 기록을 시각화, 비교하고 드래프트, 트레이드, 스카우트 대상 선수를 추천해주는 시스템

## 기술 스택
* Backend (API Server) - SpringBoot
* Backend (Data Analysis Server) - django
* Web Server - Nginx: SpringBoot, django의 기본 내장 서버를 사용하는 것보다는 Nginx와 연동해서 사용하는 것이 가용성 +
* DBMS - MariaDB or PostgreSQL
* Frontend - Vue.js
    * 추후 고려: SpringBatch / Redis?