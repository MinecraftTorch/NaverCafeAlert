
# Naver Cafe Alert

![language](https://img.shields.io/github/languages/top/MinecraftTorch/NaverCafeAlert) [![CodeFactor](https://www.codefactor.io/repository/github/minecrafttorch/navercafealert/badge)](https://www.codefactor.io/repository/github/minecrafttorch/navercafealert) ![downloads](https://img.shields.io/github/downloads/MinecraftTorch/NaverCafeAlert/total) ![enter image description here](https://img.shields.io/github/license/MinecraftTorch/NaverCafeAlert) ![enter image description here](https://img.shields.io/github/v/release/MinecraftTorch/NaverCafeAlert)

마인크래프트 서버에서 일어나는 일들을 실시간으로 네이버 카페에 올리는 플러그인입니다.

## ScreenShot
사진이 많습니다. [여기](/Screenshots.md)를 확인해주세요.

## Feature
- **밴 처리시** 네이버 카페에 자동으로 등록
-  **경고 처리시** 네이버 카페에 자동으로 등록
- **채팅차단 처리시** 네이버 카페에 자동으로 등록
- HTML 태그 커스터마이징 지원
- 네이버 공식 API 사용
- 설정 가능한 `config.yml` 과 `forms.yml`
- 원하는게 있으시면 말씀해주세요~*

## Version
- NaverCafeAlert 는 버전에 따른 API를 사용하지 않습니다. 만일 특정 버전이 필요하시면, `pom.xml` 에서 버전을 바꿔주시면 됩니다.

동작 확인 버전:
- 1.19.1
- 1.19
- 1.18
- 이론상 1.15 ~ 는 다 됩니다.

## Supported Plugins
NaverCafeAlert 는 처벌 플러그인들과 연결하여 자동으로 네이버 카페에 게시글을 작성합니다. 현재 지원하는 처벌 플러그인들은 다음과 같습니다:

- [LiteBans](https://www.spigotmc.org/resources/litebans.3715/) : 공식 API 사용 (100% 지원)

## Download & Installation
1. 프로젝트 [Release 페이지](https://github.com/MinecraftTorch/NaverCafeAlert/releases/latest)를 방문해주세요.
2. 최신 버전의 `.jar` 파일을 받고 `./plugins` 폴더에 넣어주세요.
3. `config.yml` 과 `forms.yml` 의 설정을 바꿔야 합니다. [이 페이지](/guide.md)를 참고해주세요.

## Issues & PR
만일 플러그인을 사용하시다가 발생하는 버그들은 [Issues](https://github.com/MinecraftTorch/NaverCafeAlert/issues) 로 **양식에 맞춰** 제보해주세요.

PR은 언제나 환영입니다. 혼자 버그를 고치셨거나, 더 좋은 코드를 발견하시면 PR을 해주세요. 검토 후 merge 하겠습니다.
