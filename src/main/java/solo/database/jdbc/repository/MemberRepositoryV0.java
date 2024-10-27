package solo.database.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import solo.database.connection.DBConnectionUtil;
import solo.database.jdbc.domain.Member;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id,money) values(?,?)"; //파라미터 바인딩 방식으로 하면 단순하게 데이터 취급만되고
        //만약 그 방법이 아니라면 세세하게 호출을 할 수 있어 해킹 당할 위험이 있다.
        //sql을 전달해주어야 해서 인서트문으로 준비

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            // (?,?) 처음은 1번이라 1번 할당, 두번째는 String 타입이라 setString 으로 지정해주었다.
            pstmt.setInt(2, member.getMoney());
            // (?,?) 은 두번째라 2번, 두번째는 Int타입이라 IntString 으로 지정.
            pstmt.executeUpdate();
            // SQL 커넥션을 통해 디비에 전달, 이것은 Int를 반환한다. 영향반은 DB row수를 반환, 하나의 row를 등록해서 1을 반환해준다.
            // 역량받은 수만큼 돌려보내준다.
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //select 쿼리를 담고있는 통이라고 생각하면 된다.

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery(); //executeUpdate 는 쿼리를 변경 쿼리 조회는 익스큐트 쿼리이다.
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId="+ memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        }finally {
            close(con,pstmt,rs);
        }
    }

    public void update(String memberId,int money) throws SQLException {
        String sql= "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException{
        String sql ="delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();// 여기서 sql익셉션이 터져도 캐치로 잡아버린다.
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    //사용한 리소스들은 모두~~ 다  닫아주어야함
    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}


