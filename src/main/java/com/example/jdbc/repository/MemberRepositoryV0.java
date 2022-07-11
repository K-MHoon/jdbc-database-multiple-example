package com.example.jdbc.repository;

import com.example.jdbc.connection.DBConnectionUtil;
import com.example.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getMemberId());
            ps.setInt(2, member.getMoney());
            ps.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        }
    }
    
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        ResultSet rs = null;
        try(Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setString(1, memberId);
            rs = ps.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
        }
        throw new NoSuchElementException("Member not found memberId = " + memberId);
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
