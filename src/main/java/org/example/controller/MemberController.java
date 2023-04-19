package org.example.controller;

import org.example.Container;
import org.example.dto.Member;
import org.example.service.MemberService;

public class MemberController {
  private MemberService memberService; // private로 MemberService 형태의 memberService 선언

  public MemberController() {
    memberService = Container.memberService; // memberService 는 Container의 memberService와 같다로 초기화
    // 왜 초기화 시켜주는지 질문해야함
  }

  public void join() { // 회원가입 로직
    String loginId; // loginId를 문자열로 선언
    String loginPw; // loginPw를 문자열로 선언
    String loginPwConfirm; // loginPwConfirm (로그인 비밀번호 확인)을 문자열로 선언
    String name; // name을 문자열로 선언

    System.out.println("== 회원 가입 =="); // 회원가입 시작시 "== 회원 가입==" 출력

    // 로그인 아이디 입력
    while (true) { // while문 true이기때문에 무한히 실행
      System.out.printf("로그인 아이디 : "); // 로그인시 사용할 아이디를 입력해달라는 문구 출력
      loginId = Container.scanner.nextLine().trim(); // 로그인시 사용할 아이디 입력받음

      if (loginId.length() == 0) { // 문자열 loginId의 길이가 0일경우
        System.out.println("로그인 아이디를 입력해주세요."); // 로그인 아이디를 입력해달라는 문구
        // loginId를 작성했으면 문자열의 길이가 절대 0이 될 수 없다.
        continue; // 이거 만족시킬때까지 다시 돌아감
      }

      boolean isLoginIdDup = memberService.isLoginIdDup(loginId);
      // boolean형태의 isLoginIdDup는 memberService의 isLoginIdDup에 입력받은 loginId를 대입해본다
      if (isLoginIdDup) { // loginId를 대입해보면 memberService를 타고 memberRepository에 가서 데이터베이스 중 loginId중에 중복되는게 있나 찾아봄
        System.out.printf("%s(은)는 이미 사용중인 로그인 아이디입니다.\n", loginId); // 이미 데이터베이스 안에 같은 아이디가 존재함으로 이미 사용중인 로그인 아이디입니다 라고 출력.
        continue; // 아이디 중복 안될때까지 반복
      }

      break; // 조건 충족시 이 로직에서 탈출
    }

    // 로그인 비밀번호 입력
    while (true) {
      System.out.printf("로그인 비밀번호 : "); // 비밀번호 입력받기위해 로그인 비밀번호를 입력해달라는 문구 출력
      loginPw = Container.scanner.nextLine().trim(); // 비밀번호를 입력받음

      if (loginPw.length() == 0) { // 조건문 loginPw의 길이는 0일때 (비밀번호를 입력하지 않았다면 loginPw의 길이는 0이 된다.)
        System.out.println("로그인 비밀번호를 입력해주세요."); // 로그인 비밀번호를 입력하지 않았기때문에 입력해달라는 문구 출력
        continue; // 위 조건을 만족할 때까지 반복
      }

      // 로그인 비밀번호 확인 입력
      boolean loginPwConfirmIsSame = true; // 로그인 비밀번호 확인이 true

      while (true) { //while문 true 걸려있으니까 무한 반복
        System.out.printf("로그인 비밀번호 확인 : "); // 로그인 비밀번호를 확인한다는 문구
        loginPwConfirm = Container.scanner.nextLine().trim(); // loginConfirm은 비밀번호 입력값 받음

        if (loginPwConfirm.length() == 0) { // 조건문 loginPwConfirm(비밀번호 확인 입력)의 길이가 0일때
          System.out.println("로그인 비밀번호를 입력해주세요.");
          continue;
        }

        if (loginPw.equals(loginPwConfirm) == false) {
          // 조건문 위에서 입력받은 loginPw(비밀번호)와 loginPwConfirm(비밀번호 확인)이 일치하지 않는 경우
          System.out.println("로그인 비밀번호가 일치하지 않습니다."); // 비밀번호 입력값과 비밀번호 확인 입력값이 다르다는 문구 출력
          loginPwConfirmIsSame = false; // loginPwConfirmIsSame이 false가 된다.
          break; // 입력값이 다를경우 다시 "로그인 비밀번호 확인:"이 뜰거고 만약 입력값이 같다면 loginPwConfirmIsSame = true;으로 while문 탈출
        }
        break;
      }

      if (loginPwConfirmIsSame) { // 만약 입력값이 같다면 loginPwConfirmIsSame = true;으로 while문 탈출
        break;
      }
    }

    // 이름 입력
    while (true) {
      System.out.printf("이름 : "); // 이름 입력해달라는 문구 출력
      name = Container.scanner.nextLine().trim(); // 이름 = 입력값으로 받겠다. 너의 이름을 입력해라

      if (name.length() == 0) { // 조건문 이름 입력값이 0일경우 이름을 안쓴것 → 다시 "이름을 입력해주세요" 문구 출력
        System.out.println("이름을 입력해주세요.");
        continue; // name.length() != 0일때까지 반복
      }
      break; // 조건을 충족시키면 이름 입력받는 로직 끝
    }

    int id = memberService.join(loginId, loginPw, name);
    // int형 id는 memberService의 join에 loginId, loginPw, name 형태로 입력값들을 담겠다.
    System.out.printf("%d번 회원이 등록되었습니다.\n", id);
    // 마지막으로 id번 회원이 등록되었다는 문구 출력 여기서 id는 로그인시 사용하는 id가 아닌 회원 고유id(Primary key)
  }


  public void login() {
    String loginId;
    String loginPw;

    System.out.println("== 로그인 ==");

    System.out.printf("로그인 아이디 : ");
    loginId = Container.scanner.nextLine().trim();

    if (loginId.length() == 0) {
      System.out.println("로그인 아이디를 입력해주세요.");
      return;
    }

    Member member = memberService.getMemberByLoginId(loginId);

    if (member == null ) {
      System.out.println("입력하신 로그인 아이디는 존재하지 않습니다.");
      return;
    }

    int loginTryMaxCount = 3;
    int loginTryCount = 0;

    // 로그인 비밀번호 입력
    while (true) {
      if(loginTryCount >= loginTryMaxCount) {
        System.out.println("비밀번호 확인 후 다음에 다시 시도해주세요.");
        break;
      }

      System.out.printf("로그인 비밀번호 : ");
      loginPw = Container.scanner.nextLine().trim();

      if (loginPw.length() == 0) {
        System.out.println("로그인 비밀번호를 입력해주세요.");
        continue;
      }

      if(member.getLoginPw().equals(loginPw) == false) {
        loginTryCount++;
        System.out.println("비밀번호가 일치하지 않습니다.");
        continue;
      }

      System.out.printf("\"%s\"님 환영합니다.\n", member.getName());
      Container.session.login(member);

      break;
    }
  }

  public void whoami() {
    if(Container.session.isLogined() == false) {
      System.out.println("로그인 상태가 아닙니다.");
    }
    else {
      System.out.println(Container.session.loginedMember.getLoginId());
    }
  }

  public void logout() {
    Container.session.logout();
    System.out.println("로그아웃 되었습니다.");
  }
}
