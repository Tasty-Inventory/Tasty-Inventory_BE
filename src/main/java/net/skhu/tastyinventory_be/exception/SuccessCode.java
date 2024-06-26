package net.skhu.tastyinventory_be.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {
    /**
     * 200 OK
     */
    GET_SUCCESS(HttpStatus.OK, "성공적으로 조회했습니다"),
    RE_ISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 재발급을 성공했습니다"),
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "사용자 정보 조회에 성공했습니다"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다"),
    INVENTORY_GET_SUCCESS(HttpStatus.OK, "재고 조회에 성공했습니다"),
    INVENTORY_DELETE_SUCCESS(HttpStatus.OK, "재고 삭제에 성공했습니다"),
    INVENTORY_PATCH_SUCCESS(HttpStatus.OK, "재고 수정이 완료되었습니다"),
    MENU_PATCH_SUCCESS(HttpStatus.OK, "메뉴 수정이 완료되었습니다"),
    MENU_DELETE_SUCCESS(HttpStatus.OK, "메뉴 삭제에 성공했습니다"),
    SOLD_DELETE_SUCCESSCODE(HttpStatus.OK, "메뉴 판매량 삭제에 성공했습니다"),
    INVENTORY_RECORD_GET_SUCCESS(HttpStatus.OK, "재고량 정보 조회에 성공했습니다"),
    INVENTORY_RECORD_PATCH_SUCCESS(HttpStatus.OK, "재고량 정보 수정이 완료되었습니다"),
    INVENTORY_RECORD_DELETE_SUCCESS(HttpStatus.OK, "재고량 정보 삭제에 성공했습니다"),


    /**
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료됐습니다"),
    SIGNOUT_SUCCESS(HttpStatus.CREATED, "로그아웃이 완료됐습니다"),
    INVENTORY_CREATE_SUCCESS(HttpStatus.CREATED, "재고 생성을 완료하였습니다"),
    EMPLOYEE_CREATE_SUCCESS(HttpStatus.CREATED, "직원 생성이 완료되었습니다"),
    SALARY_CREATE_SUCCESS(HttpStatus.CREATED, "급여 생성이 완료되었습니다"),
    MENU_CREATE_SUCCESS(HttpStatus.CREATED, "메뉴 생성이 완료되었습니다"),
    SOLD_CREATE_SUCCESS(HttpStatus.CREATED, "메뉴 판매량 등록을 완료했습니다"),
    INVENTORY_RECORD_CREATE_SUCCESS(HttpStatus.CREATED, "재고 기록 등록을 완료했습니다"),
    SCHEDULE_CREATE_SUCCESS(HttpStatus.CREATED, "스케줄 생성이 완료되었습니다"),

    /**
     * 204 NO_CONTENT
     */
    UNLINK_SUCCESS(HttpStatus.NO_CONTENT, "사용자 탈퇴에 성공했습니다"),
    EMPLOYEE_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "직원 삭제에 성공했습니다"),
    SALARY_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "급여 삭제에 성공했습니다"),
    EMPLOYEE_PATCH_SUCCESS(HttpStatus.NO_CONTENT, "직원 정보 수정이 완료되었습니다"),
    SALARY_PATCH_SUCCESS(HttpStatus.NO_CONTENT, "급여 정보 수정이 완료되었습니다"),
    SCHEDULE_PATCH_SUCCESS(HttpStatus.NO_CONTENT, "스케줄 정보 수정이 완료되었습니다"),
    SCHEDULE_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "스케줄 삭제에 성공했습니다"), 
    SOLD_PATCH_SUCCESSCODE(HttpStatus.NO_CONTENT, "재고량 정보 수정이 완료되었습니다"),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
