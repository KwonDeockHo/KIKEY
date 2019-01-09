# KIKEY
Android Studio Project/using json(php)/Demo Application

<a href="http://kon9383.godohosting.com/content/project/project_03.php" target="_blank">홈페이지 바로가기</a>


#Android #AndroidStudio #Application #Prototype #KiKEY

2017년 1학기 배달관련 업체의 요청에 따라 진행된 프로젝트로, 실질적인 어플리케이션을 제작하기 전 
프로토타입을 제작하여 사용자/요식업계 사업자들에게 필요한 기능을 파악하기 위한 프로토타입으로 제작되었다. 
가장 대표적인 기능 3~4가지 제작하여 이후 사용자/사업자들에게 피드백을 통하여 어플리케이션을 수정하는 방식으로 프로젝트를 진행하였다. 
본 어플리케이션의 특징은 기존의 배달 앱과는 달리 사업자가 매장에 이벤트/음식사진을 실시간으로 적용가능하고, 광고를 통한 포인트 비율이 증가함으로써 포인트를 획기적으로 획득할 수 있다.

[TOPIC]
<br>본 프로젝트는 오프라인 배달 사업에서 온라인으로 확장을 원허는 클라이언트의 요청으로 진행되었으며, 본격적인 메인 개발이 아닌 프로토타입을 개발하여 가맹주(사업자)/사용자의 니즈를 파악하기 위해 제작되는 프로토타입의 어플리케이션이다. 따라서 모든 기능이 아닌 핵심기능을 먼저 개발하고 추후 보완작업을 하여 사용자의(Needs)를 파악에 목적을 두고 있다.

[SKILL]
<ul>
<li> 1) 회원가입 폼에 맞는 사용자/사업자 DB 테이블 구조를 생성. </li>
<li> 2) 로그인 폼(Activity)을 생성하고 회원가입(사용자/사업자)으로 연결. </li>
<li> 3) 회원가입 폼은 사업자/사용자로 예외처리를 하고, PHP-JSON을 통해 데이터베이스와 연동한다.
   * JSON은 문자-값을 대응하는 통신 포맷.

   * 주소입력은 Daum 우편 API를 이용하여 팝업형식으로 주소를 입력하게한다. </li>

<li> 4) 메인페이지에서의 구조는 AppBar, TabBar, MainSector로 구역으로 구분하였고 AppBar와 TabBar는 include형식으로 Activity를 생성. </li>
<li> 5) 검색/검색리스트/상세페이지/상품페이지로 Activity를 구성하고 생성한다. </li>
<li> 6) 검색 리스트에는 쓰레드 관리를 통해 검색되어지는 시스템을 관리한다.
   * 메인페이지는 로딩창을 통해 Progress Bar를 생성. </li>

<li> 7) 메인페이지에서의 옵션창을 통해 상세페이지/광고/검색광고에 삽입할 사진을 입력할 페이지를 생성. </li>
<li> 8) AppBar의 Switch버튼을 이용하여 광고를 활성화/비활성화를 구분하고 광고를 모두 완료 시 팝업창과 함께 포인트를 적립한다. </li>
</ul>


[ACHIEVEMENT]
<br>사용자의 니즈(Needs)를 파악하기 위한 프로토타입으로 추후 본개발에 들어가기 이전에 사용자의 니즈(Needs)가 무엇인지에 중점을 두고 특정 Activity만을 개발하여 만들었다. 개발을 요청한 업체는 오프라인/온라인으로 사업을 예정하고 있고, 가맹주(사업자)에게 효과적인 홍보를 위해 프로토타입을 토대로 사업을 홍보를 함으로써 본격적인 사업 이전에 사용자 니즈를 정확하게 파악할 수 있다.

★ 개발언어/엔진
JAVA / ANDROID STUDIO / PHP / MYSQL

★ 개발인원
2명(역할 : 총괄(팀장))

★ 개발기간
2017.07 – 2017.08 / 45일

★ 개발내용
<ul>
<li> 프로젝트 구조 설계/정립화 </li>
<li> 회원가입(개인/사업자) 구현(jSON) - 예외처리구현 </li>
<li> DB 설계 / 연동(jSON) </li>
<li> 안드로이드 쓰레드 관리 </li>
<li> UI/ICON 최적화(re-sizeble) </li>
<li> 위치에 따른 거리계산/팝업창 구현 </li>
</ul>
