package com.market.server.utils;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";
    private static final String LOGIN_OWNER_ID = "LOGIN_OWNER_ID";

    // 인스턴스화 방지
    private SessionUtil() {
    }

    /**
     * 로그인한 유저의 아이디를 세션에서 꺼낸다.
     *
     * @param session 사용자의 세션
     * @return 로그인한 고객의 id 또는 null
     * @author topojs8
     */
    public static String getLoginMemberId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_MEMBER_ID);
    }

    /**
     * 로그인 한 유저의 id를 세션에 저장한다.
     *
     * @param session 사용자의 session
     * @param id      로그인한 고객의 id
     * @author topojs8
     */
    public static void setLoginMemberId(HttpSession session, String id) {
        session.setAttribute(LOGIN_MEMBER_ID, id);
    }


    /**
     * 로그인한 관리자 id를 세션에서 꺼낸다.
     * 로그인 하지 않았다면 null이 반환된다
     *
     * @param session 사용자의 세션
     * @return 로그인한 사장님 id 또는 null
     * @author topojs8
     */
    public static String getLoginOwnerId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_OWNER_ID);
    }

    /**
     * 로그인한 관리자 id를 세션에 저장한다.
     *
     * @param session 사용자의 세션
     * @param id      로그인한 사장님 id
     * @author topojs8
     */
    public static void setLoginOwnerId(HttpSession session, String id) {
        session.setAttribute(LOGIN_OWNER_ID, id);
    }


    /**
     * 해당 세션의 정보를 모두 삭제한다.
     *
     * @param session 사용자의 세션
     * @author topojs8
     */
    public static void clear(HttpSession session) {
        session.invalidate();
    }

    /**
     * 유저 로그인 정보를 삭제한다.
     *
     * @param session 사용자의 세션
     * @author topojs8
     */
    public static void logoutMember(HttpSession session) {
        session.removeAttribute(LOGIN_MEMBER_ID);
    }

    /**
     * 관리자 로그인 정보를 삭제한다.
     *
     * @param session 사용자의 세션
     * @author topojs8
     */
    public static void logoutOwner(HttpSession session) {
        session.removeAttribute(LOGIN_OWNER_ID);
    }
}
