package org.example.controller;


import org.example.Container;
import org.example.dto.Article;
import org.example.service.ArticleService;

import java.util.List;

import static org.example.Container.rq;

public class ArticleController { //ArticleController 시작

  private ArticleService articleService;  // private로 ArticleService 설계도대로 articleService 선언

  public ArticleController() {  // ArticleController 함수
    articleService = Container.articleService; // 아까 ArtivleService 선언 한걸 Container에 있는 articleService로 초기화
  }

  public void write() { //write 함수 로그인 확인하는 로직
    if(Container.session.isLogined() == false) { // session클래스 보니까 고유ID인 loginedMemberId를 -1이 아닐때
      System.out.println("로그인 후 이용해주세요."); //session안에있는 isLogined가 -1일때는 로그아웃상태 -1이아닐경우 로그인 상태
      return;
      // 조건문 해석해보면 loginedMemberId가 -1일경우 라고 되어있음
      // 조건문으로 loginedMemberId가 -1이 아닐경우 로그인상태가 아니니까 로그인 후 이용해주세요라는 문구 출력
     }

    System.out.println("== 게시물 등록 =="); // 위에 조건문을 충족하여 로그인 상태일때 싱행 "== 게시물 등록 == 문구" 출력됨
    System.out.printf("제목 : ");  // "제목 : " 출력
    String title = Container.scanner.nextLine(); // 제목을 입력받음
    System.out.printf("내용 : "); // "내용 : " 출력
    String body = Container.scanner.nextLine(); // 내용을 입력받음

    int memberId = Container.session.loginedMemberId; // int형 memberId = session클래스의 loginedMemberId와 같다 여기서 loginedMemberId는 회원의 고유 번호인거 같다
    int id = articleService.write(memberId, title, body);
    // int형 id = articleService의 write이다. 여기서 write는 int memberId, String title, String body를 담는다.
    // 그리고 write는 articleRepository의 write를 return하는데 articleRepository의 write를 보면 데이터베이스에 INSERT INTO로 입력받은 값을 각 column에 넣는거같다.
    System.out.printf("%d번 게시물이 등록되었습니다.\n", id); // 등록 완료시 문구 출력
  }
  public void showList() { // 게시물 리스트 보여주는 함수 (설명 받아야함)
    System.out.println("== 게시물 리스트 =="); // "== 게시물 리스트 ==" 문구 출력
    int page = rq.getIntParam("page", 1); // int형 page는 rq의 getIntParam와 같다 getIntParam는 page라는 매개변수와 기본값 1을 받는다?
    String searchKeyword = rq.getParam("searchKeyword", "");
    // 문자열 searchKeyword는 rq의 getParam과 같다 여기서도 매개변수 serchKeyword를 받고 기본값은 공백? (설명 받아야함)
    int pageItemCount = 10;

    // 임시
    pageItemCount = 5;

    List<Article> articles = articleService.getForPrintArticleById(page, pageItemCount, searchKeyword);
    // list선언 형태는 Article 이라는 articles 리스트 = articleService의 getForPrintArticleById이다  getForPrintArticleById은 page, pageItemCount, serchKeyword를 담는다.
    // 솔직히 잘 모르겠다 이게 페이지 확인하는 건지 페이지를 만드는 건지 질문

    if (articles.isEmpty()) { // 조건문 위에서 선언한 list articles에서 뒤에 설명 봐라 (isEmpty() 메소드는 문자열이 빈 값이면 true, 비어있지 않으면 false를 리턴하는 메소드입니다)
      System.out.println("게시물이 존재하지 않습니다."); // 게시물이 빈값이면 "게시물이 존재하지 않습니다." 출력
      return;
    }

    System.out.println("번호 / 작성날짜 / 작성자 / 제목"); // 게시물에 값이 있다면 "번호 / 작성날짜 / 작성자 / 제목" 출력

    for (Article article : articles) {  //향상된 for문 list형식의 articles에 있는 것들을 Article형태의 article로 처음부터 끝까지 싹다 나눔
      System.out.printf("%d / %s / %s / %s\n", article.id, article.regDate, article.extra__writerName, article.title);
      // 그래서 id, regDate, extra__writerName,title 의 각각의 값들을 출력 명언앱 목록 출력이랑 비슷함
    }
  }

  public void showDetail() {  // shoDetail 함수 위에서 보여준 게시물 리스트를 좀더 세세하게 구분하여 보여줌
    int id = rq.getIntParam("id", 0); // int 형 id는 rq의 getIntParam와 같다 getIntParam는 page라는 매개변수와 기본값 0을 받는다?
    //왜 위에서는 defaultValue를 1을 받고 여기서는 0을 받는가 (질문해야함)
    if (id == 0) { //조건문 id가 0일때
      System.out.println("id를 올바르게 입력해주세요."); // "id를 올바르게 입력해주세요" 출력
      return;
    }

    articleService.increaseHit(id); // hit column은 어떤 역할을 하나요?
    Article article = articleService.getArticleById(id);
    // 아마 회원 고유ID로 데이터 베이스에서 회원 정보? 를 가져오는거 같음 오늘도 담배만 늘어갑니다

    if (article == null) { //article 값이 null일 경우
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      // 게시글 존재하지 않는다 출력 위에서 가져온 정보중 게시글을 담당하는 변수가 비어있을경우인거 같아요
      return;
    }

    // 요기는 게시글이 있는 경우 번호, 등록날짜, 수정날짜, 작성자, 조회수, 제목, 내용이 출력됨
    System.out.printf("번호 : %d\n", article.id);
    System.out.printf("등록날짜 : %s\n", article.regDate);
    System.out.printf("수정날짜 : %s\n", article.updateDate);
    System.out.printf("작성자 : %s\n", article.extra__writerName);
    System.out.printf("조회수 : %d\n", article.hit);
    System.out.printf("제목 : %s\n", article.title);
    System.out.printf("내용 : %s\n", article.body);
  }

