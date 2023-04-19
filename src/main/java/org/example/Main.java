package org.example;

import org.example.exception.SQLErrorException;

public class Main { //야발 
  public static void main(String[] args) {
    try {
      new App().run(); // main에서 실행 후 App클래스에서 run 함수 실행
    }
    catch (SQLErrorException e) {
      System.out.println(e.getMessage());
      e.getOrigin().printStackTrace();
    }
  }
}

// main에서 실행 → App에서 일단 sql 데이터베이스 연결 → App에서 명령어 입력받음 → 입력받은 명령어에 따라 MemberController 혹은 ArticleController로 이동
// → 각 명령어에 맞는 Controller에 진입 → Controller 안에서 명령어에 맞는 로직으로 진입 → 명령어에 맞는 로직 실행

// 정보를 저장할때 : Controller에서 로직 실행하면서 정보들을 입력받음 → 입력받은걸 값을 Service로 넘김 → Service에 넘어온 입력값들을 다시 repository로 넘김
// → repository에서 최종적으로 입력값들을 sql.append로 INSERT INTO 또는 UPDATE SELECT * FROM ㅇㅇㅇ 으로 데이터 저장 및 확인을 한다.

// 정보를 가져올때 : 사용자가 Id와 Password를 입력하면 Service에서 Repository에 접근해 Id 값을 받아서 그 Id에 맞는 정보들을 확인해봄
// → Id값과 정보들이 일치 하면 데이터베이스에 저장되어있는 정보를 Service로 가져옴 → 가져온 정보를 다시 Controller에 넘겨서 정보를 확인하거나 수정할 수 있음
