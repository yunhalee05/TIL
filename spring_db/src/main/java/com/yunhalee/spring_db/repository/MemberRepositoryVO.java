package com.yunhalee.spring_db.repository;

import com.yunhalee.spring_db.connection.DBConnectionUtil;
import com.yunhalee.spring_db.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class MemberRepositoryVO {

    private Logger log = LoggerFactory.getLogger(MemberRepositoryVO.class);

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {

//            pstmt.close(); // Exception이 터지면 아래 con.close()가 호출이 안됨 -> 둘다 catch로 잡아야 함
//            con.close();

            close(con, pstmt, null);

        }
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("erorr", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("erorr", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("erorr", e);
            }
        }
    }

    @NotNull
    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