  public void modify() { // 수정 하는 로직
    if(Container.session.isLogined() == false) {  // session클래스 보니까 고유ID인 loginedMemberId를 -1이 아닐때
      System.out.println("로그인 후 이용해주세요."); //session안에있는 isLogined가 -1일때는 로그아웃상태 -1이아닐경우 로그인 상태
      return;
    }

    int id = rq.getIntParam("id", 0);
    // int 형 id는 rq의 getIntParam와 같다 getIntParam는 id라는 매개변수와 기본값 0을 받는다?

    if (id == 0) { // 고유 id가 1부터 쌓이는거 같음 그래서 0일경우 id가 찾을수 없는거같다
      System.out.println("id를 올바르게 입력해주세요.");
      return;
    }

    Article article = articleService.getArticleById(id); // 데이터베이스에서 id를 가져와서 그 id에 해당되는 정보들을 담는다?

    boolean articleExists = articleService.articleExists(id);
    // articleService의 articleExists에서 id값을 가져온것을 boolean형태의 articleExists에 넣는다
    // 그냥 id값 가져와서 있는 id 인지 없는id 인지 확인할라고 만든거 같다

    if (articleExists == false) { //조건문 articleExists가 거짓일때
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id); // 게시글 존재하지 않습니다 출력
      return;
    }

    if(article.memberId != Container.session.loginedMemberId) {
      // 게시글 고유 id가 로그인id랑 고유 id가지고 매치해봐서 글쓴 유저가 아닐경우 권한이 없다고 하는거같음
      // 글쓴이가 수정할 수 있으니께 이사람이 쓴건지 아니면 자기가 안쓴건데 수정할라하는건지 확인하는 조건문 같음
      System.out.println("권한이 없습니다"); // 권한이 없댜 돌아가 안돼 돌아가 바꿔줄맘 없어 돌아가
      return;
    }

    System.out.printf("새 제목 : "); // 만약에 글쓴이와 로그인id가 일치 할경우 수정할 수 있게 수정하는 로직
    String title = Container.scanner.nextLine();
    System.out.printf("새 내용 : ");
    String body = Container.scanner.nextLine();

    articleService.update(id, title, body);
    // 여기서 update로 id title body를 데이터베이스에 업데이트하는거 같아요
    System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
    // 수정된 내용 데이터베이스에 저장완료되어 x번 게시물이 수정되었다고 출력
  }

  public void delete() { // 삭제 로직
    if(Container.session.isLogined() == false) { // session클래스 보니까 고유ID인 loginedMemberId를 -1이 아닐때
      System.out.println("로그인 후 이용해주세요.");  //session안에있는 isLogined가 -1일때는 로그아웃상태 -1이아닐경우 로그인 상태
      return;
    }

    int id = rq.getIntParam("id", 0);
    // int 형 id는 rq의 getIntParam와 같다 getIntParam는 id라는 매개변수와 기본값 0을 받는다?
    if (id == 0) {
      System.out.println("id를 올바르게 입력해주세요.");
      return;
    }

    System.out.printf("== %d번 게시글 삭제 ==\n", id);
    //게시글 삭제 시작 문구

    Article article = articleService.getArticleById(id);
    // 데이터베이스에서 id를 가져와서 그 id에 해당되는 정보들을 담는다?

    boolean articleExists = articleService.articleExists(id);
    // articleService의 articleExists에서 id값을 가져온것을 boolean형태의 articleExists에 넣는다
    // 그냥 id값 가져와서 있는 id 인지 없는id 인지 확인할라고 만든거 같다

    if (articleExists == false) { //조건문 articleExists가 거짓일때
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id); // 게시글 존재하지 않습니다 출력
      return;
    }

    if(article.memberId != Container.session.loginedMemberId) {
      // 게시글 고유 id가 로그인id랑 고유 id가지고 매치해봐서 글쓴 유저가 아닐경우 권한이 없다고 하는거같음
      // 글쓴이가 수정할 수 있으니께 이사람이 쓴건지 아니면 자기가 안쓴건데 수정할라하는건지 확인하는 조건문 같음
      System.out.println("권한이 없습니다");
      // 권한이 없댜 돌아가 안돼 돌아가 바꿔줄맘 없어 돌아가
      return;
    }

    articleService.delete(id);
    // 여기서 update로 id title body를 데이터베이스에서 삭제

    System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
  }

}
