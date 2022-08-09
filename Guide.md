
# Guide
NaverCafeAlert 를 사용하는데 필요한 가이드입니다.

## 1. 네이버 카페와 플러그인 연동
이 플러그인은 **공식** [네이버 로그인 API](https://developers.naver.com/docs/login/api/api.md)와 [네이버 카페 API](https://developers.naver.com/docs/login/cafe-api/cafe-api.md)를 사용합니다. 플러그인을 사용하기 위해서는 네이버 계정에 로그인해야합니다.

>**여러분의 아이디와 비밀번호를 절대 수집하지 않습니다. 뿐만 아니라 알 수 있는 방법이 없습니다!**

플러그인을 사용하기 위해서는 **Refresh Token** 이 필요합니다. https://mctor.ch/minecraftcafe/ 에 방문하셔서 네이버 로그인을 해주세요.

* 웹 페이지는 예고 없이 디자인이 변동될 수 있습니다.

![1](https://user-images.githubusercontent.com/49092508/183603775-130cfc90-fffc-42fa-a7d1-24d9e7e6690c.png)

웹 사이트는 이렇게 생겼습니다. 초록색 로그인 버튼을 눌러주세요.

![2](https://user-images.githubusercontent.com/49092508/183604051-68f831fe-1d16-4730-85cf-2218439b24ba.png)

네이버 로그인을 진행해주세요. 평소에 사용하시던 네이버 계정을 사용하셔도 됩니다.

![3](https://user-images.githubusercontent.com/49092508/183605296-bd79e88b-1889-4aaa-a8ce-795ede22ac32.png)

다음에는 이렇게 권한을 제공하는 창이 뜹니다. 
- **필수 제공 항목**
- **추가 제공 항목**

이렇게 두가지를 **반드시** 선택 후 동의하기를 눌러주세요.

**만일 체크되지 않으면, 카페에 게시글을 작성할 수 없습니다!**

![4](https://user-images.githubusercontent.com/49092508/183605944-dc454e2e-f8d6-4630-ab9e-4c65372825f9.png)

만일 성공적으로 로그인이 된다면 이런 화면이 나옵니다. 텍스트 창에 있는 refresh token 을 확인해주세요.

## 2. 카페 ID와 게시판 ID 확인
네이버 카페에 게시글을 작성하기 위해서는 다음의 두가지 정보가 필요합니다.
- 네이버 카페의 ID *(유저의 ID 처럼 네이버 카페에도 ID가 있습니다)*
- 작성할 게시판의 ID *(마찬가지로 게시판에도 ID가 존재합니다)*

![5](https://user-images.githubusercontent.com/49092508/183606841-83450b63-bc4a-4193-94e3-13179485d668.png)
게시글을 자동으로 작성하실 네이버 카페에 방문해주세요. 그리고는 작성하고 싶은 게시판을 눌러주세요.

> 예를들어 카페의 "자유게시판" 에 작성하고 싶으시면 자유게시판을 눌러주세요.

![6](https://user-images.githubusercontent.com/49092508/183606848-7320ae3e-26e8-4878-8af0-49953dde9b45.png)

해당 게시판에 들어가셔서 **글쓰기** 를 눌러주세요. 그리고 나서 주소창을 봐주세요.

```
https://cafe.naver.com/ca-fe/cafes/30772403/menus/1/articles/write?boardType=L
```
주소창이 이런 값이라면
```
카페 ID : 30772403
작성할 게시판의 ID : 1
```
입니다.

## 3. Config.yml 설정
플러그인을 사용하기 위해서는 `config.yml`을 설정해야 합니다. 가장 중요한 부분은 다음의 값들입니다
- `cafeRefreshToken`: **1. 네이버 카페와 플러그인 연동** 에서 발급받은 refresh token. 아까 자정한 값을 그대로 복사 붙여넣기 해주세요. **띄어쓰기 및 오탈자가 생기면 에러가 납니다**
- `cafeClubId`: **2. 카페 ID와 게시판 ID 확인** 에서 발급받은 카페의 ID.
- `cafeBanBoardId`: 밴 발생시 게시글을 작성할 게시판의 ID.
- `cafeWarningBoardId`: 경고 발생시 게시글을 작성할 게시판의 ID.
- `cafeMuteBoardId`: 채팅차단 발생시 게시글을 작성할 게시판의 ID.

## 4. 서버 재시작 (또는 reload)
만일 모든 설정이 올바르게 설치되었을 경우, 자동으로 밴, 경고, 채팅차단의 경우 자동으로 알림이 네이버 카페로 작성됩니다.