package solo.database.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import solo.database.connection.DBConnectionUtil;

import java.sql.Connection;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class DBConnectionUtilTest {
    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}