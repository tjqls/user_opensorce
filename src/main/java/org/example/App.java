package org.example;

import org.example.controller.ArticleController;
import org.example.controller.MemberController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {  //
  public void run() {  // main에서 App().run()실행 되면서 여기로 진입됨
    Container.scanner = new Scanner(System.in); // Container 안에서 선언된 변수 scanner은 문자를 입력받는 걸로 만듬 여기서
    Container.init();  // 아까는 몰랐지만 밑에 articlecontroller랑 membercontroller을 사용하기 때문에 여기서 사용할거라고 선언

    while (true) { // 이제 while 반복문 실행 true이기때문에 무한히 실행한다.
      System.out.printf("명령어) ");  // "명령어)" 출력
      String cmd = Container.scanner.nextLine(); // 문자열 cmd는 이제 입력값을 받는것 이라고 만듬

      Container.rq = new Rq(cmd); // 이제 rq에 cmd 즉 입력값을 대입할거임 ㅇㅇ

      // DB 연결 이거 솔직히 모르겠음 연결을 이렇게 한다는데 왜 이걸 써야하고 저걸 여기다 왜 써야하는지는 모름
      Connection conn = null;

      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        System.out.println("예외 : MySQL 드라이버 로딩 실패");
        System.out.println("프로그램을 종료합니다.");
        break;
      }

      String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

      try {
        conn = DriverManager.getConnection(url, "sbsst", "sbs123414");

        Container.conn = conn;

        // action 메서드 실행
        action(Container.rq, cmd);

      } catch (SQLException e) {
        System.out.println("예외 : MySQL 드라이버 로딩 실패");
        System.out.println("프로그램을 종료합니다.");
        break;
      } finally {
        try {
          if (conn != null && !conn.isClosed()) {
            conn.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      // DB 연결 끝
    }
    Container.scanner.close();
  }

  private void action(Rq rq, String cmd) {  // action 함수 여기에 Rq rq를 받고 입력값도 매개변수로 받는다)
    if (rq.getUrlPath().equals("/usr/member/join")) { // 명령어 "회원가입"과 동일
      Container.memberController.join(); // memberController안에 있는 join함수를 사용하겠다. join 함수 로직이 회원가입하는 로직
    } else if (rq.getUrlPath().equals("/usr/member/login")) { // 명령어 "로그인"과 동일
      Container.memberController.login(); // memberController안에 있는 login함수 사용 login함수 로직이 로그인 하는 로직
    } else if (rq.getUrlPath().equals("/usr/member/logout")) { // 담타 명령어 "로그아웃"과 동일
      Container.memberController.logout(); // memberController안에 있는 logout함수 사용 로그아웃하는 로직이 담겨있다.
    } else if (rq.getUrlPath().equals("/usr/member/whoami")) {  // 아마 memberController안에 있는 로그인이 되어있는지 확인하는 로직같음
      Container.memberController.whoami(); // memberController안에 있는 로그인 확인 로직
    } else if (rq.getUrlPath().equals("/usr/article/write")) { // 로그인확인 했을때 로그인중이 아닌경우
      Container.articleController.write(); // if문으로 써놓은거 보니까 안에 조건이 isLogined() == false 로그인이 안되어있으면 로그인 후 이용해주세요 라는 문구 출력 되게 해놨음
    } else if (rq.getUrlPath().equals("/usr/article/list")) { // 명언앱 "목록" 같은 느낌
      Container.articleController.showList(); // 게시물 리스트 보여주는데? 확인해보니 안에서 페이지, 게시물 존재여부 게시글 번호 작성날짜 작성자 제목등을 보여주는 로직같음
    } else if(rq.getUrlPath().equals("/usr/article/detail")) { // 리스트를 명확하게 보여주는 명령어
      Container.articleController.showDetail(); // 위에서 게시물 리스트를 좀더 명확하게 번호 등록날짜 수정날짜 작성자 조회수 제목 내용을 보여준다.
    } else if (rq.getUrlPath().equals("/usr/article/modify")) { // 게시글 수정할때 명령어
      Container.articleController.modify(); // 찾아봤는데 일단 로직이 로그인 되어있는지 확인하고 고유id 가지고와서 게시글 찾아와서 수정하는거 같음
    } else if (rq.getUrlPath().equals("/usr/article/delete")) { // 게시글 삭제할때 명령어
      Container.articleController.delete(); // 게시글 수정이랑 비슷하다 게시글 고유id 가져와서 수정하는것이 아닌 삭제
    } else if (cmd.equals("system exit")) { // 시스템 종료 명령어
      System.out.println("시스템 종료"); // 시스템 종료 문구 출력
      System.exit(0); //아마 여기서 함수가 끝나는거 같다.
    } else {
      System.out.println("명령어를 확인해주세요."); // 여기는 위에서 나온 명령어 외에 것들을 쓰면 출력되는 문구 같다 아마도
    }
    return;
  }

}
