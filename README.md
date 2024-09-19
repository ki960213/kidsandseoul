# 키즈앤서울

## 프로젝트 개요

서울시 공공데이터 활용 창업경진대회에 참여하기 위해 개발된 앱입니다.  
아이를 키우는 부모를 대상으로 서울시에서 제공하는 보육 시설 정보, 사용자 리뷰 및 게시글을 공유할 수 있는 커뮤니티 기능을 제공합니다.

- **프로젝트 기간**: [2024.04 ~ 2024.06](https://github.com/ki960213/kidsandseoul/graphs/contributors)
- **플레이 스토어 링크**: [키즈앤서울 다운로드](https://play.google.com/store/apps/details?id=com.ki960213.kidsandseoul)
- **참여 인력**:
  - 개발자: 본인 1명
  - 기획자: 1명
  - 디자이너: 1명

## 기술 스택

- **언어**: Kotlin
- **프레임워크**: Android
- **비동기 처리**: Coroutine, Flow
- **네트워크 통신**: Retrofit2, Kotlinx Serialization
- **지도 서비스**: Naver Map SDK
- **데이터베이스/이미지 저장소/인증**: Firebase (Firestore, Cloud Storage, Firebase Authentication)
- **이미지 로딩**: Glide
- **UI 구성**: 뷰 시스템, Data Binding
- **화면 이동**: Jetpack Navigation
- **무한 스크롤 구현**: Paging3
- **의존성 주입**: Hilt

백엔드 관련 정보는 [여기](https://github.com/ki960213/kidsandseoul-server)에서 확인할 수 있습니다.

## 아키텍처

- [**구글 권장 아키텍처**](https://developer.android.com/topic/architecture?hl=ko)
- **싱글 액티비티 아키텍처**
- **MVVM**

## 기능

<details>
  <summary><h3>홈 화면</h3></summary>

  - 시설명 검색 기능
  - 시설 카테고리별 빠른 검색 기능
  - 관심 시설 목록 조회

  <p align="center">
    <img src="https://velog.velcdn.com/images/ki960213/post/97d82ca6-abe4-4900-8e7a-c10cd076ab0b/image.png" alt="drawing" style="width:30%;"/>
  </p>
</details>

<details>
  <summary><h3>지도 화면</h3></summary>

- **카테고리 필터링**: 현재 지도에 표시된 시설들만 필터링
  - 지도를 움직이면 "현 지도에서 검색" 버튼이 나타남
- **현재 위치로 이동**: GPS 기반으로 현재 위치를 중심으로 지도 이동
- **시설명 검색**
- 시설 정보 조회 및 상세 화면으로 이동
- 검색 결과가 없을 경우 토스트 메시지로 알림
- 뒤로 버튼 클릭 시 필터링 조건 제거

| 카테고리 검색 | 지도 움직였을 때 | "강남" 검색 | 시설 정보 조회 | 검색 결과가 없을 때|
|--|--|--|--|--|
|![](https://velog.velcdn.com/images/ki960213/post/9ce0bdb8-aaed-4185-83e2-ba8ecdafa5de/image.png)|![](https://velog.velcdn.com/images/ki960213/post/058d5fb8-2b81-4ec6-b342-9c1a12088c96/image.png)|![](https://velog.velcdn.com/images/ki960213/post/9273de0a-2f05-4d75-aab7-7f2f0f6f48b6/image.png)|![](https://velog.velcdn.com/images/ki960213/post/2854bfb2-83f4-4df4-973a-eaa843e93796/image.png)|![](https://velog.velcdn.com/images/ki960213/post/d5b4ca6d-df4d-464b-a7e8-c934524a1187/image.png)

</details>

<details>
  <summary><h3>커뮤니티 화면</h3></summary>

- 후기 및 게시글 목록 조회

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/34a91646-cf90-4fac-8d2f-c2277f5b83cc/image.png" alt="drawing" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/7a056d90-00f5-45ab-a54d-d848e06f0a31/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>내 프로필 화면</h3></summary>

- 프로필 수정 화면으로 이동
- 아이 정보 추가 화면으로 이동
- 관심 시설 목록 조회
- 내가 작성한 리뷰 목록 조회
- 팔로워 및 팔로잉 목록 조회
- 내가 작성한 게시글 목록 조회
- 서비스 이용약관 및 개인정보 처리방침 화면으로 이동
- 로그아웃 및 회원탈퇴 기능

| 프로필 화면 | 로그아웃 버튼 클릭 시 | 회원탈퇴 버튼 클릭 시|
|--|--|--|
|![](https://velog.velcdn.com/images/ki960213/post/c0074520-46ff-4ebb-810d-c1cc1613df95/image.png)|![](https://velog.velcdn.com/images/ki960213/post/ee64629b-1ac8-4d0f-b176-f89332ddbd8f/image.png)|![](https://velog.velcdn.com/images/ki960213/post/b2723015-3cba-4554-a699-493303bfb81e/image.png)|

</details>

<details>
  <summary><h3>프로필 수정 화면</h3></summary>

- 프로필 이미지 설정
- 닉네임 변경
- 아이 정보 추가 화면으로 이동
- 현재 사는 지역 변경

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/1ac9f634-9f75-4666-9b00-b713cb4e5400/image.png" alt="drawing" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/70bd5181-ff96-4c18-b2dd-1a27960f24af/image.png" alt="drawing" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/4273003d-ea75-4e4c-a668-464eca85cb16/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>아이 등록 화면</h3></summary>

- 이름, 성별, 생년월일, 지역 설정 기능

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/31989134-2b59-45ef-b9ee-fe03d7a4dba1/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>정보 조회 화면</h3></summary>

- 내 관심 목록 조회
- 내 리뷰 목록 조회
- 팔로워 및 팔로잉 목록 조회
- 내가 작성한 게시글 목록 조회

| 내 관심 목록 화면 | 내 리뷰 목록 화면 | 내 팔로워 화면 | 내 게시글 목록 화면 |
|--|--|--|--|
|![](https://velog.velcdn.com/images/ki960213/post/b420cd4d-36f5-41c2-b650-2a46febd23be/image.png)|![](https://velog.velcdn.com/images/ki960213/post/b90d6c27-2bdd-4c93-81b3-0a4da5357a7d/image.png)|![](https://velog.velcdn.com/images/ki960213/post/894c71e2-fba6-416f-b5aa-c74eee8fb406/image.png)|![](https://velog.velcdn.com/images/ki960213/post/e124c9e9-391a-4102-b144-62991be0ddb4/image.png)|

</details>

<details>
  <summary><h3>시설 목록 화면</h3></summary>

- 시설명 검색 기능
- 후기순, 별점순 정렬
- 무한 스크롤 지원

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/bc2885b0-9702-4474-8d95-072f20898538/image.png" alt="drawing" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/cb67bc42-d7c7-409c-9b07-6fddbe8c03d7/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>시설 필터 설정 화면</h3></summary>

- 카테고리별 다양한 필터 조건 설정
  - 보육시설: 토요일 운영 여부 필터
  - 키즈카페: 요일별 운영 여부 필터
- 등록된 아이의 나이와 지역 정보 자동 불러오기
- 나이 및 지역별 필터링(지역 다중 선택 가능)

| 보육 시설 | 키즈카페 | 아이 정보 불러오기 | 지역 다중 선택 |
|--|--|--|--|
|![](https://velog.velcdn.com/images/ki960213/post/72d2b2ee-5b42-4099-9827-5ecb2aaaded6/image.png)|![](https://velog.velcdn.com/images/ki960213/post/d185e0c6-2f20-424e-b189-0c2954134718/image.png)|![](https://velog.velcdn.com/images/ki960213/post/ec95065e-48a1-4049-bcf4-af23b6eec328/image.png)|![](https://velog.velcdn.com/images/ki960213/post/621369de-a6b7-4603-8f86-7205ab8e93fc/image.png)|

</details>

<details>
  <summary><h3>시설 상세 화면</h3></summary>

- 관심 설정/해제
- 주소 복사 기능
- 외부 지도 앱으로 이동
- 영업 정보 제공
- 카테고리 소개 및 시설 상세 링크 이동
- 후기 목록 조회 및 본인 후기 삭제 기능
- 후기 작성 화면으로 이동

| 상세 화면 | 외부 지도 앱 검색 | 소개 및 상세 링크 | 후기 목록 |
|--|--|--|--|
|![](https://velog.velcdn.com/images/ki960213/post/746b391e-6771-4ee0-a381-ba3637b67d55/image.png)|![](https://velog.velcdn.com/images/ki960213/post/63f00487-f28b-43f7-9a66-d876a439c063/image.png)|![](https://velog.velcdn.com/images/ki960213/post/d458a858-7660-4116-8284-560d032bcd23/image.png)|![](https://velog.velcdn.com/images/ki960213/post/661911da-d4f8-46b4-834f-586707ca8775/image.png)|

</details>

<details>
  <summary><h3>후기 작성 화면</h3></summary>

- 별점 설정
- 이미지 추가(최대 10장)
- 후기 작성
- 작성 중 안전한 나가기 기능(이미지 추가 또는 내용 작성 시 뒤로 가기 시 다이얼로그 표시)

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/53d76599-d642-44be-8d5b-28d7453e2ddb/image.png" alt="drawing" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/4ea924b1-4e24-453a-8e65-136bf50fd0a4/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>게시글 상세 화면</h3></summary>

- 게시글/댓글 작성자의 프로필 화면으로 이동
- 좋아요 및 좋아요 해제
- 댓글 작성
- 본인 게시글/댓글 삭제 가능

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/95e7c3d4-e6dc-4699-8432-f22cf74d7383/image.png" alt="drawing" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>게시글 작성 화면</h3></summary>

- 제목 및 내용 작성
- 작성 중 안전한 나가기 기능(내용 작성 시 뒤로 가기 시 다이얼로그 표시)

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/71a90a65-ce32-4e5b-9b95-6600e03b053c/image.png" style="width:30%;"/>
  <img src="https://velog.velcdn.com/images/ki960213/post/32a17d25-e977-4d19-8fea-f5a067ad800c/image.png" style="width:30%"/>
</p>

</details>

<details>
  <summary><h3>프로필 화면</h3></summary>

- 다른 부모의 프로필, 아이 정보, 작성 게시물, 작성 리뷰 조회
- 팔로우 및 언팔로우 기능

<p align="center">
  <img src="https://velog.velcdn.com/images/ki960213/post/962327b9-39e4-4f7c-92af-2e668f18cb42/image.png" style="width:30%;"/>
</p>

</details>

<details>
  <summary><h3>앱 기능</h3></summary>

- 앱 업데이트 가능하면 앱 진입 시 구글 플레이스토어로 이동
- 앱 종료 시 두 번 연속 뒤로 가기를 눌러야 종료
- 모든 데이터 변경(예: 프로필 정보, 관심 시설) 즉시 반영
- 비로그인 시에도 앱 탐색 가능

| 앱 업데이트 | 안전한 나가기 | 비로그인 시 게시글 상세 | 해당 화면에서 즉시 로그인 |
|--|--|-----------------------------------------------------------------------------------------------|--|
|![](https://velog.velcdn.com/images/ki960213/post/f08e0f66-d745-4c65-a9b4-85e303b1fa75/image.png)|![](https://velog.velcdn.com/images/ki960213/post/c6bf2924-7703-4678-be84-398937cb7b84/image.png)| ![](https://velog.velcdn.com/images/ki960213/post/8e544b92-0484-4089-8ced-791d2ab7f723/image.png) |![](https://velog.velcdn.com/images/ki960213/post/60b6e64a-1148-43e3-a5c7-628d62bf103a/image.png)|

</details>
