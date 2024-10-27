package solo.database.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import solo.database.jdbc.domain.Member;
import solo.database.jdbc.repository.MemberRepositoryV0;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class MemberRepositoryV0Test {
    MemberRepositoryV0 repository= new MemberRepositoryV0();

    @Test
    void crud() throws SQLException{
        //save
        Member member =new Member("memberV7 ", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}",findMember);
        assertThat(findMember).isEqualTo(member);

        repository.update(member.getMemberId(),20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
